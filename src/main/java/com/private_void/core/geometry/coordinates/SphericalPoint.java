package com.private_void.core.geometry.coordinates;

public class SphericalPoint implements Point3D {
    private final double radius;
    private final double theta;
    private final double phi;

    public SphericalPoint(double radius, double theta, double phi) {
        this.radius = Math.abs(radius);
        this.theta = theta;
        this.phi = phi;
    }

    public SphericalPoint(final SphericalPoint point) {
        this.radius = point.radius;
        this.theta = point.theta;
        this.phi = point.phi;
    }

    public CartesianPoint convertToCartesian() {
        double x = radius * Math.sin(theta) * Math.cos(phi);
        double y = radius * Math.sin(theta) * Math.sin(phi);
        double z = radius * Math.cos(theta);

        return new CartesianPoint(x, y, z);
    }

    public SphericalPoint shift(double radius, double theta, double phi) {
        return new SphericalPoint(this.radius + radius, this.theta + theta, this.phi + phi);
    }

    public SphericalPoint shift(final SphericalPoint point) {
        return new SphericalPoint(this.radius + point.radius, this.theta + point.theta, this.phi + point.phi);
    }

    public double getRadius() {
        return radius;
    }

    public double getTheta() {
        return theta;
    }

    public double getPhi() {
        return phi;
    }

    @Override
    public double getQ1() {
        return radius;
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