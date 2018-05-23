package com.private_void.core.plates;

import com.private_void.app.MessagePool;
import com.private_void.app.ProgressProvider;
import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.coordinates.Point3D;
import com.private_void.core.surfaces.capillar_factories.CapillarFactory;

import java.io.FileWriter;
import java.io.IOException;

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
        ProgressProvider.getInstance().setProgress(MessagePool.creatingCapillarsStart());

        double frontSquare = sideLength * sideLength;
        double minCapillarSquare = (2.0 * capillarRadius) * (2.0 * capillarRadius);
        double maxCapillarDensity = 1.0 / minCapillarSquare;

        if (capillarsDensity > 0.67 * maxCapillarDensity) {
            double capillarsCellSideLength;

            if (capillarsDensity >= maxCapillarDensity) {
                ProgressProvider.getInstance().setProgress(MessagePool.capillarsDensityTooBig(maxCapillarDensity));
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

            for (int i = 0; i < pool + 1; i++) {
                for (int j = 0; j < pool + 1; j++) {

                    CartesianPoint cellCenter = initialPoint.shift(
                            0.0, i * capillarsCellSideLength, j * capillarsCellSideLength);

                    if ((cellCenter.getY() - center.getY())
                            * (cellCenter.getY() - center.getY())
                            + (cellCenter.getZ() - center.getZ())
                            * (cellCenter.getZ() - center.getZ())
                            < (plateRadius - capillarRadius) * (plateRadius - capillarRadius)) {

                        CartesianPoint capillarsFrontCoordinate;
                        if (capillarsDensity >= maxCapillarDensity) {
                            capillarsFrontCoordinate = cellCenter;
                        } else {
                            capillarsFrontCoordinate = generator().getXFlatUniformDistribution(cellCenter,
                                    capillarsCellSideLength / 2.0 - capillarRadius,
                                    capillarsCellSideLength / 2.0 - capillarRadius)
                                    .getCoordinate();
                        }

                        capillars.add(capillarFactory.getNewCapillar(capillarsFrontCoordinate));

                        if (++capillarsCounter % (capillarsAmount / 10) == 0.0) {
                            ProgressProvider.getInstance().setProgress(i * 100 / capillarsAmount);
                        }
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
                    ProgressProvider.getInstance().setProgress(i * 100 / capillarsAmount);
                }
            }
        }

        ProgressProvider.getInstance().setProgress(MessagePool.creatingCapillarsFinish());
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
//        double initialZ = center.getX() - sideLength / 2;
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

    private void writeDataInFile() {
        try (FileWriter writer = new FileWriter("capillar_plate_info.txt")) {
            writer.write("Flat plate\n");
            writer.write("Front center coordinate = ("
                    + center.getX() + ", " + center.getY() + ", " + center.getZ() + ")\n");
            writer.write("Plate's side length = " + this.sideLength + "\n");
            writer.write("Plate's width = " + this.width + "\n");
            writer.write("Capillars amount = " + this.capillarsAmount + "\n");
            writer.write("Capillars radius = " + this.capillarRadius + "\n");
            writer.write("Capillars density = " + this.capillarsDensity + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}