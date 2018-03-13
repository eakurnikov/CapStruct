package com.private_void.core.surfaces.atomic_surfaces.atomic_capillars;

import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.SphericalPoint;
import com.private_void.core.geometry.Vector;
import com.private_void.core.particles.AtomFactory;
import com.private_void.core.particles.ChargedParticle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.CapillarFactory;
import com.private_void.utils.Utils;

import java.util.ArrayList;

public class AtomicCone extends AtomicCapillar {
    private float divergentAngleR;

    public AtomicCone(final AtomFactory atomFactory, final CartesianPoint front, float period, float chargeNumber,
                      float radius, float length, float coneCoefficient) throws IllegalArgumentException {
        super(atomFactory, front, period, chargeNumber, radius);
        if (coneCoefficient >= 1 || coneCoefficient <= 0) {
            throw new IllegalArgumentException();
        }
        this.length = length;
        this.divergentAngleR = Utils.getConeDivergentAngle(radius, length, coneCoefficient);
    }

    public AtomicCone(final AtomFactory atomFactory, final CartesianPoint front, final SphericalPoint position,
                      float period, float chargeNumber, float radius, float length, float coneCoefficient)
            throws IllegalArgumentException {
        super(atomFactory, front, period, chargeNumber, radius);
        if (coneCoefficient >= 1 || coneCoefficient <= 0) {
            throw new IllegalArgumentException();
        }
        this.position = position;
        this.length = length;
        this.divergentAngleR = Utils.getConeDivergentAngle(radius, length, coneCoefficient);
    }

    @Override
    protected Vector getNormal(final CartesianPoint point) {
        return null;
    }

    @Override
    protected Vector getAxis(final CartesianPoint point) {
        return null;
    }

    @Override
    protected void createAtoms() {
        atoms = new ArrayList<>();
    }

    @Override
    protected float getCriticalAngle(final ChargedParticle particle) {
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

    public static CapillarFactory getFactory(final AtomFactory atomFactory, float period, float chargeNumber,
                                             float radius, float length, float coneCoefficient) {
        return new CapillarFactory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate, final SphericalPoint position) {
                return new AtomicCone(atomFactory, coordinate, position, period, chargeNumber, radius, length,
                        coneCoefficient);
            }

            @Override
            public float getRadius() {
                return radius;
            }

            @Override
            public float getLength() {
                return length;
            }
        };
    }
}