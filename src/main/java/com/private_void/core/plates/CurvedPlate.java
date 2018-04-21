package com.private_void.core.plates;

import com.private_void.app.Logger;
import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.coordinates.Point3D;
import com.private_void.core.geometry.coordinates.SphericalPoint;
import com.private_void.core.geometry.reference_frames.ReferenceFrame;
import com.private_void.core.surfaces.capillar_factories.RotatedCapillarFactory;

import java.io.FileWriter;
import java.io.IOException;

import static com.private_void.utils.Constants.PI;
import static com.private_void.utils.Generator.generator;

public class CurvedPlate extends Plate {
    private final RotatedCapillarFactory capillarFactory;
    private final double maxAngleR;
    private final double frontSurfaceRadius;
    private final double endSurfaceRadius;
    private final double capillarRadiusR;

    public CurvedPlate(final RotatedCapillarFactory capillarFactory, final CartesianPoint center, double capillarsDensity,
                       double maxAngleR, double frontSurfaceRadius) {
        super(center, capillarFactory.getRadius(), capillarsDensity);
        this.capillarFactory = capillarFactory;
        this.maxAngleR = maxAngleR;
        this.frontSurfaceRadius = frontSurfaceRadius;
        this.endSurfaceRadius = frontSurfaceRadius - capillarFactory.getLength();
        this.capillarRadiusR = Math.asin(capillarRadius / endSurfaceRadius);
        this.detector = new Detector(getDetectorsCoordinate(), 2.0 / 2.0 * endSurfaceRadius * Math.sin(maxAngleR));
        createCapillars();
        writeDataInFile();
    }

