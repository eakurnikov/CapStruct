package com.private_void.core.plates;

import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.CoordinateFactory;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.SphericalCoordinateFactory;
import com.private_void.core.geometry.SphericalPoint;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.CapillarFactory;

import static com.private_void.utils.Generator.generator;
import static com.private_void.utils.Constants.PI;

public class CurvedPlate extends Plate {
    private float maxAngleR;
    private float curvRadius;

    public CurvedPlate(final CapillarFactory capillarFactory, final Point3D center, float capillarsDensity,
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
            // заполняю сеткой впритирку
            capillars = null;

        } else if (capillarsDensity > 0.67f * maxCapillarDensity) {
            System.out.println("Capillars density is very big, so it has been automatically set to " + maxCapillarDensity);
            /*capillarsAmount = ...
            заполняю сеткой с каким-то шагом*/
            capillars = null;

        } else {
            capillarsAmount = (int) (capillarsDensity * frontSquare);

//            CoordinateFactory coordinateFactory = generator().getSphericalUniformDistribution(
//                    center.shift(curvRadius, 0.0f, 0.0f),
//                    new SphericalPoint(0.0f, 0.0f, PI),
//                    curvRadius, maxAngleR / 2, maxAngleR / 2);

            SphericalCoordinateFactory coordinateFactory = generator().getSphericalUniformDistribution1(
                    curvRadius, maxAngleR / 2, maxAngleR / 2);

            SphericalPoint[] capillarsCenters = new SphericalPoint[capillarsAmount];
            SphericalPoint coordinate;

            for (int i = 0; i < capillarsAmount; i++) {
                do {
                    coordinate = coordinateFactory.getCoordinate();
                } while (false/*!isCapillarCoordinateValid(capillarsCenters, coordinate)*/);

                capillarsCenters[i] = coordinate;
                capillars.put(capillarFactory.getNewCapillar(
                        coordinate
                                .shift(new SphericalPoint(0.0f, 0.0f, PI))
                                .convertToCartesian()
                                .shift(curvRadius, 0.0f, 0.0f)
                                .shift(center)),
                        coordinate);
            }
        }

        long finish = System.nanoTime();
        System.out.println();
        System.out.println("Creating capillars time = " + (finish - start) / 1_000_000 + " ms");
    }

    @Override
    protected Point3D getDetectorsCoordinate() {
        return center.shift(curvRadius, 0.0f, 0.0f);
    }

    @Override
    protected boolean isCapillarCoordinateValid(Point3D[] coordinates, Point3D coordinate) {
        int i = 0;
        while (coordinates[i] != null && i < coordinates.length) {
            if (true) {
                return false;
            }
            i++;
        }
        return true;
    }
}