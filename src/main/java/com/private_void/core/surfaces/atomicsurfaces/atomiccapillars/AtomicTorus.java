package com.private_void.core.surfaces.atomicsurfaces.atomiccapillars;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.particles.AtomFactory;
import com.private_void.core.particles.ChargedParticle;

import java.util.ArrayList;

public class AtomicTorus extends AtomicCapillar {

    public AtomicTorus(final Point3D frontCoordinate, final AtomFactory factory, float period, float chargeNumber,
                       float radius) {
        super(frontCoordinate, factory, period, chargeNumber, radius);
    }

    @Override
    protected void createAtoms() {
        atoms = new ArrayList<>();
    }

    @Override
    protected float getCriticalAngle(ChargedParticle particle) {
        return 0;
    }

    @Override
    protected float getPotential(ChargedParticle particle) {
        return 0;
    }
}
