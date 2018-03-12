package com.private_void.core.surfaces;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.SphericalPoint;

public interface CapillarFactory {

    Capillar getNewCapillar(final Point3D coordinate, final SphericalPoint position);

    float getRadius();

    float getLength();
}
