package com.private_void.core;

import java.util.ArrayList;

public abstract class Flux {
    protected Point3D fluxCoordinate;
    protected Vector3D fluxAxis;
    protected int particlesAmount;
    protected float minIntensity;
    protected double upperBound;
    protected double lowerBound;
    protected ArrayList<Particle> particles;

    protected Flux(final Point3D fluxCoordinate, final Vector3D fluxAxis, int particlesAmount, float minIntensity) {
        this.fluxCoordinate = fluxCoordinate;
        this.fluxAxis = fluxAxis;
        this.particlesAmount = particlesAmount;
        this.minIntensity = minIntensity;
        this.particles = new ArrayList<>();

        checkParameters();
    }

    protected abstract void createParticles();

    public ArrayList<Particle> getParticles() {
        return particles;
    }

    public void setParticles(ArrayList<Particle> newParticles) {
        particles = newParticles;
    }

    public float getMinIntensity() {
        return minIntensity;
    }

    public void setMinIntensity(float minIntensity) {
        this.minIntensity = minIntensity;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public double getLowerBound() {
        return lowerBound;
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

    public void computeScatter(float maxWidth) {
        for (Particle particle : particles) {
            if (particle.getCoordinate().getX() > upperBound) {
                upperBound = particle.getCoordinate().getX();
            }
            if (particle.getCoordinate().getY() > upperBound) {
                upperBound = particle.getCoordinate().getY();
            }
            if (particle.getCoordinate().getX() < lowerBound) {
                lowerBound = particle.getCoordinate().getX();
            }
            if (particle.getCoordinate().getY() < lowerBound) {
                lowerBound = particle.getCoordinate().getY();
            }
        }
        if (upperBound > maxWidth) {
            upperBound = maxWidth;
        }
        if (lowerBound < -maxWidth) {
            lowerBound = -maxWidth;
        }
    }
}