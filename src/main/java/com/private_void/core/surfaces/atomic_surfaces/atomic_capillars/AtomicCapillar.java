package com.private_void.core.surfaces.atomic_surfaces.atomic_capillars;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.particles.AtomFactory;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.atomic_surfaces.AtomicSurface;

public abstract class AtomicCapillar extends AtomicSurface implements Capillar {
    protected float radius;

    public AtomicCapillar(final AtomFactory atomFactory, final Point3D frontCoordinate, float period, float chargeNumber,
                          float radius) {
        super(atomFactory, frontCoordinate, period, chargeNumber);
        this.radius = radius;
    }

    @Override
    public void interact(Particle particle) {

    }

    @Override
    public boolean willParticleGetInside(final Particle p) {
        float x0 = front.getX();

        float x = p.getCoordinate().getX();
        float y = p.getCoordinate().getY();
        float z = p.getCoordinate().getZ();

        float Vx = p.getSpeed().getX();
        float Vy = p.getSpeed().getY();
        float Vz = p.getSpeed().getZ();

        float newY = (Vy / Vx) * (x0 - x) + y;
        float newZ = (Vz / Vx) * (x0 - x) + z;

        return newY * newY + newZ * newZ < radius * radius;
    }

    protected abstract boolean isPointInside(Point3D point);
}
