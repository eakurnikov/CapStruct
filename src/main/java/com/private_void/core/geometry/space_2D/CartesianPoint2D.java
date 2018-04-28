package com.private_void.core.geometry.space_2D;

public class CartesianPoint2D implements Point2D {
    private final double x;
    private final double y;

    public CartesianPoint2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public CartesianPoint2D(final CartesianPoint2D cartesianPoint2D) {
        x = cartesianPoint2D.getX();
        y = cartesianPoint2D.getY();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public double getQ1() {
        return x;
    }

    @Override
    public double getQ2() {
        return y;
    }
}