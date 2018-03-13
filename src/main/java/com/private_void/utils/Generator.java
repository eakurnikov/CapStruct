package com.private_void.utils;

import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.CoordinateFactory;
import com.private_void.core.geometry.SphericalCoordinateFactory;
import com.private_void.core.geometry.SphericalPoint;

import java.util.Random;

import static com.private_void.utils.Constants.SEED;

public class Generator {
    private Random rand;

    private Generator(int seed) {
        rand = new Random(seed);
    }

    private static class Holder {
        private static final Generator instance = new Generator(SEED);
    }

    public static Generator generator() {
        return Holder.instance;
    }

    public int uniformInt(int bound) {
        return rand.nextInt(bound);
    }

    public float uniformFloat(float minValue, float maxValue) {
        return minValue + (maxValue - minValue) * rand.nextFloat();
    }

    public CoordinateFactory getGaussDistribution(float mean, float dev) {
        return () -> {
            float u, v, s;
            float y, z;

            do {
                u = rand.nextFloat() * 2 - 1;
                v = rand.nextFloat() * 2 - 1;
                s = u * u + v * v;
            } while (s >= 1 || s == 0);

            y = v * (float) Math.sqrt(-2.0f * Math.log(s) / s);
            z = u * (float) Math.sqrt(-2.0f * Math.log(s) / s);

            return new CartesianPoint(0.0f, mean + y * dev, mean + z * dev);
        };
    }

    public CoordinateFactory getGaussDistribution(final CartesianPoint center, float mean, float dev) {
        return () -> {
            float u, v, s;
            float y, z;

            do {
                u = rand.nextFloat() * 2 - 1;
                v = rand.nextFloat() * 2 - 1;
                s = u * u + v * v;
            } while (s >= 1 || s == 0);

            y = v * (float) Math.sqrt(-2.0f * Math.log(s) / s);
            z = u * (float) Math.sqrt(-2.0f * Math.log(s) / s);

            return new CartesianPoint(0.0f, mean + y * dev, mean + z * dev).shift(center);
        };
    }

    public CoordinateFactory getXFlatCircleUniformDistribution(float radius) {
        return () -> {
            float y;
            float z;

            do {
                y = uniformFloat(-radius, radius);
                z = uniformFloat(-radius, radius);
            } while (y * y + z * z > radius * radius);

            return new CartesianPoint(0.0f, y, z);
        };
    }

    public CoordinateFactory getXFlatCircleUniformDistribution(final CartesianPoint center, float radius) {
        return () -> {
            float y;
            float z;

            do {
                y = uniformFloat(-radius, radius);
                z = uniformFloat(-radius, radius);
            } while (y * y + z * z > radius * radius);

            return new CartesianPoint(0.0f, y, z).shift(center);
        };
    }

    public CoordinateFactory getXFlatUniformDistribution(float yRange, float zRange) {
        return () -> new CartesianPoint(
                0.0f,
                uniformFloat(-yRange, yRange),
                uniformFloat(-zRange, zRange));
    }

    public CoordinateFactory getXFlatUniformDistribution(final CartesianPoint center, float yRange, float zRange) {
        return () -> new CartesianPoint(
                0.0f,
                uniformFloat(-yRange, yRange),
                uniformFloat(-zRange, zRange))
                .shift(center);
    }

    public SphericalCoordinateFactory getSphericalUniformDistribution(float radius, float thetaRange, float phiRange) {
        return () -> new SphericalPoint(radius,
                uniformFloat(-thetaRange, thetaRange),
                uniformFloat(-phiRange, phiRange));
    }

    public SphericalCoordinateFactory getSphericalUniformDistribution(final SphericalPoint config,
                                                                      float radius, float thetaRange, float phiRange) {
        return () -> new SphericalPoint(radius,
                uniformFloat(-thetaRange, thetaRange),
                uniformFloat(-phiRange, phiRange))
                .shift(config);
    }

    public CoordinateFactory getVolumeUniformDistribution(float xRange, float yRange, float zRange) {
        return () -> new CartesianPoint(
                uniformFloat(-xRange, xRange),
                uniformFloat(-yRange, yRange),
                uniformFloat(-zRange, zRange));
    }

    public CoordinateFactory getVolumeUniformDistribution(final CartesianPoint center,
                                                          float xRange, float yRange, float zRange) {
        return () -> new CartesianPoint(
                uniformFloat(-xRange, xRange),
                uniformFloat(-yRange, yRange),
                uniformFloat(-zRange, zRange))
                .shift(center);
    }
}