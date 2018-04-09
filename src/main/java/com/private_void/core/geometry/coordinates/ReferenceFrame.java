package com.private_void.core.geometry.coordinates;

public class ReferenceFrame {
    public static final ReferenceFrame GLOBAL = new ReferenceFrame(
            0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

    private final double shiftX;
    private final double shiftY;
    private final double shiftZ;

    private final double angleAroundOX;
    private final double angleAroundOY;
    private final double angleAroundOZ;

    private ReferenceFrame(double shiftX, double shiftY, double shiftZ,
                           double angleAroundOX, double angleAroundOY, double angleAroundOZ) {
        this.shiftX = shiftX;
        this.shiftY = shiftY;
        this.shiftZ = shiftZ;
        this.angleAroundOX = angleAroundOX;
        this.angleAroundOY = angleAroundOY;
        this.angleAroundOZ = angleAroundOZ;
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

    public double getAngleAroundOX() {
        return angleAroundOX;
    }

    public double getAngleAroundOY() {
        return angleAroundOY;
    }

    public double getAngleAroundOZ() {
        return angleAroundOZ;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private double shiftX;
        private double shiftY;
        private double shiftZ;

        private double angleAroundOX;
        private double angleAroundOY;
        private double angleAroundOZ;

        private Builder() {
            this.shiftX = 0.0;
            this.shiftY = 0.0;
            this.shiftZ = 0.0;

            this.angleAroundOX = 0.0;
            this.angleAroundOY = 0.0;
            this.angleAroundOZ = 0.0;
        }

        public Builder atPoint(final CartesianPoint point) {
            this.shiftX = -point.getX();
            this.shiftY = -point.getY();
            this.shiftZ = -point.getZ();
            return this;
        }

        public Builder setShiftX(double shiftX) {
            this.shiftX = shiftX;
            return this;
        }

        public Builder setShiftY(double shiftY) {
            this.shiftY = shiftY;
            return this;
        }

        public Builder setShiftZ(double shiftZ) {
            this.shiftZ = shiftZ;
            return this;
        }

        public Builder setAngleAroundOX(double angleAroundOX) {
            this.angleAroundOX = angleAroundOX;
            return this;
        }

        public Builder setAngleAroundOY(double angleAroundOY) {
            this.angleAroundOY = angleAroundOY;
            return this;
        }

        public Builder setAngleAroundOZ(double angleAroundOZ) {
            this.angleAroundOZ = angleAroundOZ;
            return this;
        }

        public ReferenceFrame build() {
            return new ReferenceFrame(shiftX, shiftY, shiftZ, angleAroundOX, angleAroundOY, angleAroundOZ);
        }
    }
}