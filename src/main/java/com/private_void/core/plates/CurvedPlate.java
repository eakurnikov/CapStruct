package com.private_void.core.plates;

import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.coordinates.Point3D;
import com.private_void.core.geometry.coordinates.SphericalPoint;
import com.private_void.core.surfaces.CapillarFactory;

import static com.private_void.utils.Generator.generator;
import static com.private_void.utils.Constants.PI;

public class CurvedPlate extends Plate {
    private final double maxAngleR;
    private final double curvRadius;

    public CurvedPlate(final CapillarFactory capillarFactory, final CartesianPoint center, double capillarsDensity,
                       double maxAngleR, double curvRadius) {
        super(capillarFactory, center, capillarsDensity);
        this.maxAngleR = maxAngleR;
        this.curvRadius = curvRadius;
        createCapillars();
        this.detector = new Detector(getDetectorsCoordinate(), curvRadius *  Math.sin(maxAngleR));
    }

    @Override
    protected void createCapillars() {
        System.out.println("Creating capillars start ...");
        long start = System.nanoTime();

        double frontSquare = 2.0 * PI * curvRadius * curvRadius * (1.0 -  Math.cos(maxAngleR));

//        double coef = 4 / PI; //площадь круга увеличит до площади описанного вокруг него квадрата.

        double minCapillarSquare = 2.0 * PI * curvRadius * curvRadius * (1.0 - Math.cos(Math.asin(capillarRadius / curvRadius)));
        double maxCapillarDensity = 1.0 / minCapillarSquare;

        if (capillarsDensity > maxCapillarDensity) {
            System.out.println("Capillars density is too big, it has been automatically set to " + maxCapillarDensity);
            capillarsAmount = (int) (frontSquare / minCapillarSquare);
            // todo заполняю сеткой впритирку
            capillars = null;

        } else if (capillarsDensity > 0.67 * maxCapillarDensity) {
            System.out.println("Capillars density is very big, so it has been automatically set to " + maxCapillarDensity);
            /*todo capillarsAmount = ...
            заполняю сеткой с каким-то шагом*/
            capillars = null;

        } else {
            capillarsAmount = (int) (capillarsDensity * frontSquare);

            SphericalPoint.Factory coordinateFactory = generator().getSphericalUniformDistribution(
                    new SphericalPoint(0.0, PI / 2.0, PI),// тета отсчитывается от оси игрек, фи от оси икс, поворачиваю так, чтобы координаты попадали на мою пластину
                    curvRadius, maxAngleR, maxAngleR);

            SphericalPoint[] capillarsCenters = new SphericalPoint[capillarsAmount];
            SphericalPoint coordinate;

            for (int i = 0; i < capillarsAmount; i++) {
                do {
                    coordinate = coordinateFactory.getCoordinate();
                } while (false/*!isCapillarCoordinateValid(capillarsCenters, coordinate)*/);

                capillarsCenters[i] = coordinate;

                CartesianPoint front = coordinate.convertToCartesian()
                        .shift(curvRadius, 0.0, 0.0)
                        .shift(center);

                SphericalPoint position = coordinate.shift(0.0, -PI / 2.0, -PI);

                capillars.add(capillarFactory.getNewCapillar(front, position));

                if (i % (capillarsAmount / 10) == 0.0) System.out.println("    ... " + (i * 100 / capillarsAmount) + "% capillars created");
            }
        }

        long finish = System.nanoTime();
        System.out.println("Creating capillars finish. Total time = " + (finish - start) / 1_000_000 + " ms");
        System.out.println();
    }

    @Override
    protected CartesianPoint getDetectorsCoordinate() {
        return center.shift(curvRadius, 0.0, 0.0);
    }

    @Override //TODO сделать наконец этот метод
    protected boolean isCapillarCoordinateValid(Point3D[] coordinates, Point3D coordinate) {
        double radius = 2.0 * Math.asin(capillarRadius / curvRadius);
        int i = 0;
        while (coordinates[i] != null && i < coordinates.length) {
            if (      (coordinate.getQ2() - coordinates[i].getQ2()) * (coordinate.getQ2() - coordinates[i].getQ2())
                    + (coordinate.getQ3() - coordinates[i].getQ3()) * (coordinate.getQ3() - coordinates[i].getQ3())
                    < radius * radius) {
                return false;
            }
            i++;
        }
        return true;
    }

//    @Override
//    protected boolean isCapillarCoordinateValid(Point3D[] coordinates, Point3D coordinate) {
////        ////////////
////        CartesianPoint[] capillarsCenters = new CartesianPoint[capillarsAmount];
////        CartesianPoint coordinate;
////        SphericalPoint sper;
////
////        for (int i = 0; i < capillarsAmount; i++) {
////            do {
////                sper = coordinateFactory.getCoordinate();
////                coordinate = sper.convertToCartesian();
////            } while (!isCapillarCoordinateValid(capillarsCenters, coordinate));
////
////            capillarsCenters[i] = coordinate;
////
////            CartesianPoint front = coordinate.getNewByShift(curvRadius, 0.0, 0.0).shift(center);
////
////            SphericalPoint position = sper.getNewByShift(0.0, -PI / 2.0, -PI);
////
////            capillars.add(capillarFactory.getNewCapillar(front, position));
////        /////////
//
//        int i = 0;
//        while (coordinates[i] != null && i < coordinates.length) {
//            if (      (coordinate.getQ1() - coordinates[i].getQ1()) * (coordinate.getQ1() - coordinates[i].getQ1())
//                    + (coordinate.getQ2() - coordinates[i].getQ2()) * (coordinate.getQ2() - coordinates[i].getQ2())
//                    + (coordinate.getQ3() - coordinates[i].getQ3()) * (coordinate.getQ3() - coordinates[i].getQ3())
//                    < 4.0 * capillarRadius * capillarRadius) {
//                return false;
//            }
//            i++;
//        }
//        return true;
//    }
}