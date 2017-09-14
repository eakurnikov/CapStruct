package com.private_void.core;

import com.private_void.utils.Utils;

import java.util.Iterator;

public class RotatedDetector extends Detector {
    private float angleR;

    public RotatedDetector(final Point3D centerCoordinate, float width, float cellSize, float angleD) {
        super(centerCoordinate, width, cellSize);
        this.angleR = Utils.convertDegreesToRads(angleD);
    }

    @Override
    public void detect(Flux flux) {
        float tanR = (float) Math.tan(angleR);
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

                    particle.setCoordinate(tanR * (x * Vz - z * Vx - L * Vz) / (tanR * Vz - Vx) + L,
                                           (Vy / Vz) * ((x * Vz - z * Vx - L * Vz) / (tanR * Vz - Vx) - z) + y,
                                           (x * Vz - z * Vx - L * Vz) / (tanR * Vz - Vx));

                    if (Math.abs(particle.getCoordinate().getY()) > width / 2 || Math.abs(particle.getCoordinate().getZ()) > width / 2) {
                        outOfDetectorParticlesAmount++;
                        outOfDetectorIntensity += particle.getIntensity();
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
}