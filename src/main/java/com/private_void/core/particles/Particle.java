package com.private_void.core.particles;

import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.coordinates.ReferenceFrame;
import com.private_void.core.geometry.rotation_matrix.RotationMatrix;
import com.private_void.core.geometry.vectors.Vector;

public abstract class Particle {
    protected CartesianPoint coordinate;
    protected Vector speed;
    protected ReferenceFrame refFrame;
    protected double trace;
    protected boolean absorbed;
    protected boolean out;

    protected Particle(final CartesianPoint coordinate, final Vector speed) {
        this.coordinate = coordinate;
        this.speed = speed;
        this.refFrame = ReferenceFrame.GLOBAL;
        this.trace = 0.0;
        this.absorbed = false;
        this.out = false;
    }

//    @Override
//    public void toInnerRefFrame(Particle particle) {
//        particle
//                .shiftCoordinate(-front.getX(), -front.getY(), -front.getZ())
//                .rotateRefFrameAroundOY(position.getTheta())
//                .rotateRefFrameAroundOZ(-position.getPhi());
//    }
//
//    @Override
//    public void toGlobalRefFrame(Particle particle) {
//        particle
//                .rotateRefFrameAroundOZ(position.getPhi())
//                .rotateRefFrameAroundOY(-position.getTheta())
//                .shiftCoordinate(front.getX(), front.getY(), front.getZ());
//    }

    public Particle toReferenceFrame(final ReferenceFrame refFrame) {
        //todo override equals and hashcode for ref frame2
        if (this.refFrame == ReferenceFrame.GLOBAL && refFrame == ReferenceFrame.GLOBAL) {
            return this;
        }

        if (refFrame == ReferenceFrame.GLOBAL) {
            RotationMatrix matrixZ = RotationMatrix.aroundOZ(-this.refFrame.getAngleWithOZ());
            coordinate = matrixZ.rotate(coordinate);
            speed = matrixZ.rotate(speed);

            RotationMatrix matrixY = RotationMatrix.aroundOY(-this.refFrame.getAngleWithOY());
            coordinate = matrixY.rotate(coordinate);
            speed = matrixY.rotate(speed);

            RotationMatrix matrixX = RotationMatrix.aroundOX(-this.refFrame.getAngleWithOX());
            coordinate = matrixX.rotate(coordinate);
            speed = matrixX.rotate(speed);

            coordinate = coordinate.shift(-this.refFrame.getShiftX(), -this.refFrame.getShiftY(), -this.refFrame.getShiftZ());
        } else {
            coordinate = coordinate.shift(refFrame.getShiftX(), refFrame.getShiftY(), refFrame.getShiftZ());

            RotationMatrix matrixX = RotationMatrix.aroundOX(refFrame.getAngleWithOX());
            coordinate = matrixX.rotate(coordinate);
            speed = matrixX.rotate(speed);

            RotationMatrix matrixY = RotationMatrix.aroundOY(refFrame.getAngleWithOY());
            coordinate = matrixY.rotate(coordinate);
            speed = matrixY.rotate(speed);

            RotationMatrix matrixZ = RotationMatrix.aroundOZ(refFrame.getAngleWithOZ());
            coordinate = matrixZ.rotate(coordinate);
            speed = matrixZ.rotate(speed);
        }

        this.refFrame = refFrame;

        return this;
    }

    public CartesianPoint getCoordinate() {
        return coordinate;
    }

    public Particle setCoordinate(final CartesianPoint newCoordinate) {
        increaseTrace(newCoordinate);
        this.coordinate = newCoordinate;
        return this;
    }

    public Vector getSpeed() {
        return speed;
    }

    public void setSpeed(final Vector speed) {
        this.speed = speed;
    }

    public Particle rotateSpeed(final Vector vector, double angle) {
        speed = speed.rotateAroundVector(vector, angle);
        return this;
    }

    public boolean isAbsorbed() {
        return absorbed;
    }

    public void setAbsorbed(boolean absorbed) {
        this.absorbed = absorbed;
    }

    public boolean isOut() {
        return out;
    }

    public void setOut(boolean out) {
        this.out = out;
    }

    public double getTrace() {
        return trace;
    }

    public void increaseTrace(final CartesianPoint point) {
        trace = Math.sqrt((point.getX() - coordinate.getX()) * (point.getX() - coordinate.getX())
                + (point.getY() - coordinate.getY()) * (point.getY() - coordinate.getY())
                + (point.getZ() - coordinate.getZ()) * (point.getZ() - coordinate.getZ()));
    }

    public interface Factory {
        Particle getNewParticle(final CartesianPoint coordinate, final Vector speed);
    }

    // Возвращает проекцию координаты на плоскость, расположенную под углом angle к абсолютной
    public CartesianPoint rotateCoordinateAroundOY(double angle) {
        return RotationMatrix.aroundOY(angle).rotate(coordinate);
    }
}
