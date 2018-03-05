package com.private_void.utils;

import com.private_void.core.geometry.CoordinateFactory;
import com.private_void.core.geometry.Point3D;

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

            return new Point3D(0.0f, mean + y * dev, mean + z * dev);
        };
    }

    public CoordinateFactory getGaussDistribution(final Point3D center, float mean, float dev) {
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

            return new Point3D(center.getX(), center.getY() + mean + y * dev, center.getZ() + mean + z * dev);
        };
    }

    public CoordinateFactory getXPlanarCircleUniformDistribution(float radius) {
        return () -> {
            float y;
            float z;

            do {
                y = uniformFloat(-radius, radius);
                z = uniformFloat(-radius, radius);
            } while (y * y + z * z > radius * radius);

            return new Point3D(0.0f, y, z);
        };
    }

    public CoordinateFactory getXPlanarCircleUniformDistribution(final Point3D center, float radius) {
        return () -> {
            float y;
            float z;

            do {
                y = uniformFloat(-radius, radius);
                z = uniformFloat(-radius, radius);
            } while (y * y + z * z > radius * radius);

            return new Point3D(center.getX(), center.getY() + y, center.getZ() + z);
        };
    }

    public CoordinateFactory getXPlanarUniformDistribution(float yRange, float zRange) {
        return () -> new Point3D(
                0.0f,
                uniformFloat(-yRange, yRange),
                uniformFloat(-zRange, zRange));
    }

    public CoordinateFactory getXPlanarUniformDistribution(final Point3D center, float yRange, float zRange) {
        return () -> new Point3D(
                center.getX(),
                uniformFloat(center.getY() - yRange, center.getY() + yRange),
                uniformFloat(center.getZ() - zRange, center.getZ() + zRange));
    }

    public CoordinateFactory getVolumeUniformDistribution(float xRange, float yRange, float zRange) {
        return () -> new Point3D(
                uniformFloat(-xRange, xRange),
                uniformFloat(-yRange, yRange),
                uniformFloat(-zRange, zRange));
    }

    public CoordinateFactory getVolumeUniformDistribution(final Point3D center, float xRange, float yRange, float zRange) {
        return () -> new Point3D(
                uniformFloat(center.getX() - xRange, center.getX() + xRange),
                uniformFloat(center.getY() - yRange, center.getY() + yRange),
                uniformFloat(center.getZ() - zRange, center.getZ() + zRange));
    }
}