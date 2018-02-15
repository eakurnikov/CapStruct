package com.private_void.core.surfaces.atomicsurfaces.atomiccapillars;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.AtomFactory;
import com.private_void.core.particles.ChargedParticle;

import java.util.ArrayList;

public class AtomicCylinder extends AtomicCapillar {
    private float length;

    public AtomicCylinder(final AtomFactory atomFactory, final Point3D frontCoordinate, float period, float chargeNumber,
                          float radius, float length) {
        super(atomFactory, frontCoordinate, period, chargeNumber, radius);
        this.length = length;
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

        float x = frontCoordinate.getX();
        //float y = frontCoordinate.getY();
        //float z = frontCoordinate.getZ() - size / 2;

        while (x <= frontCoordinate.getX() + length) {

            x += period;
        }
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
    protected float getPotential(final ChargedParticle particle) {
        return 0;
    }

    @Override
    protected Vector3D getSpeedByPotential(float potential) {
        return null;
    }
}