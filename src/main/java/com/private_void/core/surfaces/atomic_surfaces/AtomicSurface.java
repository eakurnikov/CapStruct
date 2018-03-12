package com.private_void.core.surfaces.atomic_surfaces;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.Atom;
import com.private_void.core.particles.AtomFactory;
import com.private_void.core.particles.ChargedParticle;
import com.private_void.core.surfaces.Surface;

import java.util.ArrayList;
import java.util.List;

import static com.private_void.utils.Constants.BOHR_RADIUS;

public abstract class AtomicSurface extends Surface {
    protected float period;
    protected float chargeNumber;
    protected float shieldingDistance;
    protected AtomFactory atomFactory;
    protected List<Atom> atoms;

    public AtomicSurface(final AtomFactory atomFactory, final Point3D front, float period, float chargeNumber) {
        super(front);
        this.atomFactory = atomFactory;
        this.period = period;
        this.chargeNumber = chargeNumber;
        this.shieldingDistance = 1.0f;
        this.atoms = new ArrayList<>();
    }

    protected void setShieldingDistance(float particleChargeNumber) {
        this.shieldingDistance = 0.885f * BOHR_RADIUS * (float) (Math.pow(Math.sqrt(particleChargeNumber) + Math.sqrt(chargeNumber),  2/3));
    }

    protected abstract void createAtoms();

    protected abstract float getCriticalAngle(final ChargedParticle particle);

    protected abstract Vector3D getNewSpeed(final ChargedParticle particle);

    protected abstract Point3D getNewCoordinate(final ChargedParticle p);

    //protected abstract float getPotential(final ChargedParticle particle);
}