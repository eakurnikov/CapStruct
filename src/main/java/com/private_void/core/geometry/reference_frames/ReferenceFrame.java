package com.private_void.core.geometry.reference_frames;

import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.rotation_matrix.RotationMatrix;
import com.private_void.core.geometry.vectors.Vector;
import com.private_void.core.particles.Particle;

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

    public static class Converter {
        private final double shiftX;
        private final double shiftY;
        private final double shiftZ;

        private final RotationMatrix matrixX;
        private final RotationMatrix matrixY;
        private final RotationMatrix matrixZ;

        private final RotationMatrix matrixXBack;
        private final RotationMatrix matrixYBack;
        private final RotationMatrix matrixZBack;

        public Converter(final ReferenceFrame to) {
            this.shiftX = to.shiftX;
            this.shiftY = to.shiftY;
            this.shiftZ = to.shiftZ;

            this.matrixX = RotationMatrix.aroundOX(to.angleAroundOX);
            this.matrixY = RotationMatrix.aroundOY(to.angleAroundOY);
            this.matrixZ = RotationMatrix.aroundOZ(to.angleAroundOZ);

            this.matrixXBack = RotationMatrix.aroundOX(-to.angleAroundOX);
            this.matrixYBack = RotationMatrix.aroundOY(-to.angleAroundOY);
            this.matrixZBack = RotationMatrix.aroundOZ(-to.angleAroundOZ);
        }

        public Converter(final ReferenceFrame from, final ReferenceFrame to) {
            this.shiftX = to.shiftX - from.shiftX;
            this.shiftY = to.shiftY - from.shiftY;
            this.shiftZ = to.shiftZ - from.shiftZ;

            this.matrixX = RotationMatrix.aroundOX(to.angleAroundOX - from.angleAroundOX);
            this.matrixY = RotationMatrix.aroundOY(to.angleAroundOY - from.angleAroundOY);
            this.matrixZ = RotationMatrix.aroundOZ(to.angleAroundOZ - from.angleAroundOZ);

            this.matrixXBack = RotationMatrix.aroundOX(-to.angleAroundOX + from.angleAroundOX);
            this.matrixYBack = RotationMatrix.aroundOY(-to.angleAroundOY + from.angleAroundOY);
            this.matrixZBack = RotationMatrix.aroundOZ(-to.angleAroundOZ + from.angleAroundOZ);
        }

        public CartesianPoint convert(final CartesianPoint point) {
            return matrixZ.rotate(matrixY.rotate(matrixX.rotate(point.shift(shiftX, shiftY, shiftZ))));
        }

        public CartesianPoint convertBack(final CartesianPoint point) {
            return matrixXBack.rotate(matrixYBack.rotate(matrixZBack.rotate(point))).shift(-shiftX, -shiftY, -shiftZ);
        }

        public Vector convert(final Vector vector) {
            return matrixZ.rotate(matrixY.rotate(matrixX.rotate(vector)));
        }

        public Vector convertBack(final Vector vector) {
            return matrixXBack.rotate(matrixYBack.rotate(matrixZBack.rotate(vector)));
        }

        public void convert(final Particle particle) {
            particle
                    .setSpeed(convert(particle.getSpeed()))
                    .setCoordinate(convert(particle.getCoordinate()));
        }

        public void convertBack(final Particle particle) {
            particle
                    .setCoordinate(convertBack(particle.getCoordinate()))
                    .setSpeed(convertBack(particle.getSpeed()));
        }
    }
}