package com.private_void.core.geometry.coordinates;

public class CylindricalPoint implements Point3D {
    private final double r;
    private final double phi;
    private final double z;

    public CylindricalPoint(double r, double phi, double z) {
        this.r = r;
        this.phi = phi;
        this.z = z;
    }

    public CartesianPoint convertToCartesian() {
        double x = r * Math.cos(phi);
        double y = r * Math.sin(phi);

        return new CartesianPoint(x, y, z);
    }

    public double getR() {
        return r;
    }

    public double getPhi() {
        return phi;
    }

    public double getZ() {
        return z;
    }

    @Override
    public double getQ1() {
        return r;
    }

    @Override
    public double getQ2() {
        return phi;
    }

    @Override
    public double getQ3() {
        return z;
    }
}