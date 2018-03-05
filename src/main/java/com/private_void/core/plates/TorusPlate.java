package com.private_void.core.plates;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.surfaces.CapillarFactory;

public class TorusPlate extends FlatPlate {
    private float angleR;

    public TorusPlate(final CapillarFactory capillarFactory, final Point3D center, int capillarsAmount,
                      float capillarsDensity, float angleR) {
        super(capillarFactory, center, capillarsAmount, capillarsDensity);
        this.angleR = angleR;
    }
}