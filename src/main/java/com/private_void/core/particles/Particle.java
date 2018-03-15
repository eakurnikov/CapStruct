package com.private_void.core.particles;

import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.Vector;

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

    public CartesianPoint getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(final CartesianPoint newCoordinate) {
        increaseTrace(newCoordinate);
        this.coordinate = newCoordinate;
    }

    // Возвращает проекцию координаты на плоскость, расположенную под углом angle к абсолютной
    public CartesianPoint getProjection(double angle) {
        double[][] rotationMatrixY = new double[3][3];

        rotationMatrixY[0][0] = Math.cos(angle);
        rotationMatrixY[1][0] = 0.0;
        rotationMatrixY[2][0] = Math.sin(angle);

        rotationMatrixY[0][1] = 0.0;
        rotationMatrixY[1][1] = 1.0;
        rotationMatrixY[2][1] = 0.0;

        rotationMatrixY[0][2] = -Math.sin(angle);
        rotationMatrixY[1][2] = 0.0;
        rotationMatrixY[2][2] = Math.cos(angle);

        double[] temp = {0.0, 0.0, 0.0};
        for (int i = 0; i < 3; i++) {
            temp[i] = rotationMatrixY[0][i] * coordinate.getX() + rotationMatrixY[1][i] * coordinate.getY() + rotationMatrixY[2][i] * coordinate.getZ();
        }

        return new CartesianPoint(temp[0], temp[1], temp[2]);
    }

    public Vector getSpeed() {
        return speed;
    }

    public void setSpeed(final Vector speed) {
        this.speed = speed;
    }

    public double getTrace() {
        return trace;
    }

    public void setTrace(double trace) {
        this.trace = trace;
    }

    public void increaseTrace(final CartesianPoint point) {
        trace = Math.sqrt((point.getX() - coordinate.getX()) * (point.getX() - coordinate.getX())
                + (point.getY() - coordinate.getY()) * (point.getY() - coordinate.getY())
                + (point.getZ() - coordinate.getZ()) * (point.getZ() - coordinate.getZ()));
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
}
