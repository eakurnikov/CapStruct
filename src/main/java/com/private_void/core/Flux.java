package com.private_void.core;

import java.util.LinkedList;
import java.util.List;

public abstract class Flux {
    protected Point3D fluxCoordinate;
    protected Vector3D fluxAxis;
    protected int particlesAmount;
    protected float minIntensity;
    protected List<Particle> particles;

    protected Flux(final Point3D fluxCoordinate, final Vector3D fluxAxis, int particlesAmount, float minIntensity) {
        this.fluxCoordinate = fluxCoordinate;
        this.fluxAxis = fluxAxis;
        this.particlesAmount = particlesAmount;
        this.minIntensity = minIntensity;
        this.particles = new LinkedList<>();
        checkParameters();
    }

    protected abstract void createParticles();

    public List<Particle> getParticles() {
        return particles;
    }

    public void setParticles(List<Particle> newParticles) {
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