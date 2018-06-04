package com.private_void.core.entities.particles;

import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;

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

    public static Particle.Factory getFactory(double intensity) {
        return (final CartesianPoint coordinate, final Vector speed) -> new NeutralParticle(coordinate, speed, intensity);
    }
}