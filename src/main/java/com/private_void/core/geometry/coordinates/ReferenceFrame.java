package com.private_void.core.geometry.coordinates;

public class ReferenceFrame {
    public static final ReferenceFrame GLOBAL = new ReferenceFrame(CartesianPoint.ORIGIN);

    private final double shiftX;
    private final double shiftY;
    private final double shiftZ;

    private final double angleWithOX;
    private final double angleWithOY;
    private final double angleWithOZ;

    public ReferenceFrame(final CartesianPoint cartesianPoint) {
        this.shiftX = -cartesianPoint.getX();
        this.shiftY = -cartesianPoint.getY();
        this.shiftZ = -cartesianPoint.getZ();

        this.angleWithOX = 0.0;
        this.angleWithOY = 0.0;
        this.angleWithOZ = 0.0;
    }

    public ReferenceFrame(final CartesianPoint cartesianPoint, final SphericalPoint sphericalPoint) {
        this.shiftX = -cartesianPoint.getX();
        this.shiftY = -cartesianPoint.getY();
        this.shiftZ = -cartesianPoint.getZ();

        this.angleWithOX = 0.0;
        this.angleWithOY = sphericalPoint.getTheta();
        this.angleWithOZ = -sphericalPoint.getPhi();
    }

    public double getShiftX() {
        return shiftX;
    }

    public double getShiftY() {
        return shiftY;
    }

    public double getShiftZ() {
        return shiftZ;
    }

    public double getAngleWithOX() {
        return angleWithOX;
    }

    public double getAngleWithOY() {
        return angleWithOY;
    }

    public double getAngleWithOZ() {
        return angleWithOZ;
    }
}