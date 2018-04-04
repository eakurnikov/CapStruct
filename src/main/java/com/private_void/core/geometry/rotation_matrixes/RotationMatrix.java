package com.private_void.core.geometry.rotation_matrixes;

import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.vectors.Vector;

// Положительным углам соответствует вращение вектора против часовой стрелки в правой системе координат,
// и по часовой стрелке в левой системе координат, если смотреть против направления соответствующей оси

public abstract class RotationMatrix {
    protected final double[][] matrix;

    protected RotationMatrix() {
        matrix = new double[3][3];
    }

    public final Vector rotate(final Vector vector) {
        double[] temp = {0.0, 0.0, 0.0};

        for (int i = 0; i < 3; i++) {
            temp[i] = matrix[0][i] * vector.getX() + matrix[1][i] * vector.getY() + matrix[2][i] * vector.getZ();
        }

        return Vector.set(temp);
    }

    public final CartesianPoint rotate(final CartesianPoint point) {
        double[] temp = {0.0, 0.0, 0.0};

        for (int i = 0; i < 3; i++) {
            temp[i] = matrix[0][i] * point.getX() + matrix[1][i] * point.getY() + matrix[2][i] * point.getZ();
        }

        return new CartesianPoint(temp[0], temp[1], temp[2]);
    }
}
