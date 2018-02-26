package com.private_void.core.surfaces;

import com.private_void.core.geometry.Point3D;

public interface CapillarFactory {
    Capillar getNewCapillar(final Point3D coordinate);

    float getRadius();

    float getLength();
}
