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

    public Point3D gauss(float mean, float dev) {
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
    }

    public CoordinateFactory getGaussDistributionFactory(float mean, float dev) {
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

    public CoordinateFactory getXPlanarUniformDistributionFactory(float x,
                                                                  float ymin, float ymax,
                                                                  float zmin, float zmax) {
        return () -> new Point3D(x,
                uniformFloat(ymin, ymax),
                uniformFloat(zmin, zmax)
        );
    }

    public CoordinateFactory getVolumeUniformDistributionFactory(float xmin, float xmax,
                                                                 float ymin, float ymax,
                                                                 float zmin, float zmax) {
        return () -> new Point3D(
                uniformFloat(xmin, xmax),
                uniformFloat(ymin, ymax),
                uniformFloat(zmin, zmax)
        );

//        return () -> new Point3D(
//                xmin == xmax ? xmin : uniformFloat(xmin, xmax),
//                ymin == ymax ? ymin : uniformFloat(ymin, ymax),
//                zmin == zmax ? zmin : uniformFloat(zmin, zmax)
//        );
    }
}