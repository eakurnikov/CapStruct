package com.private_void.core.particles;

import com.private_void.core.geometry.Point3D;

public interface AtomFactory {
    Atom getNewAtom(final Point3D coordinate, float charge);
}
