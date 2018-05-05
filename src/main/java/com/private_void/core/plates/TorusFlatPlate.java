package com.private_void.core.plates;

import com.private_void.app.Logger;
import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.coordinates.CylindricalPoint;
import com.private_void.core.geometry.space_3D.coordinates.Point3D;
import com.private_void.core.geometry.space_3D.reference_frames.ReferenceFrame;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.capillar_factories.RotatedTorusFactory;
import com.private_void.utils.Utils;

import java.io.FileWriter;
import java.io.IOException;

import static com.private_void.utils.Generator.generator;

public class TorusFlatPlate extends Plate {
    private static final double DETECTORS_DISTANCE_COEFFICIENT = 20.0;

    private final RotatedTorusFactory capillarFactory;
    private final double maxAngleR;
    private final double plateRadius;
    private final double width;
    private final CartesianPoint focus;

    public TorusFlatPlate(final RotatedTorusFactory capillarFactory, final CartesianPoint center, double capillarsDensity,
                          double maxAngleR) {
        super(center, capillarFactory.getRadius(), capillarsDensity);
        this.capillarFactory = capillarFactory;
        this.maxAngleR = maxAngleR;
        this.width = capillarFactory.getLength();
        this.focus = center.shift(DETECTORS_DISTANCE_COEFFICIENT * width, 0.0, 0.0);
        this.plateRadius = (focus.getX() - width) * Math.tan(maxAngleR);
        this.detector = new Detector(getDetectorsCoordinate(), 2.0 / 2.0 * plateRadius);
        createCapillars();
    }

    @Override
    protected void createCapillars() {
        Logger.creatingCapillarsStart();

        double frontSquare = Math.PI * plateRadius * plateRadius;
        double minCapillarSquare = (2.0 * capillarRadius) * (2.0 * capillarRadius);
        double maxCapillarDensity = 1.0 / minCapillarSquare;
        double plateEffectiveRadius = plateRadius - capillarRadius;

        if (capillarsDensity > 0.67 * maxCapillarDensity) {
            double capillarsCellSideLength;

            if (capillarsDensity >= maxCapillarDensity) {
                Logger.capillarsDensityTooBig(maxCapillarDensity);
                capillarsAmount = (int) (frontSquare / minCapillarSquare);
                capillarsCellSideLength = 2.0 * capillarRadius;
            } else {
                capillarsAmount = (int) (capillarsDensity * frontSquare);
                capillarsCellSideLength = Math.sqrt(frontSquare / capillarsAmount);
            }

            int capillarsCounter = 0;
            int pool = (int) (2.0 * plateRadius / capillarsCellSideLength);

            CartesianPoint initialPoint = center.shift(width, -plateEffectiveRadius, -plateEffectiveRadius);

            for (int i = 0; i < pool + 1; i++) {
                for (int j = 0; j < pool + 1; j++) {

                    CartesianPoint cellCenter = initialPoint.shift(
                            0.0, i * capillarsCellSideLength, j * capillarsCellSideLength);

                    if (      (cellCenter.getY() - center.getY()) * (cellCenter.getY() - center.getY())
                            + (cellCenter.getZ() - center.getZ()) * (cellCenter.getZ() - center.getZ())
                            < plateEffectiveRadius * plateEffectiveRadius) {

                        CartesianPoint capillarsEndCenter;
                        if (capillarsDensity >= maxCapillarDensity) {
                            capillarsEndCenter = cellCenter;
                        } else {
                            capillarsEndCenter = generator().getXFlatUniformDistribution(cellCenter,
                                    capillarsCellSideLength / 2.0 - capillarRadius,
                                    capillarsCellSideLength / 2.0 - capillarRadius)
                                    .getCoordinate();
                        }

                        capillars.add(createCapillarAtPoint(capillarsEndCenter, capillarsEndCenter.convertToCylindrical()));

                        if (++capillarsCounter % (capillarsAmount / 10) == 0.0) {
                            Logger.createdCapillarsPercent(i * 100 / capillarsAmount);
                        }
                    }
                }
            }
        } else {
            capillarsAmount = (int) (capillarsDensity * frontSquare);

            CylindricalPoint.Factory coordinateFactory = generator().getRadialUniformDistribution(
                    new CylindricalPoint(0.0, 0.0, width), plateEffectiveRadius);

            CartesianPoint[] capillarsEndCenters = new CartesianPoint[capillarsAmount];
            CartesianPoint capillarsEndCenter;
            CylindricalPoint cylindricalRandomPoint;

            for (int i = 0; i < capillarsAmount; i++) {
                do {
                    cylindricalRandomPoint = coordinateFactory.getCoordinate();
                    capillarsEndCenter = cylindricalRandomPoint.convertToCartesian();
                } while (!isCapillarCoordinateValid(capillarsEndCenters, capillarsEndCenter));

                capillarsEndCenters[i] = capillarsEndCenter;

                capillars.add(createCapillarAtPoint(capillarsEndCenter, cylindricalRandomPoint));

                if (i % (capillarsAmount / 10) == 0.0) {
                    Logger.createdCapillarsPercent(i * 100 / capillarsAmount);
                }
            }
        }

        Logger.creatingCapillarsFinish();
    }

