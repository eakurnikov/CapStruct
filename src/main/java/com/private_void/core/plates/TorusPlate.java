package com.private_void.core.plates;

import com.private_void.core.geometry.CoordinateFactory;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.surfaces.CapillarFactory;

public class TorusPlate extends Plate {

    public TorusPlate(final CapillarFactory capillarFactory, final CoordinateFactory coordinateFactory,
                      final Point3D center, float length, float height, float capillarsDensity) {
        super(capillarFactory, coordinateFactory, center, length, height, capillarsDensity);
    }

    @Override
    protected void createCapillars() {
        //TODO капилляры должны быть немного другой формы - длина фиксирована, и угол фиксированы. края равной длины
    }

    @Override
    protected Point3D getDetectorsCoordinate() {
        return null;
    }

    @Override
    protected float getFrontSquare() {
        return 0;
    }
}
