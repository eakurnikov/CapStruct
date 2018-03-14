package com.private_void.core.plates;

import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.*;
import com.private_void.core.surfaces.CapillarFactory;

import static com.private_void.utils.Generator.generator;
import static com.private_void.utils.Constants.PI;

public class CurvedPlate extends Plate {
    private float maxAngleR;
    private float curvRadius;

    public CurvedPlate(final CapillarFactory capillarFactory, final CartesianPoint center, float capillarsDensity,
                       float maxAngleR, float curvRadius) {
        super(capillarFactory, center, capillarsDensity);
        this.maxAngleR = maxAngleR;
        this.curvRadius = curvRadius;
        createCapillars();
        this.detector = new Detector(getDetectorsCoordinate(),curvRadius * (float) Math.sin(maxAngleR));
    }

    @Override
    protected void createCapillars() {
        long start = System.nanoTime();
        float frontSquare = 2.0f * PI * curvRadius * curvRadius * (1 - (float) Math.cos(maxAngleR));

        float coef = 4 / PI; //площадь круга увеличит до площади описанного вокруг него квадрата. Т.к. у меня все таки сегменты, это тоже приближение
        float minCapillarSquare = coef * 2.0f * PI * curvRadius * curvRadius
                * (1 - (float) Math.cos(Math.asin(capillarRadius / curvRadius)));
        float maxCapillarDensity = 1.0f / minCapillarSquare;

        if (capillarsDensity > maxCapillarDensity) {
            System.out.println("Capillars density is too big, it has been automatically set to " + maxCapillarDensity);
            capillarsAmount = (int) (frontSquare / minCapillarSquare);
            // todo заполняю сеткой впритирку
            capillars = null;

        } else if (capillarsDensity > 0.67f * maxCapillarDensity) {
            System.out.println("Capillars density is very big, so it has been automatically set to " + maxCapillarDensity);
            /*todo capillarsAmount = ...
            заполняю сеткой с каким-то шагом*/
            capillars = null;

        } else {
            capillarsAmount = (int) (capillarsDensity * frontSquare);

            SphericalCoordinateFactory coordinateFactory = generator().getSphericalUniformDistribution(
                    new SphericalPoint(0.0f, PI / 2, PI),// тета отсчитывается от оси игрек, фи от оси икс, поворачиваю так, чтобы координаты попадали на мою пластину
                    curvRadius, maxAngleR / 2, maxAngleR / 2);

            SphericalPoint[] capillarsCenters = new SphericalPoint[capillarsAmount];
            SphericalPoint coordinate;

            for (int i = 0; i < capillarsAmount; i++) {
                do {
                    coordinate = coordinateFactory.getCoordinate();
                } while (!isCapillarCoordinateValid(capillarsCenters, coordinate));

                capillarsCenters[i] = coordinate;

                CartesianPoint front = coordinate.convertToCartesian()
                        .shift(curvRadius, 0.0f, 0.0f)
                        .shift(center);

                SphericalPoint position = coordinate.shift(0.0f, -PI / 2, -PI);

                capillars.add(capillarFactory.getNewCapillar(front, position));
            }
        }

        long finish = System.nanoTime();
        System.out.println();
        System.out.println("Creating capillars time = " + (finish - start) / 1_000_000 + " ms");
    }

    @Override
    protected CartesianPoint getDetectorsCoordinate() {
        return center.getNewByShift(curvRadius, 0.0f, 0.0f);
    }

    @Override
    protected boolean isCapillarCoordinateValid(Point3D[] coordinates, Point3D coordinate) {
        float radius = 2.0f * (float) Math.asin(capillarRadius / curvRadius);
        int i = 0;
        while (coordinates[i] != null && i < coordinates.length) {
            if ((coordinate.getQ2() - coordinates[i].getQ2()) * (coordinate.getQ2() - coordinates[i].getQ2())
                    + (coordinate.getQ3() - coordinates[i].getQ3()) * (coordinate.getQ3() - coordinates[i].getQ3())
                    < radius * radius) {
                return false;
            }
            i++;
        }
        return true;
    }
}