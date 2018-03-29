package com.private_void.core.plates;

import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.CoordinateFactory;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.SphericalPoint;
import com.private_void.core.surfaces.CapillarFactory;

import static com.private_void.utils.Generator.generator;

public class FlatPlate extends Plate {
    private double sideLength;

    public FlatPlate(final CapillarFactory capillarFactory, final CartesianPoint center, double capillarsDensity,
                     double sideLength) {
        super(capillarFactory, center, capillarsDensity);
        this.sideLength = sideLength;
        createCapillars();
        this.detector = new Detector(getDetectorsCoordinate(), sideLength * 1.0);
    }

    @Override
    protected void createCapillars() {
        System.out.println("Creating capillars start ...");
        long start = System.nanoTime();

        double frontSquare = sideLength * sideLength;
        double minCapillarSquare = (2.0 * capillarRadius) * (2.0 * capillarRadius);
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

            CoordinateFactory coordinateFactory = generator().getXFlatUniformDistribution(center,
                    sideLength / 2 - capillarRadius,
                    sideLength / 2 - capillarRadius);

            CartesianPoint[] capillarsCenters = new CartesianPoint[capillarsAmount];
            CartesianPoint coordinate;

            for (int i = 0; i < capillarsAmount; i++) {
                do {
                    coordinate = coordinateFactory.getCoordinate();
                } while (!isCapillarCoordinateValid(capillarsCenters, coordinate));

                capillarsCenters[i] = coordinate;
                capillars.add(capillarFactory.getNewCapillar(coordinate, new SphericalPoint(1_00, 0.0, Math.toRadians(1.1))));

                if (i % (capillarsAmount / 10) == 0.0) System.out.println("    ... " + (i * 100 / capillarsAmount) + "% capillars created");
            }
        }

        long finish = System.nanoTime();
        System.out.println("Creating capillars fifnish. Total time = = " + (finish - start) / 1_000_000 + " ms");
        System.out.println();
    }

//    @Override
//    protected void createCapillars() throws IllegalArgumentException {
//        long start = System.nanoTime();
//
//        double domainsAmount = capillarsAmount / CAPILLARS_PER_DOMAIN_AMOUNT;
//        int domainsAmountPerLine = (int) Math.sqrt(domainsAmount);
//
//        double domainSquare = CAPILLARS_PER_DOMAIN_AMOUNT / capillarsDensity;
//        if (domainSquare < 4 * capillarRadius * capillarRadius * CAPILLARS_PER_DOMAIN_AMOUNT) {
//            throw new IllegalArgumentException();
//        }
//        double domainSideLength = Math.sqrt(domainSquare);
//
//        sideLength = domainsAmountPerLine * domainSideLength;
//
//        double initialX = center.getX();
//        double initialY = center.getY() - sideLength / 2;
//        double initialZ = center.getZ() - sideLength / 2;
//
//        CartesianPoint coordinate;
//        CartesianPoint[] capillarsCenters;
//        CoordinateFactory coordinateFactory;
//
//        for (int y = 0; y < domainsAmountPerLine; y++) {
//            for (int z = 0; z < domainsAmountPerLine; z++) {
//                capillarsCenters = new CartesianPoint[CAPILLARS_PER_DOMAIN_AMOUNT];
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
    protected CartesianPoint getDetectorsCoordinate() {
        return new CartesianPoint(center.getX() + width, center.getY(), center.getZ());
    }

    @Override
    protected boolean isCapillarCoordinateValid(Point3D[] coordinates, Point3D coordinate) {
        int i = 0;
        while (coordinates[i] != null && i < coordinates.length) {
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