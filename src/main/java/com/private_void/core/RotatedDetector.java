package com.private_void.core;

import com.private_void.utils.Utils;

public class RotatedDetector extends Detector {
    private float angleR;

    public RotatedDetector(final Point3D centerCoordinate, float width, float cellSize, float angleD) {
        super(centerCoordinate, width, cellSize);
        this.angleR = Utils.convertDegreesToRads(angleD);
    }

    @Override
    public Flux detect(Flux flux) {
        float tanR = (float) Math.tan(angleR);
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

                    particle.setCoordinate(tanR * (x * Vz - z * Vx - L * Vz) / (tanR * Vz - Vx) + L,
                                           (Vy / Vz) * ((x * Vz - z * Vx - L * Vz) / (tanR * Vz - Vx) - z) + y,
                                           (x * Vz - z * Vx - L * Vz) / (tanR * Vz - Vx));

                    cellIndexZ = (int) (Math.ceil((particle.getCoordinate().getZ() - leftBottomPoint.getZ()) / cellSize));
                    cellIndexY = (int) (Math.ceil((particle.getCoordinate().getY() - leftBottomPoint.getY()) / cellSize));
                    cells[cellIndexZ][cellIndexY] += particle.getIntensity();
                    detectedParticlesAmount++;
                }
            }
        }
        catch (Exception ex){
            notDetectedParticlesAmount++;
            System.out.println(ex.getMessage());
        }
        return flux;
    }
}