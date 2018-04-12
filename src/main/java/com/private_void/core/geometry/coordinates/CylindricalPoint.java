package com.private_void.core.geometry.coordinates;

public class CylindricalPoint implements Point3D {
    private final double r;
    private final double phi;
    private final double z;

    public CylindricalPoint(double r, double phi, double z) {
        this.r = r;
        this.phi = phi > 2.0 * Math.PI ? phi - 2.0 * Math.PI : phi;
        this.z = z;
    }

    // Для получения координат в плоскости YOZ
    public CartesianPoint convertToCartesian() {
        double z = r * Math.cos(phi);
        double y = r * Math.sin(phi);

        return new CartesianPoint(this.z, y, z);
    }

    public CylindricalPoint shift(double r, double phi, double z) {
        return new CylindricalPoint(this.r + r, this.phi + phi, this.z + z);
    }

    public CylindricalPoint shift(final CylindricalPoint point) {
        return new CylindricalPoint(this.r + point.r, this.phi + point.phi, this.z + point.z);
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

    public interface Factory {
        CylindricalPoint getCoordinate();
    }
}