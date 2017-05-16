package com.private_void.core;

import com.private_void.utils.Utils;

import java.util.Arrays;

//TODO: возможно, заменить двумерный массив на какой-то контейнер
//TODO: протестировать ceil, возможно он не будет давать то, что нужно, так как округляет не в меньшую сторону, а окружгляет к ближайшему инту
public class Detector {

    private Point3D centerCoordinate;
    private Point3D leftBottomPoint;
    private float width;
    private float cellSize;
    private float angleD;
    private float angleR;
    private int cellsAmount;
    private int detectedParticlesAmount;
    private int notDetectedParticlesAmount;
    private int outOfCapillarParticlesAmount;
    private float torusRadius;
    private float radius;

    private float x;
    private float y;
    private float z;

    private float Vx;
    private float Vy;
    private float Vz;

    private float[][] cells;

    public Detector(final Point3D centerCoordinate, float width, float cellSize, float angleD, float torusRadius, float radius) {

        this.centerCoordinate = centerCoordinate;
        this.width = width;
        this.cellSize = cellSize;
        this.angleD = angleD;
        this.angleR = Utils.convertDegreesToRads(angleD);
        this.torusRadius = torusRadius;
        this.radius = radius;
        this.detectedParticlesAmount = 0;

        this.leftBottomPoint.setX(centerCoordinate.getX());
        this.leftBottomPoint.setY(centerCoordinate.getY() - width);
        this.leftBottomPoint.setZ(centerCoordinate.getZ() - width);
        this.cellsAmount = (int) (2.0f * width / cellSize);
        this.cells = new float[cellsAmount][cellsAmount];

        Arrays.fill(cells, 0.0f);

    }

    public Detector(final Point3D centerCoordinate, float width, float cellSize, float angleD, float radius) {

        this.centerCoordinate = centerCoordinate;
        this.width = width;
        this.cellSize = cellSize;
        this.angleD = angleD;
        this.angleR = Utils.convertDegreesToRads(angleD);
        this.torusRadius = 0.0f;
        this.radius = radius;
        this.detectedParticlesAmount = 0;

        this.leftBottomPoint.setX(centerCoordinate.getX());
        this.leftBottomPoint.setY(centerCoordinate.getY() - width);
        this.leftBottomPoint.setZ(centerCoordinate.getZ() - width);
        this.cellsAmount = (int) (2.0f * width / cellSize);
        this.cells = new float[cellsAmount][cellsAmount];

        Arrays.fill(cells, 0.0f);

    }

    //TODO это сработает только для тора, сделать еще метод для цилиндра или переосмыслить логику и сделать униварсальный метод detect
    public void detect(Flux flux) {

        float sinR = (float) Math.sin(angleR);
        float tanR = (float) Math.tan(angleR);
        float rR = torusRadius + radius;

        try {

            for (Particle particle : flux.getParticles()) {

                if (!particle.isAbsorbed() && particle.getIntensity() > flux.getMinIntensity()) {

                    x = particle.getCoordinate().getX();
                    y = particle.getCoordinate().getY();
                    z = particle.getCoordinate().getZ();

                    Vx = particle.getSpeed().getX();
                    Vy = particle.getSpeed().getY();
                    Vz = particle.getSpeed().getZ();

                    particle.setCoordinate(tanR * (x * Vz - z * Vx - rR * sinR * Vz) / (tanR * Vz - Vx) + rR * sinR,
                                           (Vy / Vz) * ((x * Vz - z * Vx - rR * sinR * Vz) / (tanR * Vz - Vx) - z) + y,
                                           (x * Vz - z * Vx - rR * sinR * Vz) / (tanR * Vz - Vx));

                    cells[(int) (Math.ceil((particle.getCoordinate().getZ() - leftBottomPoint.getZ()) / cellSize))][(int) (Math.ceil((particle.getCoordinate().getY() - leftBottomPoint.getY()) / cellSize))] += particle.getIntensity();
                    detectedParticlesAmount++;

                }

            }

        }
        catch (Exception ex){
            notDetectedParticlesAmount++;
            System.out.println(ex.getMessage());
        }

    }

