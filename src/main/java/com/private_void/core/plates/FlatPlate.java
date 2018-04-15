package com.private_void.core.plates;

import com.private_void.app.Logger;
import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.coordinates.Point3D;
import com.private_void.core.surfaces.capillar_factories.CapillarFactory;

import static com.private_void.utils.Generator.generator;

public class FlatPlate extends Plate {
    private final CapillarFactory capillarFactory;
    private final double sideLength;
    private final double width;

    public FlatPlate(final CapillarFactory capillarFactory, final CartesianPoint center, double capillarsDensity,
                     double sideLength) {
        super(center, capillarFactory.getRadius(), capillarsDensity);
        this.capillarFactory = capillarFactory;
        this.sideLength = sideLength;
        this.width = capillarFactory.getLength();
        this.detector = new Detector(getDetectorsCoordinate(), sideLength);
        createCapillars();
    }

    @Override
    protected void createCapillars() {
        Logger.creatingCapillarsStart();

        double frontSquare = sideLength * sideLength;
        double minCapillarSquare = (2.0 * capillarRadius) * (2.0 * capillarRadius);
        double maxCapillarDensity = 1.0 / minCapillarSquare;

        if (capillarsDensity > 0.67 * maxCapillarDensity) {
            double capillarsCellSideLength;
            if (capillarsDensity >= maxCapillarDensity) {
                Logger.capillarsDensityTooBig(maxCapillarDensity);
                capillarsAmount = (int) (frontSquare / minCapillarSquare);
                capillarsCellSideLength = 2.0 * capillarRadius;
            } else {
                capillarsAmount = (int) (capillarsDensity * frontSquare);
                capillarsCellSideLength = Math.sqrt(frontSquare / capillarsAmount);
            }

            int capillarsCounter = 0;
            int pool = (int) (sideLength / capillarsCellSideLength);
            double plateRadius = sideLength / 2.0;

            CartesianPoint initialPoint = center.shift(0.0, -plateRadius + capillarRadius, -plateRadius + capillarRadius);

            for (int i = 0; i < pool; i++) {
                for (int j = 0; j < pool; j++) {

                    CartesianPoint coordinate = initialPoint.shift(
                            0.0, i * capillarsCellSideLength, j * capillarsCellSideLength);

                    capillars.add(capillarFactory.getNewCapillar(coordinate));

                    if (++capillarsCounter % (capillarsAmount / 10) == 0.0) {
                        Logger.createdCapillarsPercent(i * 100 / capillarsAmount);
                    }
                }
            }
        } else {
            capillarsAmount = (int) (capillarsDensity * frontSquare);

            CartesianPoint.Factory coordinateFactory = generator().getXFlatUniformDistribution(center,
                    sideLength / 2.0 - capillarRadius,
                    sideLength / 2.0 - capillarRadius);

            CartesianPoint[] capillarsCenters = new CartesianPoint[capillarsAmount];
            CartesianPoint coordinate;

            for (int i = 0; i < capillarsAmount; i++) {
                do {
                    coordinate = coordinateFactory.getCoordinate();
                } while (!isCapillarCoordinateValid(capillarsCenters, coordinate));

                capillarsCenters[i] = coordinate;
                capillars.add(capillarFactory.getNewCapillar(coordinate));

                if (i % (capillarsAmount / 10) == 0.0) {
                    Logger.createdCapillarsPercent(i * 100 / capillarsAmount);
                }
            }
        }

        Logger.creatingCapillarsFinish();
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
//        CartesianPoint.Factory coordinateFactory;
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
        return center.shift(width, 0.0, 0.0);
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