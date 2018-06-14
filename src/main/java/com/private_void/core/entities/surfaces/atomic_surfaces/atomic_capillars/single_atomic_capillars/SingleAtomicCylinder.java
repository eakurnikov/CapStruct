package com.private_void.core.entities.surfaces.atomic_surfaces.atomic_capillars.single_atomic_capillars;

import com.private_void.core.entities.detectors.Detector;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;

public class SingleAtomicCylinder extends SingleAtomicCapillar {

    public SingleAtomicCylinder(final CartesianPoint front, double radius, double length, double period,
                                double chargeNumber) {
        super(front, radius, length, period, chargeNumber);
        this.detector = createDetector();
    }

    @Override
    protected Vector getAxis(final CartesianPoint point) {
        return Vector.E_X;
    }

    @Override
    protected CartesianPoint getAcceleration(final CartesianPoint coordinate, double particleChargeNumber, double mass) {
        double y = coordinate.getY();
        double z = coordinate.getZ();
        double r = Math.sqrt(y * y + z * z);

        double F = getForce(r, particleChargeNumber);
        double Fy = F * (y / r);
        double Fz = F * (z / r);

        return new CartesianPoint(0.0, Fy / mass, Fz / mass);
//        return new double[] {Fy / mass, Fz / mass};
    }

    @Override
    protected boolean isPointInside(final CartesianPoint point) {
        return point.getX() <= front.getX() + length;
    }

    @Override
    protected Detector createDetector() {
        return new Detector(
                new CartesianPoint(front.getX() + length, front.getY(), front.getZ()),
                2.0 * radius);
    }
}