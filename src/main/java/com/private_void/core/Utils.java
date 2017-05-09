package com.private_void.core;

public final class Utils {

    public static float zero = 1e-5f;

    private Utils() {
        zero = 1e-5f;
    }

    public static boolean compareToZero(float value)
    {
        return Math.abs(value) < zero;
    }
}
