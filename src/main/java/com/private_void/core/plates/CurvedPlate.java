package com.private_void.core.plates;

import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.CoordinateFactory;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.surfaces.CapillarFactory;

public class CurvedPlate extends Plate {
    private float angleR;
    private float curvRadius;

    public CurvedPlate(final CapillarFactory capillarFactory, final Point3D center, int capillarsAmount,
                       float capillarsDensity, float angleR, float curvRadius) {
        super(capillarFactory, center, capillarsAmount, capillarsDensity);
        this.angleR = angleR;
        this.curvRadius = curvRadius;
        this.detector = new Detector(getDetectorsCoordinate(),curvRadius * (float) Math.sin(angleR));
        createCapillars();
    }

    @Override
    protected void createCapillars() {
        long start = System.nanoTime();

        float frontSquare = capillarsAmount / capillarsDensity;
        float minFrontSquare = capillarsAmount * (2 * capillarRadius) * (2 * capillarRadius);

        long finish = System.nanoTime();
        System.out.println();
        System.out.println("Creating capillars time = " + (finish - start) / 1_000_000 + " ms");
    }

    @Override
    protected Point3D getDetectorsCoordinate() {
        return new Point3D(center.getX() + curvRadius, center.getY(), center.getZ());
    }

    @Override
    protected boolean isCapillarCoordinateValid(Point3D[] coordinates, Point3D coordinate) {
        return false;
    }
}