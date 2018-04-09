package com.private_void.core.surfaces.atomic_surfaces.atomic_capillars;

import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.coordinates.ReferenceFrame;
import com.private_void.core.particles.Atom;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.atomic_surfaces.AtomicSurface;

public abstract class AtomicCapillar extends AtomicSurface implements Capillar {
    protected final ReferenceFrame refFrame;
    protected final double length;
    protected final double radius;

    public AtomicCapillar(final Atom.Factory atomFactory, final CartesianPoint front, double period, double chargeNumber,
                          double radius, double length) {
        super(atomFactory, front, period, chargeNumber);
        this.refFrame = new ReferenceFrame(front);
        this.radius = radius;
        this.length = length;

    }

    @Override
    public void interact(Particle particle) {

    }

    @Override
    public boolean willParticleGetInside(final Particle p) {
        double x0 = front.getX();

        double x = p.getCoordinate().getX();
        double y = p.getCoordinate().getY();
        double z = p.getCoordinate().getZ();

        double Vx = p.getSpeed().getX();
        double Vy = p.getSpeed().getY();
        double Vz = p.getSpeed().getZ();

        double newY = (Vy / Vx) * (x0 - x) + y;
        double newZ = (Vz / Vx) * (x0 - x) + z;

        return newY * newY + newZ * newZ < radius * radius;
    }

    @Override
    public ReferenceFrame getReferenceFrame() {
        return refFrame;
    }

    protected abstract boolean isPointInside(CartesianPoint point);
}
