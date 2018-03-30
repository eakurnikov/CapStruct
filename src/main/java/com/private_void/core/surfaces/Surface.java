package com.private_void.core.surfaces;

import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.Vector;

public abstract class Surface {
    protected CartesianPoint front;

    protected Surface(final CartesianPoint front) {
        this.front = front;
    }

    public CartesianPoint getFront() {
        return front;
    }

    protected abstract Vector getNormal(final CartesianPoint point);

    protected abstract Vector getParticleSpeedRotationAxis(final CartesianPoint point, final Vector normal);
}