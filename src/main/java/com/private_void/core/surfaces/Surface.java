package com.private_void.core.surfaces;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;

public abstract class Surface {
    protected Point3D front;
    protected Vector3D normal;
    protected Vector3D axis;

    protected Surface(final Point3D front) {
        this.front = front;
        this.normal = new Vector3D(0.0f, 1.0f, 0.0f);
        this.axis = new Vector3D(1.0f, 0.0f, 0.0f);
    }

    protected abstract Vector3D getNormal(final Point3D point);

    protected abstract Vector3D getAxis(final Point3D point);
}
