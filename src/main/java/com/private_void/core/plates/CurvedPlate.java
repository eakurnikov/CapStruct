package com.private_void.core.plates;

import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.coordinates.Point3D;
import com.private_void.core.geometry.coordinates.ReferenceFrame;
import com.private_void.core.geometry.coordinates.SphericalPoint;
import com.private_void.core.surfaces.capillar_factories.RotatedCapillarFactory;

import static com.private_void.utils.Constants.PI;
import static com.private_void.utils.Generator.generator;

public class CurvedPlate extends Plate {
    private final RotatedCapillarFactory capillarFactory;
    private final double maxAngleR;
    private final double curvRadius;

    public CurvedPlate(final RotatedCapillarFactory capillarFactory, final CartesianPoint center, double capillarsDensity,
                       double maxAngleR, double curvRadius) {
        super(center, capillarFactory.getRadius(), capillarFactory.getLength(), capillarsDensity);
        this.capillarFactory = capillarFactory;
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
                    new SphericalPoint(0.0, PI / 2.0, PI),
                    curvRadius, maxAngleR, maxAngleR);

            SphericalPoint[] capillarsCenters = new SphericalPoint[capillarsAmount];
            SphericalPoint coordinate;

            for (int i = 0; i < capillarsAmount; i++) {
                do {
                    coordinate = coordinateFactory.getCoordinate();
                } while (!isCapillarCoordinateValid(capillarsCenters, coordinate));

                capillarsCenters[i] = coordinate;

                CartesianPoint front = coordinate.convertToCartesian()
                        .shift(curvRadius, 0.0, 0.0)
                        .shift(center);

                capillars.add(capillarFactory.getNewCapillar(front, new ReferenceFrame(
                        front, coordinate.shift(0.0, -PI / 2.0, -PI))));

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

    @Override
    protected boolean isCapillarCoordinateValid(Point3D[] coordinates, Point3D coordinate) {
        double radius = Math.atan(capillarRadius / curvRadius);
        int i = 0;
        while (i < coordinates.length && coordinates[i] != null) {
            if (      (coordinate.getQ2() - coordinates[i].getQ2()) * (coordinate.getQ2() - coordinates[i].getQ2())
                    + (coordinate.getQ3() - coordinates[i].getQ3()) * (coordinate.getQ3() - coordinates[i].getQ3())
                    < 4.0 * radius * radius) {
                return false;
            }
            i++;
        }
        return true;
    }
}