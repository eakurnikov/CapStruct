package com.private_void.core.geometry;

import static com.private_void.utils.Constants.POINT_AMBIT;

public class Point3D {
    protected float x = 0.0f;
    protected float y = 0.0f;
    protected float z = 0.0f;

    public Point3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D(final Point3D point3D) {
        this.x = point3D.getX();
        this.y = point3D.getY();
        this.z = point3D.getZ();
    }

    public boolean isNear(final Point3D point3D) {
        return  (x - point3D.getX()) * (x - point3D.getX()) +
                (y - point3D.getY()) * (y - point3D.getY()) +
                (z - point3D.getZ()) * (z - point3D.getZ()) < POINT_AMBIT * POINT_AMBIT;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}