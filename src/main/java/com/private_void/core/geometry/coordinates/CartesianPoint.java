package com.private_void.core.geometry.coordinates;

import com.private_void.core.geometry.rotation_matrix.RotationMatrix;
import com.private_void.core.geometry.vectors.Vector;

import static com.private_void.utils.Constants.POINT_AMBIT;

public class CartesianPoint implements Point3D {
    public static final CartesianPoint ORIGIN = new CartesianPoint(0.0, 0.0, 0.0);

    protected final double x;
    protected final double y;
    protected final double z;

    public CartesianPoint(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CartesianPoint(double[] component) {
        this.x = component[0];
        this.y = component[1];
        this.z = component[2];
    }

    public CartesianPoint(final CartesianPoint point) {
        this.x = point.x;
        this.y = point.y;
        this.z = point.z;
    }

    public boolean isNear(final CartesianPoint point) {
        return  (x - point.x) * (x - point.x) +
                (y - point.y) * (y - point.y) +
                (z - point.z) * (z - point.z) < POINT_AMBIT * POINT_AMBIT;
    }

    public SphericalPoint convertToSpherical() {
        double radius = Math.sqrt(x * x + y * y + z * z);
        double theta =  Math.atan2(Math.sqrt(x * x + y * y), z);
        double phi = Math.atan2(y, x);

        return new SphericalPoint(radius, theta, phi);
    }

    public CartesianPoint shift(double x, double y, double z) {
        return new CartesianPoint(this.x + x, this.y + y, this.z + z);
    }

    public CartesianPoint shift(final CartesianPoint point) {
        return new CartesianPoint(this.x + point.x, this.y + point.y, this.z + point.z);
    }

    public CartesianPoint rotateAroundOX(double angle) {
        return RotationMatrix.aroundOX(angle).rotate(this);
    }

    public CartesianPoint rotateAroundOY(double angle) {
        return RotationMatrix.aroundOY(angle).rotate(this);
    }

    public CartesianPoint rotateAroundOZ(double angle) {
        return RotationMatrix.aroundOZ(angle).rotate(this);
    }

    public CartesianPoint rotateAroundVector(final Vector vector, double angle) {
        return RotationMatrix.aroundVector(vector, angle).rotate(this);
    }

    public CartesianPoint inverse() {
        return new CartesianPoint(-x, -y, -z);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Override
    public double getQ1() {
        return x;
    }

    @Override
    public double getQ2() {
        return y;
    }

    @Override
    public double getQ3() {
        return z;
    }

    public interface Factory {
        CartesianPoint getCoordinate();
    }

//    public static RefFrameConverter getRefFrameConverter(final CartesianPoint point) {
//        return new RefFrameConverter(point);
//    }
//
//    public static RefFrameConverter getRefFrameConverter(final CartesianPoint point, final ReferenceFrame refFrame) {
//        return new RefFrameConverter(point, refFrame);
//    }
//
//    public static class RefFrameConverter extends ReferenceFrame.Converter<CartesianPoint> {
//
//        private RefFrameConverter(final CartesianPoint point) {
//            super(point);
//        }
//
//        private RefFrameConverter(final CartesianPoint point, final ReferenceFrame refFrame) {
//            super(point, refFrame);
//        }
//
//        @Override
//        public CartesianPoint convert(final ReferenceFrame refFrame) {
//            if (this.refFrame == ReferenceFrame.GLOBAL && refFrame == ReferenceFrame.GLOBAL) {
//                return obj;
//            }
//
//            CartesianPoint newPoint;
//
//            if (refFrame == ReferenceFrame.GLOBAL) {
//                newPoint = RotationMatrix.aroundOX(-this.refFrame.getAngleAroundOX()).rotate(
//                        RotationMatrix.aroundOY(-this.refFrame.getAngleAroundOY()).rotate(
//                                RotationMatrix.aroundOZ(-this.refFrame.getAngleAroundOZ()).rotate(
//                                        obj)))
//                        .shift(-this.refFrame.getShiftX(), -this.refFrame.getShiftY(), -this.refFrame.getShiftZ());
//
//            } else {
//                newPoint = RotationMatrix.aroundOZ(refFrame.getAngleAroundOZ()).rotate(
//                        RotationMatrix.aroundOY(refFrame.getAngleAroundOY()).rotate(
//                                RotationMatrix.aroundOX(refFrame.getAngleAroundOX()).rotate(
//                                        obj.shift(refFrame.getShiftX(), refFrame.getShiftY(), refFrame.getShiftZ()))));
//            }
//
//            this.refFrame = refFrame;
//            return newPoint;
//        }
//    }
}