package com.private_void.core.surfaces;

import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.SphericalPoint;

public interface CapillarFactory {

    Capillar getNewCapillar(final CartesianPoint coordinate, final SphericalPoint position);

    float getRadius();

    float getLength();
}
