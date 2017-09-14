package com.private_void.core.detectors;

import com.private_void.core.fluxes.Flux;
import com.private_void.core.particles.Particle;
import com.private_void.core.geometry.Point3D;
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
        Particle particle;
        Iterator<Particle> iterator = flux.getParticles().iterator();
        while (iterator.hasNext()) {
            particle = iterator.next();
            if (!particle.isAbsorbed() && particle.getIntensity() > flux.getMinIntensity()) {
                particle.setCoordinate(getCoordinateOnDetector(particle));
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
        computeScatter(flux.getParticles());
    }

    @Override
    protected Point3D getCoordinateOnDetector(Particle particle) {
        float tanR = (float) Math.tan(angleR);
        float L = centerCoordinate.getX();

        float x = particle.getCoordinate().getX();
        float y = particle.getCoordinate().getY();
        float z = particle.getCoordinate().getZ();

        float Vx = particle.getSpeed().getX();
        float Vy = particle.getSpeed().getY();
        float Vz = particle.getSpeed().getZ();

        return new Point3D(tanR * (x * Vz - z * Vx - L * Vz) / (tanR * Vz - Vx) + L,
                (Vy / Vz) * ((x * Vz - z * Vx - L * Vz) / (tanR * Vz - Vx) - z) + y,
                (x * Vz - z * Vx - L * Vz) / (tanR * Vz - Vx));
    }
}