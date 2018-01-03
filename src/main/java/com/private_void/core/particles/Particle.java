package com.private_void.core.particles;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;

public abstract class Particle {
    protected Point3D coordinate;
    protected Vector3D speed;
    protected float trace;
    protected boolean absorbed;

    protected Particle(final Point3D coordinate, final Vector3D speed) {
        this.coordinate = coordinate;
        this.speed = speed;
        this.trace = 0.0f;
        this.absorbed = false;
    }

    public Point3D getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(final Point3D newCoordinate) {
        increaseTrace(newCoordinate);
        this.coordinate = newCoordinate;
    }

    // Возвращает проекцию координаты на плоскость, расположенную под углом angle к абсолютной
    public Point3D getProjection(float angle) {
        float[][] rotationMatrixY = new float[3][3];

        rotationMatrixY[0][0] = (float) Math.cos(angle);
        rotationMatrixY[1][0] = 0.0f;
        rotationMatrixY[2][0] = (float) Math.sin(angle);

        rotationMatrixY[0][1] = 0.0f;
        rotationMatrixY[1][1] = 1.0f;
        rotationMatrixY[2][1] = 0.0f;

        rotationMatrixY[0][2] = (float) -Math.sin(angle);
        rotationMatrixY[1][2] = 0.0f;
        rotationMatrixY[2][2] = (float) Math.cos(angle);

        float[] temp = {0.0f, 0.0f, 0.0f};
        for (int i = 0; i < 3; i++) {
            temp[i] = rotationMatrixY[0][i] * coordinate.getX() + rotationMatrixY[1][i] * coordinate.getY() + rotationMatrixY[2][i] * coordinate.getZ();
        }

        return new Point3D(temp[0], temp[1], temp[2]);
    }

    public Vector3D getSpeed() {
        return speed;
    }

    public void setSpeed(final Vector3D speed) {
        this.speed = speed;
    }

    public float getTrace() {
        return trace;
    }

    public void setTrace(float trace) {
        this.trace = trace;
    }

    public void increaseTrace(final Point3D point3D) {
        trace = (float) Math.sqrt((point3D.getX() - coordinate.getX()) * (point3D.getX() - coordinate.getX())
                + (point3D.getY() - coordinate.getY()) * (point3D.getY() - coordinate.getY())
                + (point3D.getZ() - coordinate.getZ()) * (point3D.getZ() - coordinate.getZ()));
    }

    public boolean isAbsorbed() {
        return absorbed;
    }

    public void setAbsorbed(boolean absorbed) {
        this.absorbed = absorbed;
    }
}
