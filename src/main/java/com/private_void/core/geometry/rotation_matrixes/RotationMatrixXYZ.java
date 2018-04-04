package com.private_void.core.geometry.rotation_matrixes;

import com.private_void.core.geometry.vectors.Vector;

public class RotationMatrixXYZ extends RotationMatrix {

    public RotationMatrixXYZ(final Vector axis, double angle) {
        super();

        matrix[0][0] = Math.cos(angle) + (1.0 - Math.cos(angle)) * axis.getX() * axis.getX();
        matrix[1][0] = (1.0 - Math.cos(angle)) * axis.getY() * axis.getX() - Math.sin(angle) * axis.getZ();
        matrix[2][0] = (1.0 - Math.cos(angle)) * axis.getZ() * axis.getX() + Math.sin(angle) * axis.getY();

        matrix[0][1] = (1.0 - Math.cos(angle)) * axis.getX() * axis.getY() + Math.sin(angle) * axis.getZ();
        matrix[1][1] = Math.cos(angle) + (1.0 - Math.cos(angle)) * axis.getY() * axis.getY();
        matrix[2][1] = (1.0 - Math.cos(angle)) * axis.getZ() * axis.getY() - Math.sin(angle) * axis.getX();

        matrix[0][2] = (1.0 - Math.cos(angle)) * axis.getX() * axis.getZ() - Math.sin(angle) * axis.getY();
        matrix[1][2] = (1.0 - Math.cos(angle)) * axis.getY() * axis.getZ() + Math.sin(angle) * axis.getX();
        matrix[2][2] = Math.cos(angle) + (1.0 - Math.cos(angle)) * axis.getZ() * axis.getZ();
    }
}
