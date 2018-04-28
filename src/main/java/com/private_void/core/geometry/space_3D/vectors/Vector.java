package com.private_void.core.geometry.space_3D.vectors;

import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.rotation_matrix.RotationMatrix;

public class Vector extends CartesianPoint {
    public static final Vector E_X = new Vector(1.0, 0.0, 0.0);
    public static final Vector E_Y = new Vector(0.0, 1.0, 0.0);
    public static final Vector E_Z = new Vector(0.0, 0.0, 1.0);

    private Vector(double x, double y, double z) {
        super(x, y, z);
    }

    public static Vector set(double x, double y, double z) throws IllegalArgumentException {
        if (x == 0.0 && y == 0.0 && z == 0.0) {
            throw new IllegalArgumentException("Vector's length can't equals 0");
        }

        double norm = Math.sqrt(x * x + y * y + z * z);

        x = x / norm;
        y = y / norm;
        z = z / norm;

        return new Vector(x, y, z);
    }

    public static Vector set(double[] component) throws IllegalArgumentException {
        if (component[0] == 0.0 && component[1] == 0.0 && component[2] == 0.0) {
            throw new IllegalArgumentException("Vector's length can't equals 0");
        }

        double norm = Math.sqrt(component[0] * component[0] + component[1] * component[1] + component[2] * component[2]);

        component[0] = component[0] / norm;
        component[1] = component[1] / norm;
        component[2] = component[2] / norm;

        return new Vector(component[0], component[1], component[2]);
    }

    public static Vector set(final Vector vector){
        return new Vector(vector.x, vector.y, vector.z);
    }

    public Vector rotateAroundOX(double angle) {
        return RotationMatrix.aroundOX(angle).rotate(this);
    }

    public Vector rotateAroundOY(double angle) {
        return RotationMatrix.aroundOY(angle).rotate(this);
    }

    public Vector rotateAroundOZ(double angle) {
        return RotationMatrix.aroundOZ(angle).rotate(this);
    }

    public Vector rotateAroundVector(final Vector vector, double angle) {
        return RotationMatrix.aroundVector(vector, angle).rotate(this);
    }

    public double getAngle(final Vector vec) {
        return Math.acos((x * vec.x + y * vec.y + z * vec.z)
                / (Math.sqrt(x * x + y * y + z * z) * Math.sqrt(vec.x * vec.x + vec.y * vec.y + vec.z * vec.z)));
    }
}