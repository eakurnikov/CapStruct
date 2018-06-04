package com.private_void.core.entities.particles;

import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.rotation_matrix.RotationMatrix;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;

public abstract class Particle {
    protected CartesianPoint coordinate;
    protected Vector speed;
    protected double trace;

    protected boolean absorbed;
    protected boolean channeled;
    protected boolean deleted;

    protected Particle(final CartesianPoint coordinate, final Vector speed) {
        this.coordinate = coordinate;
        this.speed = speed;
        this.trace = 0.0;

        this.absorbed = false;
        this.channeled = false;
        this.deleted = false;
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

    public Particle setSpeed(final Vector speed) {
        this.speed = speed;
        return this;
    }

    public Particle rotateSpeed(final Vector vector, double angle) {
        speed = speed.rotateAroundVector(vector, angle);
        return this;
    }

    public boolean isAbsorbed() {
        return absorbed;
    }

    public void absorb() {
        this.absorbed = true;
    }

    public boolean isChanneled() {
        return channeled;
    }

    public void setChanneled() {
        this.channeled = true;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void delete() {
        deleted = true;
    }

    public double getTrace() {
        return trace;
    }

    // Возвращает проекцию координаты на плоскость, расположенную под углом angle к абсолютной
    public CartesianPoint rotateCoordinateAroundOY(double angle) {
        return RotationMatrix.aroundOY(angle).rotate(coordinate);
    }

    private void increaseTrace(final CartesianPoint point) {
        trace = Math.sqrt((point.getX() - coordinate.getX()) * (point.getX() - coordinate.getX())
                + (point.getY() - coordinate.getY()) * (point.getY() - coordinate.getY())
                + (point.getZ() - coordinate.getZ()) * (point.getZ() - coordinate.getZ()));
    }

    public interface Factory {
        Particle getNewParticle(final CartesianPoint coordinate, final Vector speed);
    }
}

//    @Override
//    public void toInnerRefFrame(Particle particle) {
//        particle
//                .shiftCoordinate(-front.getX(), -front.getY(), -front.getX())
//                .rotateRefFrameAroundOY(position.getTheta())
//                .rotateRefFrameAroundOZ(-position.getPhi());
//    }
//
//    @Override
//    public void toGlobalRefFrame(Particle particle) {
//        particle
//                .rotateRefFrameAroundOZ(position.getPhi())
//                .rotateRefFrameAroundOY(-position.getTheta())
//                .shiftCoordinate(front.getX(), front.getY(), front.getX());
//    }