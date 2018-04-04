package com.private_void.core.particles;

import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.vectors.Vector;

import static com.private_void.utils.Constants.RECURSIVE_ITERATIONS_MAX;

public class NeutralParticle extends Particle {
    private double intensity;
    private int recursiveIterationCount;

    private NeutralParticle(final CartesianPoint coordinate, final Vector speed, double intensity) {
        super(coordinate, speed);
        this.intensity = intensity;
        this.recursiveIterationCount = 1;
    }

    public double getIntensity() {
        return intensity;
    }

    public void decreaseIntensity(double reflectivity) {
        intensity *= reflectivity;
    }

    public boolean isRecursiveIterationsLimitReached() {
        return recursiveIterationCount == RECURSIVE_ITERATIONS_MAX;
    }

    public void recursiveIteration() {
        this.recursiveIterationCount++;
    }

    public int getRecursiveIterationCount() {
        return recursiveIterationCount;
    }

    public void stopRecursiveIterations() {
        recursiveIterationCount = 1;
    }

    public static Particle.Factory getFactory(double intensity) {
        return (final CartesianPoint coordinate, final Vector speed) -> new NeutralParticle(coordinate, speed, intensity);
    }
}