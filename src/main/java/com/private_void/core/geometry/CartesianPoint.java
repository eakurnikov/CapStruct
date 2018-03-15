package com.private_void.core.geometry;

import static com.private_void.utils.Constants.POINT_AMBIT;

public class CartesianPoint implements Point3D {
    protected double x = 0.0;
    protected double y = 0.0;
    protected double z = 0.0;

    public CartesianPoint(double x, double y, double z) {
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
        double radius = Math.sqrt(x * x + y * y + z * z);
        double theta =  Math.atan2(Math.sqrt(x * x + z * z), y);
        double phi = Math.atan2(z, x);

        return new SphericalPoint(radius, theta, phi);
    }

    public CartesianPoint shift(double x, double y, double z) {
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

    public CartesianPoint getNewByShift(double x, double y, double z) {
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

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public double getQ1() {
        return x;
    }

    @Override
    public double getQ2() {
        return y;
    }

    @Override
    public double getQ3() {
        return z;
    }
}