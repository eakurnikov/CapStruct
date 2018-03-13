package com.private_void.core.surfaces.atomic_surfaces.atomic_capillars;

import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.SphericalPoint;
import com.private_void.core.geometry.Vector;
import com.private_void.core.particles.AtomFactory;
import com.private_void.core.particles.ChargedParticle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.CapillarFactory;

import java.util.ArrayList;

public class AtomicCylinder extends AtomicCapillar {
    private float length;

    public AtomicCylinder(final AtomFactory atomFactory, final CartesianPoint front, float period, float chargeNumber,
                          float radius, float length) {
        super(atomFactory, front, period, chargeNumber, radius);
        this.length = length;
    }

    public AtomicCylinder(final AtomFactory atomFactory, final CartesianPoint front, final SphericalPoint position,
                          float period, float chargeNumber, float radius, float length) {
        super(atomFactory, front, period, chargeNumber, radius);
        this.position = position;
        this.length = length;
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

        float x = front.getX();
        //float y = front.getY();
        //float z = front.getZ() - size / 2;

        while (x <= front.getX() + length) {

            x += period;
        }
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
                                             float radius, float length) {
        return new CapillarFactory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate, final SphericalPoint position) {
                return new AtomicCylinder(atomFactory, coordinate, position, period, chargeNumber, radius, length);
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