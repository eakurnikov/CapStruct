package com.private_void.core.detectors;

import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.Particle;
import com.private_void.core.geometry.Point3D;

public class RotatedDetector extends Detector {
    private float angleR;
    private Vector3D normal;

    public RotatedDetector(final Point3D centerCoordinate, float width, float angleR) {
        super(centerCoordinate, width);
        this.angleR = angleR;
        this.normal = new Vector3D(1.0f, 0.0f, 0.0f).turnAroundOY(angleR);
    }

    @Override
    protected Point3D getCoordinateOnDetector(Particle particle) {
        float x = particle.getCoordinate().getX();
        float y = particle.getCoordinate().getY();
        float z = particle.getCoordinate().getZ();

        float Vx = particle.getSpeed().getX();
        float Vy = particle.getSpeed().getY();
        float Vz = particle.getSpeed().getZ();

        float temp = (normal.getX() * (centerCoordinate.getX() - x) + normal.getY() * (centerCoordinate.getY() - y) + normal.getZ() * (centerCoordinate.getZ() - z)) /
                (normal.getX() * Vx + normal.getY() * Vy + normal.getZ() * Vz);

        return new Point3D(x + Vx * temp, y + Vy * temp, z + Vz * temp);
    }

    public float getAngle() {
        return angleR;
    }
}