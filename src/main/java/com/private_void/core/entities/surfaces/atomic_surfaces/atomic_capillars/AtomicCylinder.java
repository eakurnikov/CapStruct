package com.private_void.core.entities.surfaces.atomic_surfaces.atomic_capillars;

import com.private_void.core.entities.surfaces.Capillar;
import com.private_void.core.entities.surfaces.capillar_factories.RotatedCylinderFactory;
import com.private_void.core.entities.surfaces.capillar_factories.StraightCapillarFactory;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.reference_frames.ReferenceFrame;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;

public class AtomicCylinder extends AtomicCapillar {

    public AtomicCylinder(final CartesianPoint front, final ReferenceFrame refFrame,
                          double radius, double length, double period, double chargeNumber) {
        super(front, refFrame, radius, length, period, chargeNumber);
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

    public static StraightCapillarFactory getCapillarFactory(double radius, double length, double period,
                                                             double chargeNumber) {
        return new StraightCapillarFactory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate) {
                return new AtomicCylinder(coordinate, ReferenceFrame.builder().atPoint(coordinate).build(),
                          radius, length, period, chargeNumber);
            }

            @Override
            public double getRadius() {
                return radius;
            }

            @Override
            public double getLength() {
                return length;
            }
        };
    }

    public static RotatedCylinderFactory getRotatedCapillarFactory(double radius, double length, double period,
                                                                   double chargeNumber) {
        return new RotatedCylinderFactory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate, final ReferenceFrame refFrame) {
                return new AtomicCylinder(coordinate, refFrame, radius, length, period, chargeNumber);
            }

            @Override
            public double getRadius() {
                return radius;
            }

            @Override
            public double getLength() {
                return length;
            }
        };
    }
}