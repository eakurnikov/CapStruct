package com.private_void.core.geometry.coordinates;

public class Point2D {
    private final double Q1;
    private final double Q2;

    public Point2D(double q1, double q2) {
        Q1 = q1;
        Q2 = q2;
    }

    public Point2D(final Point2D point2D) {
        Q1 = point2D.getQ1();
        Q2 = point2D.getQ2();
    }

    public double getQ1() {
        return Q1;
    }

    public double getQ2() {
        return Q2;
    }
}