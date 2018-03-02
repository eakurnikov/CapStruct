package com.private_void.core.detectors;

import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.particles.Particle;

import java.util.ArrayList;
import java.util.List;

public class Detector {
    protected Point3D center;
    protected float width;
    protected double upperBound;
    protected double lowerBound;

    protected int detectedAmount;
    protected int absorbedAmount;
    protected int outOfCapillarsAmount;
    protected int outOfDetectorAmount;

    private float L;

    public Detector(final Point3D center, float width) {
        this.center = center;
        this.L = center.getX();
        this.width = width;
        this.upperBound = width / 2;
        this.lowerBound = -width / 2;
        init();
    }

    private void init() {
        detectedAmount = 0;
        outOfDetectorAmount = 0;
        absorbedAmount = 0;
        outOfCapillarsAmount = 0;
    }

    public void detect(Flux flux) {
        init();

        List<Particle> detectedParticles = new ArrayList<>();

        for (Particle particle : flux.getParticles()) {

            if (!particle.isOut()) {

                if (!particle.isAbsorbed()) {
                    particle.setCoordinate(getCoordinateOnDetector(particle));

                    if (isParticleWithinBorders(particle)) {
                        detectedAmount++;
                        detectedParticles.add(particle);
                    } else {
                        outOfDetectorAmount++;
                    }
                } else {
                    absorbedAmount++;
                }
            } else {
                outOfCapillarsAmount++;
            }
        }

        computeScatter(detectedParticles);
        flux.setParticles(detectedParticles);
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
        return  p.getCoordinate().getY() > center.getY() - width / 2 &&
                p.getCoordinate().getY() < center.getY() + width / 2 &&
                p.getCoordinate().getZ() > center.getZ() - width / 2 &&
                p.getCoordinate().getZ() < center.getZ() + width / 2;
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
//        if (upperBound > width / 2) {
//            upperBound = width / 2;
//        }
//        if (lowerBound < -width / 2) {
//            lowerBound = -width / 2;
//        }
    }

    public Point3D getCenter() {
        return center;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public double getLowerBound() {
        return lowerBound;
    }


    public int getDetectedAmount() {
        return detectedAmount;
    }

    public int getAbsorbedAmount() {
        return absorbedAmount;
    }

    public int getOutOfCapillarsAmount() {
        return outOfCapillarsAmount;
    }

    public int getOutOfDetectorAmount() {
        return outOfDetectorAmount;
    }
}

//    protected float detectedIntensity;
//    protected float absorbedIntensity;
//    protected float outOfCapillarIntensity;
//    protected float outOfDetectorIntensity;
//
//    public float getDetectedIntensity() {
//        return detectedIntensity;
//    }
//
//    public float getAbsorbedIntensity() {
//        return absorbedIntensity;
//    }
//
//    public float getOutOfCapillarIntensity() {
//        return outOfCapillarIntensity;
//    }
//
//    public float getOutOfDetectorIntensity() {
//        return outOfDetectorIntensity;
//    }
//
//    public void increaseOutOfCapillarIntensity(float intensity) {
//        outOfCapillarIntensity += intensity;
//    }
//
//    public void increaseAbsorbedIntensity(float intensity) {
//        absorbedIntensity += intensity;
//    }