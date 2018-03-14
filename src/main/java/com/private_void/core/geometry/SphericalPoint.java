package com.private_void.core.geometry;

//todo ограничения на углы нужно навесить, проверку делать при изменении
public class SphericalPoint implements Point3D {
    private float radius;
    private float theta;
    private float phi;

    public SphericalPoint(float radius, float theta, float phi) {
        this.radius = radius;
        this.theta = theta;
        this.phi = phi;
    }

    public CartesianPoint convertToCartesian() {
        float x = (float) (radius * Math.sin(theta) * Math.cos(phi));
        float y = (float) (radius * Math.cos(theta));
        float z = (float) (radius * Math.sin(theta) * Math.sin(phi));

        return new CartesianPoint(x, y, z);
    }

    public SphericalPoint shift(float radius, float theta, float phi) {
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

    public SphericalPoint getNewByShift(float radius, float theta, float phi) {
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

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getTheta() {
        return theta;
    }

    public void setTheta(float theta) {
        this.theta = theta;
    }

    public float getPhi() {
        return phi;
    }

    public void setPhi(float phi) {
        this.phi = phi;
    }

    @Override
    public float getQ1() {
        return radius;
    }

    @Override
    public float getQ2() {
        return theta;
    }

    @Override
    public float getQ3() {
        return phi;
    }
}