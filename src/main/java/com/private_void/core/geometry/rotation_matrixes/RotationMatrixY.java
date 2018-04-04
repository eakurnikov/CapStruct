package com.private_void.core.geometry.rotation_matrixes;

public class RotationMatrixY extends RotationMatrix {

    public RotationMatrixY(double angle) {
        super();

        matrix[0][0] = Math.cos(angle);
        matrix[1][0] = 0.0;
        matrix[2][0] = Math.sin(angle);

        matrix[0][1] = 0.0;
        matrix[1][1] = 1.0;
        matrix[2][1] = 0.0;

        matrix[0][2] = -Math.sin(angle);
        matrix[1][2] = 0.0;
        matrix[2][2] = Math.cos(angle);
    }
}