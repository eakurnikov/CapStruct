package com.private_void.core.geometry;

import static com.private_void.utils.Constants.POINT_AMBIT;

public class CartesianPoint implements Point3D {
    protected float x = 0.0f;
    protected float y = 0.0f;
    protected float z = 0.0f;

    public CartesianPoint(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean isNear(final CartesianPoint point) {
        return  (x - point.getX()) * (x - point.getX()) +
                (y - point.getY()) * (y - point.getY()) +
                (z - point.getZ()) * (z - point.getZ()) < POINT_AMBIT * POINT_AMBIT;
    }

    public SphericalPoint convertToSpherical() {
        float radius = (float) Math.sqrt(x * x + y * y + z * z);
        float theta = (float) Math.atan2(Math.sqrt(x * x + z * z), y);
        float phi = (float) Math.atan2(z, x);

        return new SphericalPoint(radius, theta, phi);
    }

    public CartesianPoint shift(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;

        return this;
    }

    public CartesianPoint shift(final CartesianPoint point) {
        this.x += point.getX();
        this.y += point.getY();
        this.z += point.getZ();

        return this;
    }

    public CartesianPoint getNewByShift(float x, float y, float z) {
        CartesianPoint newPoint = new CartesianPoint(this.x, this.y, this.z);

        newPoint.x += x;
        newPoint.y += y;
        newPoint.z += z;

        return newPoint;
    }

    public CartesianPoint getNewByShift(final CartesianPoint point) {
        CartesianPoint newPoint = new CartesianPoint(point.x, point.y, point.z);

        newPoint.x += point.getX();
        newPoint.y += point.getY();
        newPoint.z += point.getZ();

        return newPoint;
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

    @Override
    public float getQ1() {
        return x;
    }

    @Override
    public float getQ2() {
        return y;
    }

    @Override
    public float getQ3() {
        return z;
    }
}