package com.private_void.core.surfaces.atomic_surfaces.atomic_capillars;

import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.vectors.Vector;
import com.private_void.core.particles.Atom;
import com.private_void.core.particles.ChargedParticle;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.capillar_factories.CapillarFactory;
import com.private_void.utils.Utils;

import java.util.ArrayList;

public class AtomicTorus extends AtomicCapillar {
    private final double curvRadius;
    private final double curvAngleR;

    public AtomicTorus(final Atom.Factory atomFactory, final CartesianPoint front, double period, double chargeNumber,
                       double radius, double curvRadius, double curvAngleR) {
        super(atomFactory, front, period, chargeNumber, radius, Utils.getTorusLength(curvRadius, curvAngleR));
        this.curvRadius = curvRadius;
        this.curvAngleR = curvAngleR;
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
    public void toInnerRefFrame(Particle particle) {
        particle.shiftCoordinate(-front.getX(), -front.getY(), -front.getZ());
    }

    @Override
    public void toGlobalRefFrame(Particle particle) {
        particle.shiftCoordinate(front.getX(), front.getY(), front.getZ());
    }

    @Override
    protected boolean isPointInside(CartesianPoint point) {
        return false;
    }

    public static CapillarFactory getFactory(final Atom.Factory atomFactory, double period, double chargeNumber,
                                             double radius, double curvRadius, double curvAngleR) {
        return new CapillarFactory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate) {
                return new AtomicTorus(atomFactory, coordinate, period, chargeNumber, radius, curvRadius,
                        curvAngleR);
            }

            @Override
            public double getRadius() {
                return radius;
            }

            @Override
            public double getLength() {
                return Utils.getTorusLength(curvRadius, curvAngleR);
            }
        };
    }
}