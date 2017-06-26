package com.private_void.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

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

    protected float[][] cells;
    protected int cellIndexZ;
    protected int cellIndexY;

    //TODO частицы вообще все поглотились, они не на детекторе.

    public Detector(final Point3D centerCoordinate, float width, float cellSize) {
        this.centerCoordinate = centerCoordinate;
        this.width = width;
        this.cellSize = cellSize;
        this.detectedParticlesAmount = 0;
        this.detectedIntensity = 0.0f;

        this.leftBottomPoint = new Point3D(centerCoordinate.getX(), centerCoordinate.getY() - width, centerCoordinate.getZ() - width);
        this.cellsAmount = (int) (2.0f * width / cellSize);
        this.cells = new float[cellsAmount][cellsAmount];

        for(float[] line : cells) {
            Arrays.fill(line, 0.0f);
        }
    }

    public Flux detect(Flux flux) {
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

                    cellIndexZ = (int) (Math.ceil((particle.getCoordinate().getZ() - leftBottomPoint.getZ()) / cellSize));
                    cellIndexY = (int) (Math.ceil((particle.getCoordinate().getY() - leftBottomPoint.getY()) / cellSize));
                    cells[cellIndexZ][cellIndexY] += particle.getIntensity();
                    detectedParticlesAmount++;
                    detectedIntensity += particle.getIntensity();
                }
            }
        }
        catch (Exception ex){
            //TODO increase not detected Intensity
            notDetectedParticlesAmount++;
            System.out.println(ex.getMessage());
        }

//        Collections.sort(flux.particles, (Particle o1, Particle o2) -> {
//            if (o1.getIntensity() > o2.getIntensity())
//                return 1;
//            else if (o1.getIntensity() < o2.getIntensity())
//                return -1;
//            else  return 0;
//        });
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