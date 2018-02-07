package com.private_void.core.surfaces.atomicsurfaces.atomiccapillars;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.AtomFactory;
import com.private_void.core.particles.ChargedParticle;

import java.util.ArrayList;

public class AtomicTorus extends AtomicCapillar {

    public AtomicTorus(final AtomFactory atomFactory, final Point3D frontCoordinate, float period, float chargeNumber,
                       float radius) {
        super(atomFactory, frontCoordinate, period, chargeNumber, radius);
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
    protected Point3D getNewCoordinate(final ChargedParticle p) {
        return null;
    }

    @Override
    protected float getCriticalAngle(final ChargedParticle particle) {
        return 0;
    }

    @Override
    protected float getPotential(final Point3D coordinate) {
        return 0;
    }

    @Override
    protected Vector3D getSpeedByPotential(float potential) {
        return null;
    }
}