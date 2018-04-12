package com.private_void.core.plates;

import com.private_void.app.Logger;
import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.coordinates.CylindricalPoint;
import com.private_void.core.geometry.coordinates.Point3D;
import com.private_void.core.geometry.coordinates.ReferenceFrame;
import com.private_void.core.surfaces.capillar_factories.RotatedTorusFactory;

import static com.private_void.utils.Generator.generator;

public class TorusPlate extends Plate {
    private final RotatedTorusFactory capillarFactory;
    private final double sideLength;
    private final double width;
    private final double maxAngleR;
    private final CartesianPoint focus;

    public TorusPlate(final RotatedTorusFactory capillarFactory, final CartesianPoint center, double capillarsDensity,
                      double sideLength, double maxAngleR) {
        super(center, capillarFactory.getRadius(), capillarsDensity);
        this.capillarFactory = capillarFactory;
        this.sideLength = sideLength; //TODO возможно ее нужно вычислять на основе макс угла, а не тупо задавать
        this.width = capillarFactory.getLength();
        this.maxAngleR = maxAngleR;
        this.focus = center.shift(1.0 * width, 0.0, 0.0); //TODO посчитать нормально, спросить у Алексея, на каком расстоянии
        this.detector = new Detector(getDetectorsCoordinate(), sideLength);
        createCapillars();
    }

    @Override
    protected void createCapillars() {
        Logger.creatingCapillarsStart();

        double frontSquare = sideLength * sideLength;
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

            CylindricalPoint.Factory coordinateFactory = generator().getRadialUniformDistribution(sideLength / 1.5);
            //focus.getX() * Math.tan(maxAngleR)
            CylindricalPoint cylCoordinate;

            CartesianPoint[] capillarsCenters = new CartesianPoint[capillarsAmount];
            CartesianPoint coordinate;

            for (int i = 0; i < capillarsAmount; i++) {
                do {
                    cylCoordinate = coordinateFactory.getCoordinate();
                    coordinate = cylCoordinate.convertToCartesian();
                } while (!isCapillarCoordinateValid(capillarsCenters, coordinate));

                capillarsCenters[i] = coordinate;

                capillars.add(capillarFactory.getNewCapillar(
                        coordinate,
                        maxAngleR,
                        ReferenceFrame.builder()
                                .atPoint(coordinate)
                                .setAngleAroundOX(Math.PI / 2.0)
                                //.setAngleAroundOX(cylCoordinate.getPhi() - Math.PI)
                                .build())
                       );

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