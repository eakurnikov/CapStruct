package com.private_void.core.geometry;

public class SphericalPoint {
    private float radius;
    private float theta;
    private float phi;

    public SphericalPoint(float radius, float theta, float phi) {
        this.radius = radius;
        this.theta = theta;
        this.phi = phi;
    }

    public Point3D convertToCartesian() {
        float x = (float) (radius * Math.sin(theta) * Math.cos(phi));
        float y = (float) (radius * Math.sin(theta) * Math.sin(phi));
        float z = (float) (radius * Math.cos(theta));

        return new Point3D(x, y, z);
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
}