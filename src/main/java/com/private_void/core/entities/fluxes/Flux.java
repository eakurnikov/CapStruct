package com.private_void.core.entities.fluxes;

import com.private_void.core.entities.particles.Particle;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;

import java.util.List;

public abstract class Flux {
    protected final Particle.Factory particleFactory;
    protected final CartesianPoint.Factory coordinateFactory;
    protected final CartesianPoint fluxCoordinate;
    protected final Vector fluxAxis;
    protected final int particlesAmount;
    protected final double minIntensity;
    protected List<? extends Particle> particles;

    protected Flux(final Particle.Factory particleFactory, final CartesianPoint.Factory coordinateFactory,
                   final CartesianPoint fluxCoordinate, final Vector fluxAxis,
                   int particlesAmount, double minIntensity) {

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

    public double getMinIntensity() {
        return minIntensity;
    }
}