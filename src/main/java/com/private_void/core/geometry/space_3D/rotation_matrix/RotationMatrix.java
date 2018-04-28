package com.private_void.core.geometry.space_3D.rotation_matrix;

import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.vectors.Vector;

// Положительным углам соответствует вращение вектора против часовой стрелки в правой системе координат,
// и по часовой стрелке в левой системе координат, если смотреть против направления соответствующей оси

public class RotationMatrix {
    protected final double[][] matrix;

    private RotationMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public static RotationMatrix aroundVector(final Vector vector, double angle) {
        double[][] matrix = new double[3][3];

        matrix[0][0] = Math.cos(angle) + (1.0 - Math.cos(angle)) * vector.getX() * vector.getX();
        matrix[1][0] = (1.0 - Math.cos(angle)) * vector.getY() * vector.getX() - Math.sin(angle) * vector.getZ();
        matrix[2][0] = (1.0 - Math.cos(angle)) * vector.getZ() * vector.getX() + Math.sin(angle) * vector.getY();

        matrix[0][1] = (1.0 - Math.cos(angle)) * vector.getX() * vector.getY() + Math.sin(angle) * vector.getZ();
        matrix[1][1] = Math.cos(angle) + (1.0 - Math.cos(angle)) * vector.getY() * vector.getY();
        matrix[2][1] = (1.0 - Math.cos(angle)) * vector.getZ() * vector.getY() - Math.sin(angle) * vector.getX();

        matrix[0][2] = (1.0 - Math.cos(angle)) * vector.getX() * vector.getZ() - Math.sin(angle) * vector.getY();
        matrix[1][2] = (1.0 - Math.cos(angle)) * vector.getY() * vector.getZ() + Math.sin(angle) * vector.getX();
        matrix[2][2] = Math.cos(angle) + (1.0 - Math.cos(angle)) * vector.getZ() * vector.getZ();

        return new RotationMatrix(matrix);
    }

    public static RotationMatrix aroundOX(double angle) {
        double[][] matrix = new double[3][3];

        matrix[0][0] = 1.0;
        matrix[1][0] = 0.0;
        matrix[2][0] = 0.0;

        matrix[0][1] = 0.0;
        matrix[1][1] = Math.cos(angle);
        matrix[2][1] = -Math.sin(angle);

        matrix[0][2] = 0.0;
        matrix[1][2] = Math.sin(angle);
        matrix[2][2] = Math.cos(angle);

        return new RotationMatrix(matrix);
    }

    public static RotationMatrix aroundOY(double angle) {
        double[][] matrix = new double[3][3];

        matrix[0][0] = Math.cos(angle);
        matrix[1][0] = 0.0;
        matrix[2][0] = Math.sin(angle);

        matrix[0][1] = 0.0;
        matrix[1][1] = 1.0;
        matrix[2][1] = 0.0;

        matrix[0][2] = -Math.sin(angle);
        matrix[1][2] = 0.0;
        matrix[2][2] = Math.cos(angle);

        return new RotationMatrix(matrix);
    }

    public static RotationMatrix aroundOZ(double angle) {
        double[][] matrix = new double[3][3];

        matrix[0][0] = Math.cos(angle);
        matrix[1][0] = -Math.sin(angle);
        matrix[2][0] = 0.0;

        matrix[0][1] = Math.sin(angle);
        matrix[1][1] = Math.cos(angle);
        matrix[2][1] = 0.0;

        matrix[0][2] = 0.0;
        matrix[1][2] = 0.0;
        matrix[2][2] = 1.0;

        return new RotationMatrix(matrix);
    }

    public Vector rotate(final Vector vector) {
        double[] temp = {0.0, 0.0, 0.0};

        for (int i = 0; i < 3; i++) {
            temp[i] = matrix[0][i] * vector.getX() + matrix[1][i] * vector.getY() + matrix[2][i] * vector.getZ();
        }

        return Vector.set(temp);
    }

    public CartesianPoint rotate(final CartesianPoint point) {
        double[] temp = {0.0, 0.0, 0.0};

        for (int i = 0; i < 3; i++) {
            temp[i] = matrix[0][i] * point.getX() + matrix[1][i] * point.getY() + matrix[2][i] * point.getZ();
        }

        return new CartesianPoint(temp);
    }
}
