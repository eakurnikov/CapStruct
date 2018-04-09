package com.private_void.core.surfaces.atomic_surfaces.atomic_capillars;

import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.vectors.Vector;
import com.private_void.core.particles.Atom;
import com.private_void.core.particles.ChargedParticle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.capillar_factories.CapillarFactory;
import com.private_void.utils.Utils;

import java.util.ArrayList;

public class AtomicCone extends AtomicCapillar {
    private final double divergentAngleR;

    public AtomicCone(final Atom.Factory atomFactory, final CartesianPoint front, double period, double chargeNumber,
                      double radius, double length, double coneCoefficient) throws IllegalArgumentException {
        super(atomFactory, front, period, chargeNumber, radius, length);
        if (coneCoefficient >= 1 || coneCoefficient <= 0) {
            throw new IllegalArgumentException();
        }
        this.divergentAngleR = Utils.getConeDivergentAngle(radius, length, coneCoefficient);
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

    public static CapillarFactory getFactory(final Atom.Factory atomFactory, double period, double chargeNumber,
                                             double radius, double length, double coneCoefficient) {
        return new CapillarFactory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate) {
                return new AtomicCone(atomFactory, coordinate, period, chargeNumber, radius, length,
                        coneCoefficient);
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