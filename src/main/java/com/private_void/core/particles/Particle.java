package com.private_void.core.particles;

import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.rotation_matrixes.RotationMatrixY;
import com.private_void.core.geometry.vectors.Vector;

public abstract class Particle {
    protected CartesianPoint coordinate;
    protected Vector speed;
    protected double trace;
    protected boolean absorbed;
    protected boolean out;

    protected Particle(final CartesianPoint coordinate, final Vector speed) {
        this.coordinate = coordinate;
        this.speed = speed;
        this.trace = 0.0;
        this.absorbed = false;
        this.out = false;
    }

    public Particle shiftCoordinate(final CartesianPoint step) {
        CartesianPoint newCoordinate = coordinate.shift(step);
        increaseTrace(newCoordinate);
        coordinate = newCoordinate;
        return this;
    }

    public Particle shiftCoordinate(double x, double y, double z) {
        CartesianPoint newCoordinate = coordinate.shift(x, y, z);
        increaseTrace(newCoordinate);
        coordinate = newCoordinate;
        return this;
    }

    public Particle rotateSpeed(final Vector vector, double angle) {
        speed = speed.rotateAroundVector(vector, angle);
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

    // Возвращает проекцию координаты на плоскость, расположенную под углом angle к абсолютной
    public CartesianPoint rotateFrameAroundOY(double angle) {
        return new RotationMatrixY(angle).rotate(coordinate);
    }

    public Vector getSpeed() {
        return speed;
    }

    public void setSpeed(final Vector speed) {
        this.speed = speed;
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
}
