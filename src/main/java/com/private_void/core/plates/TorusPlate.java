package com.private_void.core.plates;

import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.CoordinateFactory;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.surfaces.CapillarFactory;

import static com.private_void.utils.Generator.generator;

public class TorusPlate extends Plate {
    private float angleR;

    public TorusPlate(final CapillarFactory capillarFactory, final Point3D center, int capillarsAmount,
                      float capillarsDensity, float angleR) {
        super(capillarFactory, center, capillarsAmount, capillarsDensity);
        this.angleR = angleR;
        createCapillars();
        this.detector = new Detector(getDetectorsCoordinate(), sideLength);
    }

    @Override
    protected void createCapillars() throws IllegalArgumentException {
        long start = System.nanoTime();

        float frontSquare = capillarsAmount / capillarsDensity;
        float minFrontSquare = capillarsAmount * (2 * capillarRadius) * (2 * capillarRadius);

        if (frontSquare < minFrontSquare) {
            throw new IllegalArgumentException();
        }

        if (frontSquare < 1.5 * minFrontSquare) {
            // можно сеткой
            throw new IllegalArgumentException();
        } else {
            sideLength = (float) Math.sqrt(frontSquare);

            CoordinateFactory coordinateFactory = generator().getXPlanarUniformDistribution(center.getX(),
                    center.getY() - sideLength / 2 + capillarRadius,
                    center.getY() + sideLength / 2 - capillarRadius,

                    center.getZ() - sideLength / 2 + capillarRadius,
                    center.getZ() + sideLength / 2 - capillarRadius);

            Point3D[] capillarsCenters = new Point3D[capillarsAmount];
            Point3D coordinate;

            for (int i = 0; i < capillarsAmount; i++) {
                do {
                    coordinate = coordinateFactory.getCoordinate();
                } while (!isCapillarCoordinateValid(capillarsCenters, coordinate));

                capillarsCenters[i] = coordinate;
                capillars.add(capillarFactory.getNewCapillar(coordinate));
            }
        }

        long finish = System.nanoTime();
        System.out.println();
        System.out.println("Creating capillars time = " + (finish - start) / 1_000_000 + " ms");
    }

    @Override
    protected Point3D getDetectorsCoordinate() {
        return new Point3D(center.getX() + width, center.getY(), center.getZ());
    }

    @Override
    protected boolean isCapillarCoordinateValid(Point3D[] coordinates, Point3D coordinate) {
        int i = 0;
        while (coordinates[i] != null && i < coordinates.length) {
            if ((coordinate.getY() - coordinates[i].getY()) * (coordinate.getY() - coordinates[i].getY())
                    + (coordinate.getZ() - coordinates[i].getZ()) * (coordinate.getZ() - coordinates[i].getZ())
                    < 4 * capillarRadius * capillarRadius) {
                return false;
            }
            i++;
        }
        return true;
    }
}