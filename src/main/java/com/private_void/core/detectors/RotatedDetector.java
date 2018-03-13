package com.private_void.core.detectors;

import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.Vector;
import com.private_void.core.particles.Particle;

public class RotatedDetector extends Detector {
    private float angleR;
    private Vector normal;

    public RotatedDetector(final CartesianPoint centerCoordinate, float width, float angleR) {
        super(centerCoordinate, width);
        this.angleR = angleR;
        this.normal = new Vector(1.0f, 0.0f, 0.0f).turnAroundOY(angleR);
    }

    @Override
    protected CartesianPoint getCoordinateOnDetector(Particle p) {
        float x = p.getCoordinate().getX();
        float y = p.getCoordinate().getY();
        float z = p.getCoordinate().getZ();

        float Vx = p.getSpeed().getX();
        float Vy = p.getSpeed().getY();
        float Vz = p.getSpeed().getZ();

        float temp = (normal.getX() * (center.getX() - x)
                    + normal.getY() * (center.getY() - y)
                    + normal.getZ() * (center.getZ() - z))
                / (normal.getX() * Vx + normal.getY() * Vy + normal.getZ() * Vz);

        return new CartesianPoint(x + Vx * temp, y + Vy * temp, z + Vz * temp);
    }

    @Override
    protected boolean isParticleWithinBorders(Particle p) {
        CartesianPoint rotatedCoordinate = p.getProjection(-angleR);
        return rotatedCoordinate.getY() * rotatedCoordinate.getY() +
               (center.getZ() + rotatedCoordinate.getZ()) * (center.getZ() + rotatedCoordinate.getZ())
                <= (width / 2) * (width / 2);
    }

    public float getAngle() {
        return angleR;
    }
}