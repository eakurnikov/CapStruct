package com.private_void.core;

//TODO: возможно, заменить использование массивов на какие-то контейнеры.
public final class Utils {

    public static float zero = 1e-5f;

    private Utils() {
        zero = 1e-5f;
    }

    public static boolean compareToZero(float value) {
        return Math.abs(value) < zero;
    }

    public static void matrixMultiplication(float[][] A, float[] B, float[] C) {
        for (int i = 0; i < 3; i++) {
            C[i] = 0;
            for (int k = 0; k < 3; k++) {
                C[i] += A[i][k] * B[k];
            }
        }
    }

    public static void inverseMatrix(float[][] A, float[][] B) {
        try {
            float detA = A[0][0] * det(A, 0, 0) + A[0][1] * det(A, 0, 1) + A[0][2] * det(A, 0, 2);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    B[j][i] = det(A, i, j) / detA;
                }
            }
        }
        catch (ArithmeticException e) {
            System.out.println("Division by zero.");
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static float det(float[][] A, int i, int j) {

        i += 1;
        if (i == 3)
            i -= 3;

        j += 1;
        if (j == 3)
            j -= 3;

        int a = i + 1;
        if (a == 3)
            a -= 3;

        int b = j + 1;
        if (b == 3)
            b -= 3;

        return A[i][j] * A[a][ b] - A[i][b] * A[a][j];

    }

    public static float findMax(float[] A) {
        float max = Math.abs(A[0]);
        for (int i = 1; i < 3; i++) {
            if (Math.abs(A[i]) > max)
                max = Math.abs(A[i]);
        }
        return max;
    }

    public static float convertDegreesToRads(final float angleDegrees) {
        return (float) Math.PI * angleDegrees / 180;
    }
}
