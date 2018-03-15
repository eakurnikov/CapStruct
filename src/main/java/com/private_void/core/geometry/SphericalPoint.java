package com.private_void.core.geometry;

//todo ограничения на углы нужно навесить, проверку делать при изменении
public class SphericalPoint implements Point3D {
    private double radius;
    private double theta;
    private double phi;

    public SphericalPoint(double radius, double theta, double phi) {
        this.radius = radius;
        this.theta = theta;
        this.phi = phi;
    }

    public CartesianPoint convertToCartesian() {
        double x = radius * Math.sin(theta) * Math.cos(phi);
        double y = radius * Math.cos(theta);
        double z = radius * Math.sin(theta) * Math.sin(phi);

        return new CartesianPoint(x, y, z);
    }

    public SphericalPoint shift(double radius, double theta, double phi) {
        this.radius += radius;
        this.theta += theta;
        this.phi += phi;

        return this;
    }

    public SphericalPoint shift(final SphericalPoint point) {
        this.radius += point.getRadius();
        this.theta += point.getTheta();
        this.phi += point.getPhi();

        return this;
    }

    public SphericalPoint getNewByShift(double radius, double theta, double phi) {
        SphericalPoint newPoint = new SphericalPoint(this.radius, this.theta, this.phi);

        newPoint.radius += radius;
        newPoint.theta += theta;
        newPoint.phi += phi;

        return newPoint;
    }

    public SphericalPoint getNewByShift(final SphericalPoint point) {
        SphericalPoint newPoint = new SphericalPoint(point.radius, point.theta, point.phi);

        newPoint.radius += point.getRadius();
        newPoint.theta += point.getTheta();
        newPoint.phi += point.getPhi();

        return newPoint;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public double getPhi() {
        return phi;
    }

    public void setPhi(double phi) {
        this.phi = phi;
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
}