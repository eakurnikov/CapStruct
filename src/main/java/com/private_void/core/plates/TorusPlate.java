package com.private_void.core.plates;

import com.private_void.app.Logger;
import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.coordinates.CylindricalPoint;
import com.private_void.core.geometry.coordinates.Point3D;
import com.private_void.core.geometry.reference_frames.ReferenceFrame;
import com.private_void.core.surfaces.capillar_factories.RotatedTorusFactory;
import com.private_void.utils.Utils;

import static com.private_void.utils.Generator.generator;

public class TorusPlate extends Plate {
    private static final double DETECTORS_DISTANCE_COEFFICIENT = 5.0;

    private final RotatedTorusFactory capillarFactory;
    private final double sideLength;
    private final double width;
    private final double maxAngleR;
    private final CartesianPoint focus;

    public TorusPlate(final RotatedTorusFactory capillarFactory, final CartesianPoint center, double capillarsDensity,
                      double maxAngleR) {
        super(center, capillarFactory.getRadius(), capillarsDensity);
        this.capillarFactory = capillarFactory;
        this.maxAngleR = maxAngleR;
        this.width = capillarFactory.getLength();
        this.focus = center.shift(DETECTORS_DISTANCE_COEFFICIENT * width, 0.0, 0.0);
        this.sideLength = (focus.getX() - width) * Math.tan(maxAngleR);

        this.detector = new Detector(getDetectorsCoordinate(), sideLength);
        createCapillars();
    }

    @Override
    protected void createCapillars() {
        Logger.creatingCapillarsStart();

        double frontSquare = Math.PI * sideLength * sideLength / 4.0;
        double minCapillarSquare = (2.0 * capillarRadius) * (2.0 * capillarRadius);
        double maxCapillarDensity = 1.0 / minCapillarSquare;

        if (capillarsDensity > maxCapillarDensity) {
            Logger.capillarsDensityTooBig(maxCapillarDensity);

            capillarsAmount = (int) (frontSquare / minCapillarSquare);
            // todo заполняю сеткой впритирку
            capillars = null;

        } else if (capillarsDensity > 0.67 * maxCapillarDensity) {
            Logger.capillarsDensityTooBig(maxCapillarDensity);

            /*todo capillarsAmount = ...
            заполняю сеткой с каким-то шагом*/
            capillars = null;

        } else {
            capillarsAmount = (int) (capillarsDensity * frontSquare);

            CylindricalPoint.Factory coordinateFactory = generator().getRadialUniformDistribution(
                    new CylindricalPoint(0.0, 0.0, width),
                    sideLength / 2.0 - capillarRadius);

            CartesianPoint[] capillarsCenters = new CartesianPoint[capillarsAmount];
            CartesianPoint coordinate;
            CylindricalPoint cylindricalCoordinate;

            for (int i = 0; i < capillarsAmount; i++) {
                do {
                    cylindricalCoordinate = coordinateFactory.getCoordinate();
                    coordinate = cylindricalCoordinate.convertToCartesian();
                } while (!isCapillarCoordinateValid(capillarsCenters, coordinate));

                capillarsCenters[i] = coordinate;

                double curvAngleR = Math.atan(cylindricalCoordinate.getR() / (focus.getX() - width));
                double curvRadius = Utils.getTorusCurvRadius(width, curvAngleR);

                CartesianPoint frontCoordinateInGlobalRefFrame = coordinate
                        .rotateAroundOX(cylindricalCoordinate.getPhi() - Math.PI)
                        .shift(coordinate.inverse())

                        .shift(-curvRadius * Math.sin(curvAngleR),
                                0.0,
                                -curvRadius * (1.0 - Math.cos(curvAngleR)))

                        .shift(coordinate)
                        .rotateAroundOX(-cylindricalCoordinate.getPhi() + Math.PI);

//                ReferenceFrame localCapillarRefFrame = ReferenceFrame.builder()
//                        .atPoint(coordinate)
//                        .setAngleAroundOX(cylindricalCoordinate.getPhi() - Math.PI)
//                        .build();
//
//                CartesianPoint coordinateInLocalCapillarRefFrame = CartesianPoint
//                        .getRefFrameConverter(coordinate)
//                        .convert(localCapillarRefFrame)
//                        .shift(-curvRadius * Math.sin(curvAngleR),
//                                0.0,
//                                -curvRadius * (1.0 - Math.cos(curvAngleR)));
//
//                CartesianPoint frontCoordinateInGlobalRefFrame = CartesianPoint
//                        .getRefFrameConverter(coordinateInLocalCapillarRefFrame, localCapillarRefFrame)
//                        .convert(ReferenceFrame.GLOBAL);

                ReferenceFrame refFrame = ReferenceFrame.builder()
                        .atPoint(frontCoordinateInGlobalRefFrame)
                        .setAngleAroundOX(cylindricalCoordinate.getPhi() - Math.PI)
                        .build();

                capillars.add(capillarFactory.getNewCapillar(frontCoordinateInGlobalRefFrame, curvAngleR, refFrame));

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
}