package com.private_void.core.surfaces.atomicsurfaces.atomiccapillars;

import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.particles.AtomFactory;
import com.private_void.core.particles.ChargedParticle;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.atomicsurfaces.AtomicSurface;

import java.util.Iterator;

public abstract class AtomicCapillar extends AtomicSurface {
    protected float radius;

    public AtomicCapillar(final AtomFactory atomFactory, final Point3D frontCoordinate, float period, float chargeNumber,
                          float radius) {
        super(atomFactory, frontCoordinate, period, chargeNumber);
        this.radius = radius;
    }

    @Override
    public void interact(Flux flux) {
        ChargedParticle p;
        Point3D newCoordinate;
        Iterator<? extends Particle> iterator = flux.getParticles().iterator();

        while (iterator.hasNext()) {

        }

        detector.detect(flux);
    }
}
