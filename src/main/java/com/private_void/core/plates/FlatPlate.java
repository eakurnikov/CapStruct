package com.private_void.core.plates;

import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.CoordinateFactory;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.surfaces.CapillarFactory;

import static com.private_void.utils.Generator.generator;

public class FlatPlate extends Plate {
    private float sideLength;

    public FlatPlate(final CapillarFactory capillarFactory, final Point3D center, float capillarsDensity,
                     float sideLength) {
        super(capillarFactory, center, capillarsDensity);
        this.sideLength = sideLength;
        createCapillars();
        this.detector = new Detector(getDetectorsCoordinate(), sideLength * 1.0f);
    }

    @Override
    protected void createCapillars() {
        long start = System.nanoTime();

        float frontSquare = sideLength * sideLength;
        float minCapillarSquare = (2.0f * capillarRadius) * (2.0f * capillarRadius);
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

            CoordinateFactory coordinateFactory = generator().getXFlatUniformDistribution(center,
                    sideLength / 2 - capillarRadius,
                    sideLength / 2 - capillarRadius);

            Point3D[] capillarsCenters = new Point3D[capillarsAmount];
            Point3D coordinate;

            for (int i = 0; i < capillarsAmount; i++) {
                do {
                    coordinate = coordinateFactory.getCoordinate();
                } while (!isCapillarCoordinateValid(capillarsCenters, coordinate));

                capillarsCenters[i] = coordinate;
                capillars.add(capillarFactory.getNewCapillar(coordinate, null));
            }
        }

        long finish = System.nanoTime();
        System.out.println();
        System.out.println("Creating capillars time = " + (finish - start) / 1_000_000 + " ms");
    }

//    @Override
//    protected void createCapillars() throws IllegalArgumentException {
//        long start = System.nanoTime();
//
//        float domainsAmount = capillarsAmount / CAPILLARS_PER_DOMAIN_AMOUNT;
//        int domainsAmountPerLine = (int) Math.sqrt(domainsAmount);
//
//        float domainSquare = CAPILLARS_PER_DOMAIN_AMOUNT / capillarsDensity;
//        if (domainSquare < 4 * capillarRadius * capillarRadius * CAPILLARS_PER_DOMAIN_AMOUNT) {
//            throw new IllegalArgumentException();
//        }
//        float domainSideLength = (float) Math.sqrt(domainSquare);
//
//        sideLength = domainsAmountPerLine * domainSideLength;
//
//        float initialX = center.getX();
//        float initialY = center.getY() - sideLength / 2;
//        float initialZ = center.getZ() - sideLength / 2;
//
//        Point3D coordinate;
//        Point3D[] capillarsCenters;
//        CoordinateFactory coordinateFactory;
//
//        for (int y = 0; y < domainsAmountPerLine; y++) {
//            for (int z = 0; z < domainsAmountPerLine; z++) {
//                capillarsCenters = new Point3D[CAPILLARS_PER_DOMAIN_AMOUNT];
//
//                coordinateFactory = generator().getXFlatUniformDistribution(initialX,
//                        initialY + capillarRadius + domainSideLength *  y,
//                        initialY - capillarRadius + domainSideLength * (y + 1),
//
//                        initialZ + capillarRadius + domainSideLength *  z,
//                        initialZ - capillarRadius + domainSideLength * (z + 1));
//
//                for (int i = 0; i < CAPILLARS_PER_DOMAIN_AMOUNT; i++) {
//                    do {
//                        coordinate = coordinateFactory.getCoordinate();
//                    } while (!isCapillarCoordinateValid(capillarsCenters, coordinate));
//
//                    capillarsCenters[i] = coordinate;
//                    capillars.add(capillarFactory.getNewCapillar(capillarsCenters[i]));
//                }
//            }
//        }
//
//        long finish = System.nanoTime();
//        System.out.println();
//        System.out.println("Creating capillars time = " + (finish - start) / 1_000_000 + " ms");
//    }

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