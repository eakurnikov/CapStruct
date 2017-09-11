package com.private_void.core;

import java.util.ArrayList;

public class Detector {
    protected Point3D centerCoordinate;
    protected Point3D leftBottomPoint;

    protected int cellsAmount;
    protected int detectedParticlesAmount;
    protected int notDetectedParticlesAmount;
    protected int outOfCapillarParticlesAmount;

    protected float detectedIntensity;
    protected float notDetectedIntensity;
    protected float outOfCapillarIntensity;

    protected float width;
    protected float cellSize;

    protected float x;
    protected float y;
    protected float z;

    protected float Vx;
    protected float Vy;
    protected float Vz;

    public Detector(final Point3D centerCoordinate, float width, float cellSize) {
        this.centerCoordinate = centerCoordinate;
        this.width = width;
        this.cellSize = cellSize;
        this.detectedParticlesAmount = 0;
        this.detectedIntensity = 0.0f;

        this.leftBottomPoint = new Point3D(centerCoordinate.getX(), centerCoordinate.getY() - width, centerCoordinate.getZ() - width);
        this.cellsAmount = (int) (2.0f * width / cellSize);
    }

    public Flux detect(Flux flux) {
        ArrayList<Particle> detectedParticles = new ArrayList<>();
        float L = centerCoordinate.getX();
        try {
            for (Particle particle : flux.getParticles()) {
                if (!particle.isAbsorbed() && particle.getIntensity() > flux.getMinIntensity()) {
                    x = particle.getCoordinate().getX();
                    y = particle.getCoordinate().getY();
                    z = particle.getCoordinate().getZ();

                    Vx = particle.getSpeed().getX();
                    Vy = particle.getSpeed().getY();
                    Vz = particle.getSpeed().getZ();

                    particle.setCoordinate(L, (Vy / Vx) * (L - x) + y, (Vz / Vx) * (L - x) + z);

                    detectedParticlesAmount++;
                    detectedIntensity += particle.getIntensity();
                    detectedParticles.add(particle);
                } else {
                    notDetectedParticlesAmount++;
                    notDetectedIntensity += 1;
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        flux.setParticles(detectedParticles);
        flux.computeScatter(width);
        return flux;
    }

//    public int getDetectedParticlesAmount() {
//        return detectedParticlesAmount;
//    }
//
//    public int getNotDetectedParticlesAmount() {
//        return notDetectedParticlesAmount;
//    }
//
//    public int getOutOfCapillarParticlesAmount() {
//        return outOfCapillarParticlesAmount;
//    }

    public void increaseOutOfCapillarParticlesAmount() {
        outOfCapillarParticlesAmount++;
    }

    public void increaseOutOfCapillarInensity(float intensity) {
        outOfCapillarIntensity += intensity;
    }
}