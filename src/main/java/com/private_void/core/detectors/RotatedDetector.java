package com.private_void.core.detectors;

import com.private_void.core.particles.Particle;
import com.private_void.core.geometry.Point3D;
import com.private_void.utils.Utils;

public class RotatedDetector extends Detector {
    private float angleR;

    public RotatedDetector(final Point3D centerCoordinate, float width, float cellSize, float angleD) {
        super(centerCoordinate, width, cellSize);
        this.angleR = Utils.convertDegreesToRads(angleD);
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