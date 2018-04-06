package com.private_void.core.surfaces.atomic_surfaces.atomic_capillars;

import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.vectors.Vector;
import com.private_void.core.particles.Atom;
import com.private_void.core.particles.ChargedParticle;
import com.private_void.core.surfaces.Capillar;

import java.util.ArrayList;

public class AtomicCylinder extends AtomicCapillar {

    public AtomicCylinder(final Atom.Factory atomFactory, final CartesianPoint front, double period, double chargeNumber,
                          double radius, double length) {
        super(atomFactory, front, period, chargeNumber, radius, length);
    }

    @Override
    protected Vector getNormal(final CartesianPoint point) {
        return null;
    }

    @Override
    protected Vector getParticleSpeedRotationAxis(final CartesianPoint point, final Vector normal) {
        return null;
    }

    @Override
    protected void createAtoms() {
        atoms = new ArrayList<>();

        double x = front.getX();
        //double y = front.getY();
        //double z = front.getZ() - size / 2;

        while (x <= front.getX() + length) {
            x += period;
        }
    }

    @Override
    protected double getCriticalAngle(final ChargedParticle particle) {
        return 0;
    }

    @Override
    protected Vector getNewSpeed(final ChargedParticle particle) {
        return null;
    }

    @Override
    protected CartesianPoint getNewCoordinate(final ChargedParticle p) {
        return null;
    }

    @Override
    protected boolean isPointInside(CartesianPoint point) {
        return false;
    }

    public static Capillar.Factory getFactory(final Atom.Factory atomFactory, double period, double chargeNumber,
                                             double radius, double length) {
        return new Capillar.Factory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate) {
                return new AtomicCylinder(atomFactory, coordinate, period, chargeNumber, radius, length);
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