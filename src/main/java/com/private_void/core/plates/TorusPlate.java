package com.private_void.core.plates;

import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.surfaces.Capillar;

public class TorusPlate extends FlatPlate {
    private final double maxAngleR;

    public TorusPlate(final Capillar.Factory capillarFactory, final CartesianPoint center, double capillarsDensity,
                      double sideLength, double maxAngleR) {
        super(capillarFactory, center, capillarsDensity, sideLength);
        this.maxAngleR = maxAngleR;
    }
}