    @Override
    protected CartesianPoint getDetectorsCoordinate() {
        return focus;
    }

    @Override
    protected boolean isCapillarCoordinateValid(Point3D[] coordinates, Point3D coordinate) {
        int i = 0;
        while (i < coordinates.length && coordinates[i] != null) {
            if ((coordinate.getQ2() - coordinates[i].getQ2()) * (coordinate.getQ2() - coordinates[i].getQ2())
                    + (coordinate.getQ3() - coordinates[i].getQ3()) * (coordinate.getQ3() - coordinates[i].getQ3())
                    < 4.0 * capillarRadius * capillarRadius) {
                return false;
            }
            i++;
        }
        return true;
    }

    private Capillar createCapillarAtPoint(final CartesianPoint capillarsEndCenter, final CylindricalPoint cylindricalPoint) {
        double curvAngleR = Math.atan(cylindricalPoint.getR() / (focus.getX() - width));
        double curvRadius = Utils.getTorusCurvRadius(width, curvAngleR);

        ReferenceFrame capillarsEndRefFrame = ReferenceFrame.builder()
                .atPoint(capillarsEndCenter)
                .setAngleAroundOX(cylindricalPoint.getPhi())
                .build();

        ReferenceFrame.Converter converter = new ReferenceFrame.Converter(capillarsEndRefFrame);

        CartesianPoint capillarsFrontCenter = converter.convertBack(
                converter.convert(capillarsEndCenter)
                        .shift(-curvRadius * Math.sin(curvAngleR),
                                0.0,
                                curvRadius * (1.0 - Math.cos(curvAngleR))));

        ReferenceFrame capillarsFrontRefFrame = ReferenceFrame.builder()
                .atPoint(capillarsFrontCenter)
                .setAngleAroundOX(cylindricalPoint.getPhi())
                .build();

        return capillarFactory.getNewCapillar(capillarsFrontCenter, curvAngleR, capillarsFrontRefFrame);
    }

    private void writeDataInFile() {
        try (FileWriter writer = new FileWriter("capillar_plate_info.txt")) {
            writer.write("Torus plate\n");
            writer.write("Front center coordinate = ("
                    + center.getX() + ", " + center.getY() + ", " + center.getZ() + ")\n");
            writer.write("Plate's radius = " + this.plateRadius + "\n");
            writer.write("Plate's curv angle = " + this.maxAngleR + "\n");
            writer.write("Plate's focus point = " + this.focus + "\n");
            writer.write("Capillars amount = " + this.capillarsAmount + "\n");
            writer.write("Capillars radius = " + this.capillarRadius + "\n");
            writer.write("Capillars density = " + this.capillarsDensity + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}