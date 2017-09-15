package com.private_void.core.detectors;

import com.private_void.core.particles.Particle;
import com.private_void.core.geometry.Point3D;
import com.private_void.utils.Utils;

public class RotatedDetector extends Detector {
    private float tgAngleR;

    public RotatedDetector(final Point3D centerCoordinate, float width, float cellSize, float angleD) {
        super(centerCoordinate, width, cellSize);
        this.tgAngleR = (float) Math.tan(Utils.convertDegreesToRads(angleD));
    }

    @Override
    protected Point3D getCoordinateOnDetector(Particle particle) {
        float x = particle.getCoordinate().getX();
        float y = particle.getCoordinate().getY();
        float z = particle.getCoordinate().getZ();

        float Vx = particle.getSpeed().getX();
        float Vy = particle.getSpeed().getY();
        float Vz = particle.getSpeed().getZ();

        return new Point3D(tgAngleR * (x * Vz - z * Vx - L * Vz) / (tgAngleR * Vz - Vx) + L,
                (Vy / Vz) * ((x * Vz - z * Vx - L * Vz) / (tgAngleR * Vz - Vx) - z) + y,
                (x * Vz - z * Vx - L * Vz) / (tgAngleR * Vz - Vx));
    }
}