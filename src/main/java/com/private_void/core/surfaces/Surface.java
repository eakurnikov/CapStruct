package com.private_void.core.surfaces;

import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.Vector;

public abstract class Surface {
    protected CartesianPoint front;
    protected Vector normal;
    protected Vector axis;

    protected Surface(final CartesianPoint front) {
        this.front = front;
        this.normal = new Vector(0.0f, 1.0f, 0.0f);
        this.axis = new Vector(1.0f, 0.0f, 0.0f);
    }

    protected abstract Vector getNormal(final CartesianPoint point);

    protected abstract Vector getAxis(final CartesianPoint point);
}