    @Override
    protected void createCapillars() {
        Logger.creatingCapillarsStart();

        double frontSquare = 2.0 * PI * endSurfaceRadius * endSurfaceRadius * (1.0 -  Math.cos(maxAngleR));
        double minCapillarSquare = 2.0 * PI * endSurfaceRadius * endSurfaceRadius * (1.0 - Math.cos(capillarRadiusR));
        double maxCapillarDensity = 1.0 / minCapillarSquare;
        double plateEffectiveRadiusR = maxAngleR - capillarRadiusR;
//        double coef = 4 / PI; //площадь круга увеличит до площади описанного вокруг него квадрата.

        if (capillarsDensity > 0.67 * maxCapillarDensity) {
            double capillarsCellSideLengthR;

            if (capillarsDensity >= maxCapillarDensity) {
                Logger.capillarsDensityTooBig(maxCapillarDensity);
                capillarsAmount = (int) (frontSquare / minCapillarSquare);
                capillarsCellSideLengthR = 2.0 * capillarRadiusR;
            } else {
                capillarsAmount = (int) (capillarsDensity * frontSquare);
                double capillarsCellSideLength = Math.sqrt(frontSquare / capillarsAmount);
                capillarsCellSideLengthR = 2.0 * Math.asin((capillarsCellSideLength / 2.0) / endSurfaceRadius);
            }

            int capillarsCounter = 0;
            int pool = (int) (2.0 * maxAngleR / capillarsCellSideLengthR);

            SphericalPoint sphericalCenter = new SphericalPoint(endSurfaceRadius, 0.0, 0.0);
            SphericalPoint initialPoint = sphericalCenter
                    .shift(0.0, -plateEffectiveRadiusR, -plateEffectiveRadiusR);

            for (int i = 0; i < pool; i++) {
                for (int j = 0; j < pool; j++) {

                    SphericalPoint cellCenter = initialPoint.shift(
                            0.0, i * capillarsCellSideLengthR, j * capillarsCellSideLengthR);

                    if (      (cellCenter.getTheta() - sphericalCenter.getTheta())
                            * (cellCenter.getTheta() - sphericalCenter.getTheta())
                            + (cellCenter.getPhi() - sphericalCenter.getPhi())
                            * (cellCenter.getPhi() - sphericalCenter.getPhi())
                            < plateEffectiveRadiusR * plateEffectiveRadiusR) {

                        SphericalPoint capillarsEndCenter;
                        if (capillarsDensity >= maxCapillarDensity) {
                            capillarsEndCenter = cellCenter;
                        } else {
                            capillarsEndCenter = generator().getSphericalUniformDistribution(
                                    new SphericalPoint(0.0, cellCenter.getTheta(), cellCenter.getPhi()),
                                    endSurfaceRadius,
                                    capillarsCellSideLengthR / 2.0 - capillarRadiusR,
                                    capillarsCellSideLengthR / 2.0 - capillarRadiusR)
                                    .getCoordinate();
                        }

                        CartesianPoint capillarsFrontCenter = capillarsEndCenter
                                .shift(capillarFactory.getLength(), PI / 2.0, PI)
                                .convertToCartesian()
                                .shift(frontSurfaceRadius, 0.0, 0.0)
                                .shift(center);

                        capillars.add(capillarFactory.getNewCapillar(capillarsFrontCenter,
                                ReferenceFrame.builder()
                                        .atPoint(capillarsFrontCenter)
                                        .setAngleAroundOY(capillarsEndCenter.getTheta())
                                        .setAngleAroundOZ(-capillarsEndCenter.getPhi())
                                        .build()));

                        if (++capillarsCounter % (capillarsAmount / 10) == 0.0) {
                            Logger.createdCapillarsPercent(i * 100 / capillarsAmount);
                        }
                    }
                }
            }
        } else {
            capillarsAmount = (int) (capillarsDensity * frontSquare);

            SphericalPoint.Factory coordinateFactory = generator().getSphericalUniformDistribution(
                    new SphericalPoint(0.0, PI / 2.0, PI),
                    endSurfaceRadius, plateEffectiveRadiusR, plateEffectiveRadiusR);

            SphericalPoint[] capillarsEndCenters = new SphericalPoint[capillarsAmount];
            SphericalPoint capillarsEndCenter;

            for (int i = 0; i < capillarsAmount; i++) {
                do {
                    capillarsEndCenter = coordinateFactory.getCoordinate();
                } while (!isCapillarCoordinateValid(capillarsEndCenters, capillarsEndCenter));

                capillarsEndCenters[i] = capillarsEndCenter;

                CartesianPoint capillarsFrontCenter = capillarsEndCenter
                        .shift(capillarFactory.getLength(), 0.0, 0.0)
                        .convertToCartesian()
                        .shift(frontSurfaceRadius, 0.0, 0.0)
                        .shift(center);

                capillars.add(capillarFactory.getNewCapillar(capillarsFrontCenter,
                        ReferenceFrame.builder()
                                .atPoint(capillarsFrontCenter)
                                .setAngleAroundOY(capillarsEndCenter.getTheta() - PI / 2.0)
                                .setAngleAroundOZ(-capillarsEndCenter.getPhi() - PI)
                                .build()));

                if (i % (capillarsAmount / 10) == 0.0) {
                    Logger.createdCapillarsPercent(i * 100 / capillarsAmount);
                }
            }
        }

        Logger.creatingCapillarsFinish();
    }

    @Override
    protected CartesianPoint getDetectorsCoordinate() {
        return center.shift(frontSurfaceRadius, 0.0, 0.0);
    }

    @Override
    protected boolean isCapillarCoordinateValid(Point3D[] coordinates, Point3D coordinate) {
        int i = 0;
        while (i < coordinates.length && coordinates[i] != null) {
            if (      (coordinate.getQ2() - coordinates[i].getQ2()) * (coordinate.getQ2() - coordinates[i].getQ2())
                    + (coordinate.getQ3() - coordinates[i].getQ3()) * (coordinate.getQ3() - coordinates[i].getQ3())
                    < 4.0 * capillarRadiusR * capillarRadiusR) {
                return false;
            }
            i++;
        }
        return true;
    }

    private void writeDataInFile() {
        try (FileWriter writer = new FileWriter("capillar_plate_info.txt")) {
            writer.write("Curved plate\n");
            writer.write("Front center coordinate = ("
                    + center.getX() + ", " + center.getY() + ", " + center.getZ() + ")\n");
            writer.write("Plate's curv radius = " + this.frontSurfaceRadius + "\n");
            writer.write("Plate's curv angle = " + this.maxAngleR + "\n");
            writer.write("Capillars amount = " + this.capillarsAmount + "\n");
            writer.write("Capillars radius = " + this.capillarRadius + "\n");
            writer.write("Capillars density = " + this.capillarsDensity + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}