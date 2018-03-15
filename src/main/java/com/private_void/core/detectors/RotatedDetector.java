package com.private_void.core.detectors;

import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.Vector;
import com.private_void.core.particles.Particle;

public class RotatedDetector extends Detector {
    private double angleR;
    private Vector normal;

    public RotatedDetector(final CartesianPoint centerCoordinate, double width, double angleR) {
        super(centerCoordinate, width);
        this.angleR = angleR;
        this.normal = new Vector(1.0, 0.0, 0.0).turnAroundOY(angleR);
    }

    @Override
    protected CartesianPoint getCoordinateOnDetector(Particle p) {
        double x = p.getCoordinate().getX();
        double y = p.getCoordinate().getY();
        double z = p.getCoordinate().getZ();

        double Vx = p.getSpeed().getX();
        double Vy = p.getSpeed().getY();
        double Vz = p.getSpeed().getZ();

        double temp = (normal.getX() * (center.getX() - x)
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
                <= (width / 2.0) * (width / 2.0);
    }

    public double getAngle() {
        return angleR;
    }
}