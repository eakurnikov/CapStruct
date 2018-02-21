package com.private_void.core.plates;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.surfaces.CapillarFactory;

public class TorusPlate extends Plate {

    public TorusPlate(final CapillarFactory capillarFactory, final Point3D center, float length, float width, float height,
                      float capillarsDensity, float capillarRadius) {
        super(capillarFactory, center, length, width, height, capillarsDensity, capillarRadius);
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
