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

    public double uniformDouble(double minValue, double maxValue) {
        return minValue + (maxValue - minValue) * rand.nextDouble();
    }

    public CoordinateFactory getGaussDistribution(double mean, double dev) {
        return () -> {
            double u, v, s;
            double y, z;

            do {
                u = rand.nextDouble() * 2.0 - 1.0;
                v = rand.nextDouble() * 2.0 - 1.0;
                s = u * u + v * v;
            } while (s >= 1 || s == 0.0);

            y = v * Math.sqrt(-2.0 * Math.log(s) / s);
            z = u * Math.sqrt(-2.0 * Math.log(s) / s);

            return new CartesianPoint(0.0, mean + y * dev, mean + z * dev);
        };
    }

    public CoordinateFactory getGaussDistribution(final CartesianPoint center, double mean, double dev) {
        return () -> {
            double u, v, s;
            double y, z;

            do {
                u = rand.nextDouble() * 2.0 - 1.0;
                v = rand.nextDouble() * 2.0 - 1.0;
                s = u * u + v * v;
            } while (s >= 1.0 || s == 0.0);

            y = v * Math.sqrt(-2.0 * Math.log(s) / s);
            z = u * Math.sqrt(-2.0 * Math.log(s) / s);

            return new CartesianPoint(0.0, mean + y * dev, mean + z * dev).shift(center);
        };
    }

    public CoordinateFactory getXFlatCircleUniformDistribution(double radius) {
        return () -> {
            double y;
            double z;

            do {
                y = uniformDouble(-radius, radius);
                z = uniformDouble(-radius, radius);
            } while (y * y + z * z > radius * radius);

            return new CartesianPoint(0.0, y, z);
        };
    }

    public CoordinateFactory getXFlatCircleUniformDistribution(final CartesianPoint center, double radius) {
        return () -> {
            double y;
            double z;

            do {
                y = uniformDouble(-radius, radius);
                z = uniformDouble(-radius, radius);
            } while (y * y + z * z > radius * radius);

            return new CartesianPoint(0.0, y, z).shift(center);
        };
    }

    public CoordinateFactory getXFlatUniformDistribution(double yRange, double zRange) {
        return () -> new CartesianPoint(
                0.0,
                uniformDouble(-yRange, yRange),
                uniformDouble(-zRange, zRange));
    }

    public CoordinateFactory getXFlatUniformDistribution(final CartesianPoint center, double yRange, double zRange) {
        return () -> new CartesianPoint(
                0.0,
                uniformDouble(-yRange, yRange),
                uniformDouble(-zRange, zRange))
                .shift(center);
    }

    public SphericalCoordinateFactory getSphericalUniformDistribution(double radius, double thetaRange, double phiRange) {
        return () -> new SphericalPoint(radius,
                uniformDouble(-thetaRange, thetaRange),
                uniformDouble(-phiRange, phiRange));
    }

    public SphericalCoordinateFactory getSphericalUniformDistribution(final SphericalPoint config,
                                                                      double radius, double thetaRange, double phiRange) {
        return () -> new SphericalPoint(radius,
                uniformDouble(-thetaRange, thetaRange),
                uniformDouble(-phiRange, phiRange))
                .shift(config);
    }

    public CoordinateFactory getVolumeUniformDistribution(double xRange, double yRange, double zRange) {
        return () -> new CartesianPoint(
                uniformDouble(-xRange, xRange),
                uniformDouble(-yRange, yRange),
                uniformDouble(-zRange, zRange));
    }

    public CoordinateFactory getVolumeUniformDistribution(final CartesianPoint center,
                                                          double xRange, double yRange, double zRange) {
        return () -> new CartesianPoint(
                uniformDouble(-xRange, xRange),
                uniformDouble(-yRange, yRange),
                uniformDouble(-zRange, zRange))
                .shift(center);
    }
}