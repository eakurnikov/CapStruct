package com.private_void.utils;

import com.private_void.core.Point3D;

import java.util.Random;

// TODO: сделать синглтоном Билла Пью или вообще убрать и пользоваться nextGaussian()
public class RandomNumberGenerator {

    private static RandomNumberGenerator instance;
    private Random rand;

    private RandomNumberGenerator() {
        rand = new Random();
    }

    public static RandomNumberGenerator getInstance() {
        if (instance == null) {
            instance = new RandomNumberGenerator();
        }
        return instance;
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

}