package com.private_void.core.geometry;

// TODO: Положительным углам при этом соответствует вращение вектора против часовой стрелки в правой системе координат, и по часовой стрелке в левой системе координат, если смотреть против направления соответствующей оси

public class Vector extends CartesianPoint {
    private double[][] rotationMatrixXYZ;
    private double[][] rotationMatrixX;
    private double[][] rotationMatrixY;

    public Vector(double x, double y, double z) {
        super(x, y, z);
        rotationMatrixXYZ = new double[3][3];
        rotationMatrixX = new double[3][3];
        rotationMatrixY = new double[3][3];
        normalize();
    }

    private void normalize() {
        double norm =  Math.sqrt(x * x + y * y + z * z);
        try {
            x = x / norm;
            y = y / norm;
            z = z / norm;
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
    }

    public Vector inverse() {
        super.setX(-x);
        super.setY(-y);
        super.setZ(-z);
        normalize();
        return this;
    }

    public Vector turnAroundOX(double angle) {
        double[] temp = {0.0, 0.0, 0.0};
        setRotationMatrixX(angle);

        for (int i = 0; i < 3; i++) {
            temp[i] = rotationMatrixX[0][i] * x + rotationMatrixX[1][i] * y + rotationMatrixX[2][i] * z;
        }

        x = temp[0];
        y = temp[1];
        z = temp[2];

        normalize();
        return this;
    }

    public Vector turnAroundOY(double angle) {
        double[] temp = {0.0, 0.0, 0.0};
        setRotationMatrixY(angle);

        for (int i = 0; i < 3; i++) {
            temp[i] = rotationMatrixY[0][i] * x + rotationMatrixY[1][i] * y + rotationMatrixY[2][i] * z;
        }

        x = temp[0];
        y = temp[1];
        z = temp[2];

        normalize();
        return this;
    }

    public Vector turnAroundVector(double angle, final Vector axis) {
        double[] temp = {0.0, 0.0, 0.0};
        setRotationMatrixXYZ(angle, axis);

        for (int i = 0; i < 3; i++) {
            temp[i] = rotationMatrixXYZ[0][i] * x + rotationMatrixXYZ[1][i] * y + rotationMatrixXYZ[2][i] * z;
        }

        x = temp[0];
        y = temp[1];
        z = temp[2];

        normalize();
        return this;
    }

    public Vector getNewByTurningAroundOX(double angle) {
        double[] temp = {0.0, 0.0, 0.0};
        setRotationMatrixX(angle);

        for (int i = 0; i < 3; i++) {
            temp[i] = rotationMatrixX[0][i] * x + rotationMatrixX[1][i] * y + rotationMatrixX[2][i] * z;
        }

        return new Vector(temp[0], temp[1], temp[2]);
    }

    public Vector getNewByTurningAroundOY(double angle) {
        double[] temp = {0.0, 0.0, 0.0};
        setRotationMatrixY(angle);

        for (int i = 0; i < 3; i++) {
            temp[i] = rotationMatrixY[0][i] * x + rotationMatrixY[1][i] * y + rotationMatrixY[2][i] * z;
        }

        return new Vector(temp[0], temp[1], temp[2]);
    }

    public Vector getNewByTurningAroundVector(double angle, final Vector axis) {
        double[] temp = {0.0, 0.0, 0.0};
        setRotationMatrixXYZ(angle, axis);

        for (int i = 0; i < 3; i++) {
            temp[i] = rotationMatrixXYZ[0][i] * x + rotationMatrixXYZ[1][i] * y + rotationMatrixXYZ[2][i] * z;
        }

        return new Vector(temp[0], temp[1], temp[2]);
    }

    public double getAngle(final Vector vector) {
        return Math.acos((x * vector.getX() + y * vector.getY() + z * vector.getZ()) 
                / (Math.sqrt(x * x + y * y + z * z) * Math.sqrt(vector.getX() * vector.getX() 
                + vector.getY() * vector.getY() + vector.getZ() * vector.getZ())));
    }

    private void setRotationMatrixX(double angle) {
        rotationMatrixX[0][0] = 1.0;
        rotationMatrixX[1][0] = 0.0;
        rotationMatrixX[2][0] = 0.0;

        rotationMatrixX[0][1] = 0.0;
        rotationMatrixX[1][1] = Math.cos(angle);
        rotationMatrixX[2][1] = -Math.sin(angle);

        rotationMatrixX[0][2] = 0.0;
        rotationMatrixX[1][2] = Math.sin(angle);
        rotationMatrixX[2][2] = Math.cos(angle);
    }

    private void setRotationMatrixY(double angle) {
        rotationMatrixY[0][0] = Math.cos(angle);
        rotationMatrixY[1][0] = 0.0;
        rotationMatrixY[2][0] = Math.sin(angle);

        rotationMatrixY[0][1] = 0.0;
        rotationMatrixY[1][1] = 1.0;
        rotationMatrixY[2][1] = 0.0;

        rotationMatrixY[0][2] = -Math.sin(angle);
        rotationMatrixY[1][2] = 0.0;
        rotationMatrixY[2][2] = Math.cos(angle);
    }

    private void setRotationMatrixXYZ(double angle, final Vector axis) {
        rotationMatrixXYZ[0][0] = Math.cos(angle) + (1.0 - Math.cos(angle)) * axis.getX() * axis.getX();
        rotationMatrixXYZ[1][0] = (1.0 - Math.cos(angle)) * axis.getY() * axis.getX() - Math.sin(angle) * axis.getZ();
        rotationMatrixXYZ[2][0] = (1.0 - Math.cos(angle)) * axis.getZ() * axis.getX() + Math.sin(angle) * axis.getY();

        rotationMatrixXYZ[0][1] = (1.0 - Math.cos(angle)) * axis.getX() * axis.getY() + Math.sin(angle) * axis.getZ();
        rotationMatrixXYZ[1][1] = Math.cos(angle) + (1.0 - Math.cos(angle)) * axis.getY() * axis.getY();
        rotationMatrixXYZ[2][1] = (1.0 - Math.cos(angle)) * axis.getZ() * axis.getY() - Math.sin(angle) * axis.getX();

        rotationMatrixXYZ[0][2] = (1.0 - Math.cos(angle)) * axis.getX() * axis.getZ() - Math.sin(angle) * axis.getY();
        rotationMatrixXYZ[1][2] = (1.0 - Math.cos(angle)) * axis.getY() * axis.getZ() + Math.sin(angle) * axis.getX();
        rotationMatrixXYZ[2][2] = Math.cos(angle) + (1.0 - Math.cos(angle)) * axis.getZ() * axis.getZ();
    }

    @Override
    public void setX(double x) {
        super.setX(x);
        normalize();
    }

    @Override
    public void setY(double y) {
        super.setY(y);
        normalize();
    }

    @Override
    public void setZ(double z) {
        super.setZ(z);
        normalize();
    }
}