package com.private_void.core.particles;

import com.private_void.core.geometry.CartesianPoint;

public interface AtomFactory {
    Atom getNewAtom(final CartesianPoint coordinate, float charge);
}
