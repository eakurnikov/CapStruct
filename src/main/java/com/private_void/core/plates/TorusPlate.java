package com.private_void.core.plates;

import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.surfaces.CapillarFactory;

public class TorusPlate extends FlatPlate {
    private double maxAngleR;

    public TorusPlate(final CapillarFactory capillarFactory, final CartesianPoint center, double capillarsDensity,
                      double sideLength, double maxAngleR) {
        super(capillarFactory, center, capillarsDensity, sideLength);
        this.maxAngleR = maxAngleR;
    }
}