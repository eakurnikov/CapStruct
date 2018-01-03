package com.private_void.core.detectors;

import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.particles.Particle;

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
        Particle p;
        Iterator<? extends Particle> iterator = flux.getParticles().iterator();

        while (iterator.hasNext()) {
            p = iterator.next();

            if (!p.isAbsorbed()) {
                p.setCoordinate(getCoordinateOnDetector(p));

                if (!isParticleWithinBorders(p)) {
                    outOfDetectorParticlesAmount++;
                    outOfDetectorIntensity += p.getIntensity();
//                    iterator.remove();
                } else {
                    detectedParticlesAmount++;
                    detectedIntensity += p.getIntensity();
                }
            } else {
                absorbedParticlesAmount++;
                absorbedIntensity += p.getIntensity();
                iterator.remove();
            }
        }
        computeScatter(flux.getParticles());
    }

    protected Point3D getCoordinateOnDetector(Particle p) {
        float x = p.getCoordinate().getX();
        float y = p.getCoordinate().getY();
        float z = p.getCoordinate().getZ();

        float Vx = p.getSpeed().getX();
        float Vy = p.getSpeed().getY();
        float Vz = p.getSpeed().getZ();

        return new Point3D(L, (Vy / Vx) * (L - x) + y, (Vz / Vx) * (L - x) + z);
    }

    protected boolean isParticleWithinBorders(Particle p) {
        return p.getCoordinate().getY() * p.getCoordinate().getY() +
               p.getCoordinate().getZ() * p.getCoordinate().getZ() <= (width / 2) * (width / 2);
    }

    protected void computeScatter(List<? extends Particle> particles) {
        for (Particle p : particles) {
            if (p.getCoordinate().getY() > upperBound) {
                upperBound = p.getCoordinate().getY();
            }
            if (p.getCoordinate().getZ() > upperBound) {
                upperBound = p.getCoordinate().getZ();
            }
            if (p.getCoordinate().getY() < lowerBound) {
                lowerBound = p.getCoordinate().getY();
            }
            if (p.getCoordinate().getZ() < lowerBound) {
                lowerBound = p.getCoordinate().getZ();
            }
        }
        if (upperBound > width / 2) {
            upperBound = width / 2;
        }
        if (lowerBound < -width / 2) {
            lowerBound = -width / 2;
        }
    }

    public Point3D getCenterCoordinate() {
        return centerCoordinate;
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

    public void increaseOutOfCapillarIntensity(float intensity) {
        outOfCapillarIntensity += intensity;
    }

    public void increaseAbsorbedIntensity(float intensity) {
        absorbedIntensity += intensity;
    }

    public void increaseAbsorbedParticlesAmount() {
        absorbedParticlesAmount++;
    }
}