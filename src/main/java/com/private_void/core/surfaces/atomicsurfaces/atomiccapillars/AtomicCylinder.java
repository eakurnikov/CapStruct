package com.private_void.core.surfaces.atomicsurfaces.atomiccapillars;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.particles.AtomFactory;
import com.private_void.core.particles.ChargedParticle;

import java.util.ArrayList;

public class AtomicCylinder extends AtomicCapillar {
    private float length;

    public AtomicCylinder(final Point3D frontCoordinate, final AtomFactory factory, float period, float chargeNumber,
                          float radius, float length) {
        super(frontCoordinate, factory, period, chargeNumber, radius);
        this.length = length;
    }

    @Override
    protected void createAtoms() {
        atoms = new ArrayList<>();

        float x = frontCoordinate.getX();
        //float y = frontCoordinate.getY();
        //float z = frontCoordinate.getZ() - size / 2;

        while (x <= frontCoordinate.getX() + length) {

            x += period;
        }
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
