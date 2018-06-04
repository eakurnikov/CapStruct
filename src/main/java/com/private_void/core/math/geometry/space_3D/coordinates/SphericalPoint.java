package com.private_void.core.math.geometry.space_3D.coordinates;

public class SphericalPoint implements Point3D {
    private final double r;
    private final double theta;
    private final double phi;

    public SphericalPoint(double radius, double theta, double phi) {
        this.r = Math.abs(radius);
        this.theta = theta;
        this.phi = phi;
    }

    public SphericalPoint(final SphericalPoint point) {
        this.r = point.r;
        this.theta = point.theta;
        this.phi = point.phi;
    }

    public CartesianPoint convertToCartesian() {
        double x = r * Math.sin(theta) * Math.cos(phi);
        double y = r * Math.sin(theta) * Math.sin(phi);
        double z = r * Math.cos(theta);

        return new CartesianPoint(x, y, z);
    }

    public SphericalPoint shift(double r, double theta, double phi) {
        return new SphericalPoint(this.r + r, this.theta + theta, this.phi + phi);
    }

    public SphericalPoint shift(final SphericalPoint point) {
        return new SphericalPoint(this.r + point.r, this.theta + point.theta, this.phi + point.phi);
    }

    public double getR() {
        return r;
    }

    public double getTheta() {
        return theta;
    }

    public double getPhi() {
        return phi;
    }

    @Override
    public double getQ1() {
        return r;
    }

    @Override
    public double getQ2() {
        return theta;
    }

    @Override
    public double getQ3() {
        return phi;
    }

    public interface Factory {
        SphericalPoint getCoordinate();
    }
}