package com.private_void.core.fluxes;

import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.Vector;
import com.private_void.core.particles.Particle;
import com.private_void.core.particles.ParticleFactory;
import com.private_void.core.geometry.CoordinateFactory;

import java.util.List;

public abstract class Flux {
    protected ParticleFactory particleFactory;
    protected CoordinateFactory coordinateFactory;
    protected CartesianPoint fluxCoordinate;
    protected Vector fluxAxis;
    protected int particlesAmount;
    protected float minIntensity;
    protected List<? extends Particle> particles;

    protected Flux(final ParticleFactory particleFactory, final CoordinateFactory coordinateFactory,
                   final CartesianPoint fluxCoordinate, final Vector fluxAxis,
                   int particlesAmount, float minIntensity) {
        this.particleFactory = particleFactory;
        this.coordinateFactory = coordinateFactory;
        this.fluxCoordinate = fluxCoordinate;
        this.fluxAxis = fluxAxis;
        this.particlesAmount = particlesAmount;
        this.minIntensity = minIntensity;
    }

    protected abstract void createParticles();

    public List<? extends Particle> getParticles() {
        return particles;
    }

    public void setParticles(List<? extends Particle> newParticles) {
        particles = newParticles;
    }

    public float getMinIntensity() {
        return minIntensity;
    }

    public void setMinIntensity(float minIntensity) {
        this.minIntensity = minIntensity;
    }
}