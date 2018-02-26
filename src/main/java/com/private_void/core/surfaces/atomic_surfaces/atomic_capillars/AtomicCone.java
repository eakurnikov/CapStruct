package com.private_void.core.surfaces.atomic_surfaces.atomic_capillars;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.AtomFactory;
import com.private_void.core.particles.ChargedParticle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.CapillarFactory;
import com.private_void.utils.Utils;

import java.util.ArrayList;

public class AtomicCone extends AtomicCapillar {
    private float divergentAngleR;

    public AtomicCone(final AtomFactory atomFactory, final Point3D frontCoordinate, float period, float chargeNumber,
                      float radius, float length, float coneCoefficient) throws IllegalArgumentException {
        super(atomFactory, frontCoordinate, period, chargeNumber, radius);
        if (coneCoefficient >= 1 || coneCoefficient <= 0) {
            throw new IllegalArgumentException();
        }
        this.length = length;
        this.divergentAngleR = Utils.getConeDivergentAngle(radius, length, coneCoefficient);
    }

    @Override
    protected Vector3D getNormal(final Point3D point) {
        return null;
    }

    @Override
    protected Vector3D getAxis(final Point3D point) {
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
    protected Vector3D getNewSpeed(final ChargedParticle particle) {
        return null;
    }

    @Override
    protected Point3D getNewCoordinate(final ChargedParticle p) {
        return null;
    }

    @Override
    protected boolean isPointInside(Point3D point) {
        return false;
    }

    public static CapillarFactory getFactory(final AtomFactory atomFactory, float period, float chargeNumber,
                                             float radius, float length, float coneCoefficient) {
        return new CapillarFactory() {

            @Override
            public Capillar getNewCapillar(Point3D coordinate) {
                return new AtomicCone(atomFactory, coordinate, period, chargeNumber, radius, length, coneCoefficient);
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