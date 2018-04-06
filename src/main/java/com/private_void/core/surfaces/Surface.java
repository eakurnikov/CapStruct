package com.private_void.core.surfaces;

import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.vectors.Vector;

public abstract class Surface {
    protected final CartesianPoint front;

    protected Surface(final CartesianPoint front) {
        this.front = front;
    }

    public CartesianPoint getFront() {
        return front;
    }

    protected abstract Vector getNormal(final CartesianPoint point);

    protected abstract Vector getParticleSpeedRotationAxis(final CartesianPoint point, final Vector normal);
}