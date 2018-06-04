package com.private_void.core.math.geometry.space_2D;

public class PolarPoint implements Point2D {
    private final double r;
    private final double phi;

    public PolarPoint(double r, double phi) {
        this.r = r;
        this.phi = phi > 2.0 * Math.PI ? phi - 2.0 * Math.PI : phi;
    }

    public CartesianPoint2D convertToCartesian() {
        double x = r * Math.cos(phi);
        double y = r * Math.sin(phi);

        return new CartesianPoint2D(x, y);
    }

    public PolarPoint shift(double r, double phi) {
        return new PolarPoint(this.r + r, this.phi + phi);
    }

    public PolarPoint shift(final PolarPoint point) {
        return new PolarPoint(this.r + point.r, this.phi + point.phi);
    }

    public double getR() {
        return r;
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
        return phi;
    }

    public interface Factory {
        PolarPoint getCoordinate();
    }
}