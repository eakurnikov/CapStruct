package com.private_void.core.detectors;

import com.private_void.core.geometry.Vector3D;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.particles.Particle;

public class RotatedDetector extends Detector {
    private float angleR;
    private Vector3D normal;

    public RotatedDetector(final Point3D centerCoordinate, float width, float angleR) {
        super(centerCoordinate, width);
        this.angleR = angleR;
        this.normal = new Vector3D(1.0f, 0.0f, 0.0f).turnAroundOY(angleR);
    }

    @Override
    protected Point3D getCoordinateOnDetector(Particle p) {
        float x = p.getCoordinate().getX();
        float y = p.getCoordinate().getY();
        float z = p.getCoordinate().getZ();

        float Vx = p.getSpeed().getX();
        float Vy = p.getSpeed().getY();
        float Vz = p.getSpeed().getZ();

        float temp = (normal.getX() * (centerCoordinate.getX() - x) + normal.getY() * (centerCoordinate.getY() - y) + normal.getZ() * (centerCoordinate.getZ() - z)) /
                     (normal.getX() * Vx + normal.getY() * Vy + normal.getZ() * Vz);

        return new Point3D(x + Vx * temp, y + Vy * temp, z + Vz * temp);
    }

    @Override
    protected boolean isParticleWithinBorders(Particle p) {
        Point3D rotatedCoordinate = p.getProjection(-angleR);
        return rotatedCoordinate.getY() * rotatedCoordinate.getY() +
               (centerCoordinate.getZ() + rotatedCoordinate.getZ()) * (centerCoordinate.getZ() + rotatedCoordinate.getZ()) <= (width / 2) * (width / 2);
    }

    public float getAngle() {
        return angleR;
    }
}