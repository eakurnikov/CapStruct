package com.private_void.core.plates;

import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.CoordinateFactory;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.CapillarFactory;

import java.util.ArrayList;
import java.util.List;

public class FlatPlate extends Plate {
    public FlatPlate(final CapillarFactory capillarFactory, final CoordinateFactory coordinateFactory,
                     final Point3D center, float length, float height, float capillarsDensity) {
        super(capillarFactory, coordinateFactory, center, length, height, capillarsDensity);
        this.detector = new Detector(getDetectorsCoordinate(), length);
        createCapillars();
    }

    @Override
    protected void createCapillars() {
        List<Capillar> newCapillars = new ArrayList<>();
        float frontSquare = getFrontSquare();
        float capillarsAmount = capillarsDensity * frontSquare;

        for (int i = 0; i < capillarsAmount; i++) {
            Point3D coordinate = coordinateFactory.getCoordinate();

//            while (coordinate удовлетворяет условию) {
//                разыгрываем новую
//            }

            newCapillars.add(capillarFactory.getNewCapillar(coordinate));
        }

        capillars = newCapillars;
    }

    @Override
    protected Point3D getDetectorsCoordinate() {
        return new Point3D(center.getX() + width, center.getY(), center.getZ());
    }

    @Override
    protected float getFrontSquare() {
        return length * height;
    }
}
