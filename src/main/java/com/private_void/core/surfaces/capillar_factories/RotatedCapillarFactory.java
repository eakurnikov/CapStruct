package com.private_void.core.surfaces.capillar_factories;

import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.coordinates.SphericalPoint;
import com.private_void.core.surfaces.Capillar;

public interface RotatedCapillarFactory {

    Capillar getNewCapillar(final CartesianPoint coordinate, final SphericalPoint position);

    double getRadius();

    double getLength();

}