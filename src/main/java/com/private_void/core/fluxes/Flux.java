package com.private_void.core.fluxes;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.Particle;
import com.private_void.core.particles.ParticleFactory;

import java.util.LinkedList;
import java.util.List;

public abstract class Flux {
    protected ParticleFactory particleFactory;
    protected Point3D fluxCoordinate;
    protected Vector3D fluxAxis;
    protected int particlesAmount;
    protected float minIntensity;
    protected List<? extends Particle> particles;

    protected Flux(ParticleFactory factory, final Point3D fluxCoordinate, final Vector3D fluxAxis, int particlesAmount, float minIntensity) {
        this.particleFactory = factory;
        this.fluxCoordinate = fluxCoordinate;
        this.fluxAxis = fluxAxis;
        this.particlesAmount = particlesAmount;
        this.minIntensity = minIntensity;
        this.particles = new LinkedList<>();
        checkParameters();
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

    protected void checkParameters() {
        if (fluxAxis.getX() == 0.0f) {
            fluxAxis.setX(0.000001f);
        }
        if (fluxAxis.getY() == 0.0f) {
            fluxAxis.setY(0.000001f);
        }
        if (fluxAxis.getZ() == 0.0f) {
            fluxAxis.setZ(0.000001f);
        }
    }
}