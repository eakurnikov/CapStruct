package com.private_void.core.geometry;

// TODO: Положительным углам при этом соответствует вращение вектора против часовой стрелки в правой системе координат, и по часовой стрелке в левой системе координат, если смотреть против направления соответствующей оси

public class Vector extends CartesianPoint {
    private float[][] rotationMatrixXYZ;
    private float[][] rotationMatrixX;
    private float[][] rotationMatrixY;

    public Vector(float x, float y, float z) {
        super(x, y, z);
        rotationMatrixXYZ = new float[3][3];
        rotationMatrixX = new float[3][3];
        rotationMatrixY = new float[3][3];
        normalize();
    }

    private void normalize() {
        float norm = (float) Math.sqrt(x * x + y * y + z * z);
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

    public Vector turnAroundOX(float angle) {
        float[] temp = {0.0f, 0.0f, 0.0f};
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

    public Vector turnAroundOY(float angle) {
        float[] temp = {0.0f, 0.0f, 0.0f};
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

    public Vector turnAroundVector(float angle, final Vector axis) {
        float[] temp = {0.0f, 0.0f, 0.0f};
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

    public Vector getNewByTurningAroundOX(float angle) {
        float[] temp = {0.0f, 0.0f, 0.0f};
        setRotationMatrixX(angle);

        for (int i = 0; i < 3; i++) {
            temp[i] = rotationMatrixX[0][i] * x + rotationMatrixX[1][i] * y + rotationMatrixX[2][i] * z;
        }

        Vector result = new Vector(temp[0], temp[1], temp[2]);
        result.normalize();
        return result;
    }

    public Vector getNewByTurningAroundVector(float angle, final Vector axis) {
        float[] temp = {0.0f, 0.0f, 0.0f};
        setRotationMatrixXYZ(angle, axis);

        for (int i = 0; i < 3; i++) {
            temp[i] = rotationMatrixXYZ[0][i] * x + rotationMatrixXYZ[1][i] * y + rotationMatrixXYZ[2][i] * z;
        }

        Vector result = new Vector(temp[0], temp[1], temp[2]);
        result.normalize();
        return result;
    }

    public float getAngle(final Vector vector) {
        return (float) (Math.acos((x * vector.getX() + y * vector.getY() + z * vector.getZ()) /
                       (Math.sqrt(x * x + y * y + z * z) * Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY() + vector.getZ() * vector.getZ()))));
    }

    private void setRotationMatrixX(float angle) {
        rotationMatrixX[0][0] = 1.0f;
        rotationMatrixX[1][0] = 0.0f;
        rotationMatrixX[2][0] = 0.0f;

        rotationMatrixX[0][1] = 0.0f;
        rotationMatrixX[1][1] = (float) Math.cos(angle);
        rotationMatrixX[2][1] = (float) -Math.sin(angle);

        rotationMatrixX[0][2] = 0.0f;
        rotationMatrixX[1][2] = (float) Math.sin(angle);
        rotationMatrixX[2][2] = (float) Math.cos(angle);
    }

    private void setRotationMatrixY(float angle) {
        rotationMatrixY[0][0] = (float) Math.cos(angle);
        rotationMatrixY[1][0] = 0.0f;
        rotationMatrixY[2][0] = (float) Math.sin(angle);

        rotationMatrixY[0][1] = 0.0f;
        rotationMatrixY[1][1] = 1.0f;
        rotationMatrixY[2][1] = 0.0f;

        rotationMatrixY[0][2] = (float) -Math.sin(angle);
        rotationMatrixY[1][2] = 0.0f;
        rotationMatrixY[2][2] = (float) Math.cos(angle);
    }

    private void setRotationMatrixXYZ(float angle, final Vector axis) {
        rotationMatrixXYZ[0][0] = (float) (Math.cos(angle) + (1.0f - Math.cos(angle)) * axis.getX() * axis.getX());
        rotationMatrixXYZ[1][0] = (float) ((1.0f - Math.cos(angle)) * axis.getY() * axis.getX() - Math.sin(angle) * axis.getZ());
        rotationMatrixXYZ[2][0] = (float) ((1.0f - Math.cos(angle)) * axis.getZ() * axis.getX() + Math.sin(angle) * axis.getY());

        rotationMatrixXYZ[0][1] = (float) ((1.0f - Math.cos(angle)) * axis.getX() * axis.getY() + Math.sin(angle) * axis.getZ());
        rotationMatrixXYZ[1][1] = (float) (Math.cos(angle) + (1.0f - Math.cos(angle)) * axis.getY() * axis.getY());
        rotationMatrixXYZ[2][1] = (float) ((1.0f - Math.cos(angle)) * axis.getZ() * axis.getY() - Math.sin(angle) * axis.getX());

        rotationMatrixXYZ[0][2] = (float) ((1.0f - Math.cos(angle)) * axis.getX() * axis.getZ() - Math.sin(angle) * axis.getY());
        rotationMatrixXYZ[1][2] = (float) ((1.0f - Math.cos(angle)) * axis.getY() * axis.getZ() + Math.sin(angle) * axis.getX());
        rotationMatrixXYZ[2][2] = (float) (Math.cos(angle) + (1.0f - Math.cos(angle)) * axis.getZ() * axis.getZ());
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        normalize();
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        normalize();
    }

    @Override
    public void setZ(float z) {
        super.setZ(z);
        normalize();
    }
}