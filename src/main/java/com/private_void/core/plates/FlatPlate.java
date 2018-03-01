package com.private_void.core.plates;

import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.CoordinateFactory;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.CapillarFactory;

import java.util.ArrayList;
import java.util.List;

import static com.private_void.utils.Generator.generator;

public class FlatPlate extends Plate {
    public FlatPlate(final CapillarFactory capillarFactory, final Point3D center, int capillarsAmount,
                     float capillarsDensity) {
        super(capillarFactory, center, capillarsAmount, capillarsDensity);
        createCapillars();
        this.detector = new Detector(getDetectorsCoordinate(), sideLength * 1.0f);
    }

    @Override
    protected void createCapillars() throws IllegalArgumentException {
        float domainsAmount = capillarsAmount / CAPILLARS_PER_DOMAIN_AMOUNT;
        int domainsAmountPerLine = (int) Math.sqrt(domainsAmount);

        float domainSquare = CAPILLARS_PER_DOMAIN_AMOUNT / capillarsDensity;
        if (domainSquare < 4 * capillarRadius * capillarRadius * CAPILLARS_PER_DOMAIN_AMOUNT) {
            throw new IllegalArgumentException();
        }
        float domainSideLength = (float) Math.sqrt(domainSquare);

        sideLength = domainsAmountPerLine * domainSideLength;

        float initialX = center.getX();
        float initialY = center.getY() - sideLength / 2;
        float initialZ = center.getZ() - sideLength / 2;

        Point3D coordinate;
        Point3D[] capillarsCenters;
        CoordinateFactory coordinateFactory;

// WITHOUT DOMAINS ----------------------------------------------------
//        coordinateFactory = generator().getXPlanarUniformDistributionFactory(initialX,
//                center.getY() - sideLength / 2 + capillarRadius,
//                center.getY() + sideLength / 2 - capillarRadius,
//
//                center.getZ() - sideLength / 2 + capillarRadius + domainSideLength,
//                center.getZ() + sideLength / 2 - capillarRadius + domainSideLength);
//
//        for (int i = 0; i < capillarsAmount; i++) {
//            capillarsCenters = new Point3D[(int) capillarsAmount];
//
//            do {
//                coordinate = coordinateFactory.getCoordinate();
//            } while (!isCapillarCoordinateValid(capillarsCenters, coordinate));
//
//            capillarsCenters[i] = coordinate;
//            capillars.add(capillarFactory.getNewCapillar(coordinate));
//
// ---------------------------------------------------------------------

        for (int y = 0; y < domainsAmountPerLine; y++) {
            for (int z = 0; z < domainsAmountPerLine; z++) {
                capillarsCenters = new Point3D[CAPILLARS_PER_DOMAIN_AMOUNT];

                coordinateFactory = generator().getXPlanarUniformDistributionFactory(initialX,
                        initialY + capillarRadius + domainSideLength *  y,
                        initialY - capillarRadius + domainSideLength * (y + 1),

                        initialZ + capillarRadius + domainSideLength *  z,
                        initialZ - capillarRadius + domainSideLength * (z + 1));

                for (int i = 0; i < CAPILLARS_PER_DOMAIN_AMOUNT; i++) {
                    do {
                        coordinate = coordinateFactory.getCoordinate();
                    } while (!isCapillarCoordinateValid(capillarsCenters, coordinate));

                    capillarsCenters[i] = coordinate;
                    capillars.add(capillarFactory.getNewCapillar(capillarsCenters[i]));
                }
            }
        }
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