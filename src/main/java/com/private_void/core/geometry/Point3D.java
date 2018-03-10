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

    public boolean isNear(final Point3D point3D) {
        return  (x - point3D.getX()) * (x - point3D.getX()) +
                (y - point3D.getY()) * (y - point3D.getY()) +
                (z - point3D.getZ()) * (z - point3D.getZ()) < POINT_AMBIT * POINT_AMBIT;
    }

    public SphericalPoint convertToSperical() {
        float radius = (float) Math.sqrt(x * x + y * y + z * z);
        float theta = (float) Math.atan(Math.sqrt(x * x + z * z) / y);
        float phi = (float) Math.atan(z / x);

        return new SphericalPoint(radius, theta, phi);
    }

    public Point3D shift(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;

        return this;
    }

    public Point3D shift(final Point3D point) {
        this.x += point.getX();
        this.y += point.getY();
        this.z += point.getZ();

        return this;
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