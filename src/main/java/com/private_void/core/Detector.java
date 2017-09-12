package com.private_void.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Detector {
    protected Point3D centerCoordinate;
//    protected Point3D leftBottomPoint;

//    protected int cellsAmount;
    protected int detectedParticlesAmount;
    protected int absorbedParticlesAmount;
    protected int outOfCapillarParticlesAmount;

    protected float detectedIntensity;
    protected float absorbedIntensity;
    protected float outOfCapillarIntensity;

    protected float width;
//    protected float cellSize;
    protected double upperBound;
    protected double lowerBound;

    protected float x;
    protected float y;
    protected float z;

    protected float Vx;
    protected float Vy;
    protected float Vz;

    public Detector(final Point3D centerCoordinate, float width, float cellSize) {
        this.centerCoordinate = centerCoordinate;
        this.width = width;
//        this.cellSize = cellSize;
        this.upperBound = width / 2;
        this.lowerBound = -width / 2;
        this.detectedParticlesAmount = 0;
        this.detectedIntensity = 0.0f;

//        this.leftBottomPoint = new Point3D(centerCoordinate.getX(), centerCoordinate.getY() - width, centerCoordinate.getZ() - width);
//        this.cellsAmount = (int) (2.0f * width / cellSize);
    }

    public void detect(Flux flux) {
        float L = centerCoordinate.getX();
        try {
            Iterator<Particle> iterator = flux.getParticles().iterator();
            Particle particle;

            while (iterator.hasNext()) {
                particle = iterator.next();

                if (!particle.isAbsorbed() && particle.getIntensity() > flux.getMinIntensity()) {
                    x = particle.getCoordinate().getX();
                    y = particle.getCoordinate().getY();
                    z = particle.getCoordinate().getZ();

                    Vx = particle.getSpeed().getX();
                    Vy = particle.getSpeed().getY();
                    Vz = particle.getSpeed().getZ();

                    particle.setCoordinate(L, (Vy / Vx) * (L - x) + y, (Vz / Vx) * (L - x) + z);

                    if (Math.abs(particle.getCoordinate().getY()) > width / 2 || Math.abs(particle.getCoordinate().getZ()) > width / 2) {
                        iterator.remove();
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
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        computeScatter(flux.getParticles());
    }

    protected void computeScatter(List<Particle> particles) {
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

    public float getDetectedIntensity() {
        return detectedIntensity;
    }

    public float getAbsorbedIntensity() {
        return absorbedIntensity;
    }

    public float getOutOfCapillarIntensity() {
        return outOfCapillarIntensity;
    }

    public void increaseOutOfCapillarParticlesAmount() {
        outOfCapillarParticlesAmount++;
    }

    public void increaseOutOfCapillarInensity(float intensity) {
        outOfCapillarIntensity += intensity;
    }
}