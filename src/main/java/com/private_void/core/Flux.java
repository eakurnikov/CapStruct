package com.private_void.core;

import java.util.ArrayList;

public abstract class Flux {

    protected Point3D fluxCoordinate;
    protected Vector3D fluxAxis;
    protected int particlesAmount;
    protected float minIntensity;
    protected ArrayList<Particle> particles;

    protected Flux(final Point3D fluxCoordinate, final Vector3D fluxAxis, int particlesAmount, float minIntensity) {
        this.fluxCoordinate = fluxCoordinate;
        this.fluxAxis = fluxAxis;
        this.particlesAmount = particlesAmount;
        this.minIntensity = minIntensity;
    }

    protected abstract void createParticles();

    public ArrayList<Particle> getParticles() {
        return particles;
    }

    public float getMinIntensity() {
        return minIntensity;
    }

    public void setMinIntensity(float minIntensity) {
        this.minIntensity = minIntensity;
    }
}