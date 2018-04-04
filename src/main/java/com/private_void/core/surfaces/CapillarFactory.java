package com.private_void.core.surfaces;

import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.coordinates.SphericalPoint;

public interface CapillarFactory {

    Capillar getNewCapillar(final CartesianPoint coordinate, final SphericalPoint position);

    double getRadius();

    double getLength();
}
