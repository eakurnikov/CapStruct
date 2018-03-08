package com.private_void.core.plates;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.surfaces.CapillarFactory;

public class TorusPlate extends FlatPlate {
    private float maxAngleR;

    public TorusPlate(final CapillarFactory capillarFactory, final Point3D center, float capillarsDensity,
                      float sideLength, float maxAngleR) {
        super(capillarFactory, center, capillarsDensity, sideLength);
        this.maxAngleR = maxAngleR;
    }
}