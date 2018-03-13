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

public class AtomicTorus extends AtomicCapillar {
    private float curvRadius;
    private float curvAngleR;

    public AtomicTorus(final AtomFactory atomFactory, final CartesianPoint front, float period, float chargeNumber,
                       float radius, float curvRadius, float curvAngleR) {
        super(atomFactory, front, period, chargeNumber, radius);
        this.curvRadius = curvRadius;
        this.curvAngleR = curvAngleR;
        this.length = Utils.getTorusLength(curvRadius, curvAngleR);
    }

    public AtomicTorus(final AtomFactory atomFactory, final CartesianPoint front, final SphericalPoint position, float period,
                       float chargeNumber, float radius, float curvRadius, float curvAngleR) {
        super(atomFactory, front, period, chargeNumber, radius);
        this.position = position;
        this.curvRadius = curvRadius;
        this.curvAngleR = curvAngleR;
        this.length = Utils.getTorusLength(curvRadius, curvAngleR);
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
                                             float radius, float curvRadius, float curvAngleR) {
        return new CapillarFactory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate, final SphericalPoint position) {
                return new AtomicTorus(atomFactory, coordinate, position, period, chargeNumber, radius, curvRadius,
                        curvAngleR);
            }

            @Override
            public float getRadius() {
                return radius;
            }

            @Override
            public float getLength() {
                return Utils.getTorusLength(curvRadius, curvAngleR);
            }
        };
    }
}