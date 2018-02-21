package com.private_void.core.particles;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;

import static com.private_void.utils.Constants.RECURSIVE_ITERATIONS_MAX;

public class NeutralParticle extends Particle {
    private float intensity;
    private int recursiveIterationCount;

    private NeutralParticle(final Point3D coordinate, final Vector3D speed, float intensity) {
        super(coordinate, speed);
        this.intensity = intensity;
        this.recursiveIterationCount = 1;
    }

    public float getIntensity() {
        return intensity;
    }

    public void decreaseIntensity(float reflectivity) {
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

    public static ParticleFactory getFactory(float intensity) {
        return (final Point3D coordinate, final Vector3D speed) -> new NeutralParticle(coordinate, speed, intensity);
    }
}