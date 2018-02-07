package com.private_void.core.surfaces.atomicsurfaces;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.Atom;
import com.private_void.core.particles.AtomFactory;
import com.private_void.core.particles.ChargedParticle;
import com.private_void.core.surfaces.Surface;

import java.util.ArrayList;
import java.util.List;

public abstract class AtomicSurface extends Surface {
    protected float period;
    protected float chargeNumber;
    protected AtomFactory atomFactory;
    protected List<Atom> atoms;

    public AtomicSurface(final AtomFactory atomFactory, final Point3D frontCoordinate, float period, float chargeNumber) {
        super(frontCoordinate);
        this.atomFactory = atomFactory;
        this.period = period;
        this.chargeNumber = chargeNumber;
        this.atoms = new ArrayList<>();
    }

    protected abstract void createAtoms();

    protected abstract Point3D getNewCoordinate(final ChargedParticle p);

    protected abstract float getCriticalAngle(final ChargedParticle particle);

    protected abstract float getPotential(final Point3D coordinate);

    protected abstract Vector3D getSpeedByPotential(float potential);
}