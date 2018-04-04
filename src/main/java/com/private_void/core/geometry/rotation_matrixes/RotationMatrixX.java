package com.private_void.core.geometry.rotation_matrixes;

public class RotationMatrixX extends RotationMatrix {

    public RotationMatrixX(double angle) {
        super();

        matrix[0][0] = 1.0;
        matrix[1][0] = 0.0;
        matrix[2][0] = 0.0;

        matrix[0][1] = 0.0;
        matrix[1][1] = Math.cos(angle);
        matrix[2][1] = -Math.sin(angle);

        matrix[0][2] = 0.0;
        matrix[1][2] = Math.sin(angle);
        matrix[2][2] = Math.cos(angle);
    }
}