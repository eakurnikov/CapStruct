package com.private_void.core.detectors;

import com.private_void.app.Logger;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.particles.Particle;

import java.util.ArrayList;
import java.util.List;

public class Detector {
    protected final CartesianPoint center;
    protected final double width;
    protected double upperBound;
    protected double lowerBound;

    protected int detectedAmount;
    protected int absorbedAmount;
    protected int outOfCapillarsAmount;
    protected int outOfDetectorAmount;
    protected int deletedAmount;

    private double L;

    public Detector(final CartesianPoint center, double width) {
        this.center = center;
        this.L = center.getX();
        this.width = width;
        this.upperBound = width / 2;
        this.lowerBound = -width / 2;
    }

    private void init() {
        detectedAmount = 0;
        outOfDetectorAmount = 0;
        absorbedAmount = 0;
        outOfCapillarsAmount = 0;
        deletedAmount = 0;
    }

    public void detect(Flux flux) {
        Logger.detectingParticlesStart();

        init();
        List<Particle> detectedParticles = new ArrayList<>();

        for (Particle particle : flux.getParticles()) {

            if (!particle.isDeleted()) {
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
            } else {
                deletedAmount++;
            }
        }

        computeScatter(detectedParticles);
        flux.setParticles(detectedParticles);

        Logger.detectingParticlesFinish();
    }

    protected CartesianPoint getCoordinateOnDetector(Particle p) {
        double x = p.getCoordinate().getX();
        double y = p.getCoordinate().getY();
        double z = p.getCoordinate().getZ();

        double Vx = p.getSpeed().getX();
        double Vy = p.getSpeed().getY();
        double Vz = p.getSpeed().getZ();

        return new CartesianPoint(L, (Vy / Vx) * (L - x) + y, (Vz / Vx) * (L - x) + z);
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

    public CartesianPoint getCenter() {
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

    public int getDeletedAmount() {
        return deletedAmount;
    }
}