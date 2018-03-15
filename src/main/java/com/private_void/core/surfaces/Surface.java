package com.private_void.core.surfaces;

import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.Vector;

public abstract class Surface {
    protected CartesianPoint front;
    protected Vector normal;
    protected Vector axis;

    protected Surface(final CartesianPoint front) {
        this.front = front;
        this.normal = new Vector(0.0, 1.0, 0.0);
        this.axis = new Vector(1.0, 0.0, 0.0);
    }

    public CartesianPoint getFront() {
        return front;
    }

    protected abstract Vector getNormal(final CartesianPoint point);

    protected abstract Vector getAxis(final CartesianPoint point);
}
