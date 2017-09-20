package com.private_void.core.detectors;

import com.private_void.core.fluxes.Flux;
import com.private_void.core.particles.Particle;
import com.private_void.core.geometry.Point3D;

import java.util.Iterator;
import java.util.List;

public class Detector {
    protected Point3D centerCoordinate;

    private float L;
    protected float width;
    protected double upperBound;
    protected double lowerBound;

    protected int detectedParticlesAmount;
    protected int absorbedParticlesAmount;
    protected int outOfCapillarParticlesAmount;
    protected int outOfDetectorParticlesAmount;

    protected float detectedIntensity;
    protected float absorbedIntensity;
    protected float outOfCapillarIntensity;
    protected float outOfDetectorIntensity;

    public Detector(final Point3D centerCoordinate, float width) {
        this.centerCoordinate = centerCoordinate;
        this.L = centerCoordinate.getX();
        this.width = width;
        this.upperBound = width / 2;
        this.lowerBound = -width / 2;
        this.detectedParticlesAmount = 0;
        this.detectedIntensity = 0.0f;
    }

    public void detect(Flux flux) {
        Particle particle;
        Iterator<Particle> iterator = flux.getParticles().iterator();
        while (iterator.hasNext()) {
            particle = iterator.next();
            if (!particle.isAbsorbed() && particle.getIntensity() > flux.getMinIntensity()) {
                particle.setCoordinate(getCoordinateOnDetector(particle));
                if (!isParticleWithinBorders(particle)) {
                    outOfDetectorParticlesAmount++;
                    outOfDetectorIntensity += particle.getIntensity();
//                    iterator.remove();
                } else {
                    detectedParticlesAmount++;
                    detectedIntensity += particle.getIntensity();
                }
            } else {
                absorbedParticlesAmount++;
                absorbedIntensity += particle.getIntensity();
                iterator.remove();
            }
        }
        computeScatter(flux.getParticles());
    }

    protected Point3D getCoordinateOnDetector(Particle particle) {
        float x = particle.getCoordinate().getX();
        float y = particle.getCoordinate().getY();
        float z = particle.getCoordinate().getZ();

        float Vx = particle.getSpeed().getX();
        float Vy = particle.getSpeed().getY();
        float Vz = particle.getSpeed().getZ();

        return new Point3D(L, (Vy / Vx) * (L - x) + y, (Vz / Vx) * (L - x) + z);
    }

    protected boolean isParticleWithinBorders(Particle particle) {
        return particle.getCoordinate().getY() * particle.getCoordinate().getY() +
               particle.getCoordinate().getZ() * particle.getCoordinate().getZ() <= (width / 2) * (width / 2);
    }

    protected void computeScatter(List<Particle> particles) {
        for (Particle particle : particles) {
            if (particle.getCoordinate().getY() > upperBound) {
                upperBound = particle.getCoordinate().getY();
            }
            if (particle.getCoordinate().getZ() > upperBound) {
                upperBound = particle.getCoordinate().getZ();
            }
            if (particle.getCoordinate().getY() < lowerBound) {
                lowerBound = particle.getCoordinate().getY();
            }
            if (particle.getCoordinate().getZ() < lowerBound) {
                lowerBound = particle.getCoordinate().getZ();
            }
        }
        if (upperBound > width / 2) {
            upperBound = width / 2;
        }
        if (lowerBound < -width / 2) {
            lowerBound = -width / 2;
        }
    }

    public double getUpperBound() {
        return upperBound;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public int getDetectedParticlesAmount() {
        return detectedParticlesAmount;
    }

    public int getAbsorbedParticlesAmount() {
        return absorbedParticlesAmount;
    }

    public int getOutOfCapillarParticlesAmount() {
        return outOfCapillarParticlesAmount;
    }

    public int getOutOfDetectorParticlesAmount() {
        return outOfDetectorParticlesAmount;
    }

    public float getDetectedIntensity() {
        return detectedIntensity;
    }

    public float getAbsorbedIntensity() {
        return absorbedIntensity;
    }

    public float getOutOfCapillarIntensity() {
        return outOfCapillarIntensity;
    }

    public float getOutOfDetectorIntensity() {
        return outOfDetectorIntensity;
    }

    public void increaseOutOfCapillarParticlesAmount() {
        outOfCapillarParticlesAmount++;
    }

    public void increaseOutOfCapillarInensity(float intensity) {
        outOfCapillarIntensity += intensity;
    }
}