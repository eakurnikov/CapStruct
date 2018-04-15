package com.private_void.core.plates;

import com.private_void.app.Logger;
import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.coordinates.Point3D;
import com.private_void.core.geometry.coordinates.SphericalPoint;
import com.private_void.core.geometry.reference_frames.ReferenceFrame;
import com.private_void.core.surfaces.capillar_factories.RotatedCapillarFactory;

import static com.private_void.utils.Constants.PI;
import static com.private_void.utils.Generator.generator;

public class CurvedPlate extends Plate {
    private final RotatedCapillarFactory capillarFactory;
    private final double maxAngleR;
    private final double curvRadius;
    private final double capillarRadiusR;

    public CurvedPlate(final RotatedCapillarFactory capillarFactory, final CartesianPoint center, double capillarsDensity,
                       double maxAngleR, double curvRadius) {
        super(center, capillarFactory.getRadius(), capillarsDensity);
        this.capillarFactory = capillarFactory;
        this.maxAngleR = maxAngleR;
        this.curvRadius = curvRadius;
        this.capillarRadiusR = Math.atan(capillarRadius / curvRadius);
        this.detector = new Detector(getDetectorsCoordinate(), 0.5*curvRadius *  Math.sin(maxAngleR));
        createCapillars();
    }

    @Override
    protected void createCapillars() {
        Logger.creatingCapillarsStart();

        double frontSquare = 2.0 * PI * curvRadius * curvRadius * (1.0 -  Math.cos(maxAngleR));

//        double coef = 4 / PI; //площадь круга увеличит до площади описанного вокруг него квадрата.

        double minCapillarSquare = 2.0 * PI * curvRadius * curvRadius * (1.0 - Math.cos(Math.asin(capillarRadius / curvRadius)));
        double maxCapillarDensity = 1.0 / minCapillarSquare;

        if (capillarsDensity > 0.67 * maxCapillarDensity) {
            double capillarsCellSideLengthR;

            if (capillarsDensity >= maxCapillarDensity) {
                Logger.capillarsDensityTooBig(maxCapillarDensity);
                capillarsAmount = (int) (frontSquare / minCapillarSquare);
                capillarsCellSideLengthR = 2.0 * capillarRadiusR;
            } else {
                capillarsAmount = (int) (capillarsDensity * frontSquare);
                capillarsCellSideLengthR = Math.sqrt(frontSquare / capillarsAmount);
            }

            int capillarsCounter = 0;
            int pool = (int) (maxAngleR / capillarsCellSideLengthR);
            double plateRadiusR = maxAngleR / 2.0;

            SphericalPoint sphericalCenter = new SphericalPoint(
                    curvRadius - capillarFactory.getLength(), 0.0, 0.0);

            SphericalPoint initialPoint = sphericalCenter
                    .shift(0.0, -plateRadiusR + capillarRadiusR, -plateRadiusR + capillarRadiusR);

            for (int i = 0; i < pool; i++) {
                for (int j = 0; j < pool; j++) {

                    SphericalPoint capillarsEndCenter = initialPoint.shift(
                            0.0, i * capillarsCellSideLengthR, j * capillarsCellSideLengthR);

                    if (      (capillarsEndCenter.getTheta() - sphericalCenter.getTheta())
                            * (capillarsEndCenter.getTheta() - sphericalCenter.getTheta())
                            + (capillarsEndCenter.getPhi() - sphericalCenter.getPhi())
                            * (capillarsEndCenter.getPhi() - sphericalCenter.getPhi())
                            < (plateRadiusR - capillarRadiusR) * (plateRadiusR - capillarRadiusR)) {

                        CartesianPoint capillarsFrontCenter = capillarsEndCenter
                                .shift(capillarFactory.getLength(), PI / 2.0, PI)
                                .convertToCartesian()
                                .shift(curvRadius, 0.0, 0.0)
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
            //todo generate capillars end centers first
            capillarsAmount = (int) (capillarsDensity * frontSquare);

            SphericalPoint.Factory coordinateFactory = generator().getSphericalUniformDistribution(
                    new SphericalPoint(0.0, PI / 2.0, PI),
                    curvRadius, maxAngleR, maxAngleR);

            SphericalPoint[] capillarsFrontCenters = new SphericalPoint[capillarsAmount];
            SphericalPoint capillarsFrontCenter;

            for (int i = 0; i < capillarsAmount; i++) {
                do {
                    capillarsFrontCenter = coordinateFactory.getCoordinate();
                } while (!isCapillarCoordinateValid(capillarsFrontCenters, capillarsFrontCenter));

                capillarsFrontCenters[i] = capillarsFrontCenter;

                CartesianPoint front = capillarsFrontCenter.convertToCartesian()
                        .shift(curvRadius, 0.0, 0.0)
                        .shift(center);

                capillars.add(capillarFactory.getNewCapillar(front,
                        ReferenceFrame.builder()
                                .atPoint(front)
                                .setAngleAroundOY(capillarsFrontCenter.getTheta() - PI / 2.0)
                                .setAngleAroundOZ(-capillarsFrontCenter.getPhi() - PI)
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
        return center.shift(curvRadius, 0.0, 0.0);
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
}