    public int getDetectedParticlesAmount() {
        return detectedParticlesAmount;
    }

    public void setDetectedParticlesAmount(int detectedParticlesAmount) {
        this.detectedParticlesAmount = detectedParticlesAmount;
    }

    public int getNotDetectedParticlesAmount() {
        return notDetectedParticlesAmount;
    }

    public void setNotDetectedParticlesAmount(int notDetectedParticlesAmount) {
        this.notDetectedParticlesAmount = notDetectedParticlesAmount;
    }

    public int getOutOfCapillarParticlesAmount() {
        return outOfCapillarParticlesAmount;
    }

    public void setOutOfCapillarParticlesAmount(int outOfCapillarParticlesAmount) {
        this.outOfCapillarParticlesAmount = outOfCapillarParticlesAmount;
    }

    public void increaseOutOfCapillarParticlesAmount() {
        outOfCapillarParticlesAmount++;
    }

}

//particle.setCoordinate(tanR * (x * Vz - z * Vx - rR * sinR * Vz) / (tanR * Vz - Vx) + rR * sinR, (Vy / Vz) * ((x * Vz - z * Vx - rR * sinR * Vz) / (tanR * Vz - Vx) - z) + y, (x * Vz - z * Vx - rR * sinR * Vz) / (tanR * Vz - Vx));
//Ray[i, j].SetCoordinate(Math.Tan(torus_angle) * (Ray[i, j].coordinate[0] * Ray[i, j].speed[2] - Ray[i, j].coordinate[2] * Ray[i, j].speed[0] - (L) * Ray[i, j].speed[2]) / (Math.Tan(torus_angle) * Ray[i, j].speed[2] - Ray[i, j].speed[0]) + (L), (Ray[i, j].speed[1] / Ray[i, j].speed[2]) * ((Ray[i, j].coordinate[0] * Ray[i, j].speed[2] - Ray[i, j].coordinate[2] * Ray[i, j].speed[0] - (L) * Ray[i, j].speed[2]) / (Math.Tan(torus_angle) * Ray[i, j].speed[2] - Ray[i, j].speed[0]) - Ray[i, j].coordinate[2]) + Ray[i, j].coordinate[1], (Ray[i, j].coordinate[0] * Ray[i, j].speed[2] - Ray[i, j].coordinate[2] * Ray[i, j].speed[0] - (L) * Ray[i, j].speed[2]) / (Math.Tan(torus_angle) * Ray[i, j].speed[2] - Ray[i, j].speed[0]));
//Ray[i, j].SetCoordinate(Math.Tan(torus_angle) * (Ray[i, j].coordinate[0] * Ray[i, j].speed[2] - Ray[i, j].coordinate[2] * Ray[i, j].speed[0] - (R + r) * Math.Sin(torus_angle) * Ray[i, j].speed[2]) / (Math.Tan(torus_angle) * Ray[i, j].speed[2] - Ray[i, j].speed[0]) + (R + r) * Math.Sin(torus_angle), (Ray[i, j].speed[1] / Ray[i, j].speed[2]) * ((Ray[i, j].coordinate[0] * Ray[i, j].speed[2] - Ray[i, j].coordinate[2] * Ray[i, j].speed[0] - (R + r) * Math.Sin(torus_angle) * Ray[i, j].speed[2]) / (Math.Tan(torus_angle) * Ray[i, j].speed[2] - Ray[i, j].speed[0]) - Ray[i, j].coordinate[2]) + Ray[i, j].coordinate[1], (Ray[i, j].coordinate[0] * Ray[i, j].speed[2] - Ray[i, j].coordinate[2] * Ray[i, j].speed[0] - (R + r) * Math.Sin(torus_angle) * Ray[i, j].speed[2]) / (Math.Tan(torus_angle) * Ray[i, j].speed[2] - Ray[i, j].speed[0]));