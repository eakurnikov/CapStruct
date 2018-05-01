package com.private_void.core.surfaces.atomic_surfaces;

import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.vectors.Vector;
import com.private_void.core.particles.Atom;
import com.private_void.core.particles.ChargedParticle;
import com.private_void.core.surfaces.Surface;

import java.util.ArrayList;
import java.util.List;

import static com.private_void.utils.Constants.BOHR_RADIUS;

public abstract class AtomicSurface extends Surface {
    protected final double period;
    protected final double chargeNumber;
    protected final Atom.Factory atomFactory;

    protected double shieldingDistance;
    protected List<Atom> atoms;

    public AtomicSurface(final Atom.Factory atomFactory, final CartesianPoint front, double period, double chargeNumber) {
        super(front);
        this.atomFactory = atomFactory;
        this.period = period;
        this.chargeNumber = chargeNumber;
        this.shieldingDistance = 1.0;
        this.atoms = new ArrayList<>();
    }

    protected void setShieldingDistance(double particleChargeNumber) {
        this.shieldingDistance = 0.885 * BOHR_RADIUS * Math.pow(Math.sqrt(particleChargeNumber) + Math.sqrt(chargeNumber),  2.0 / 3.0);
    }

    protected abstract void createAtoms();

    protected abstract double getCriticalAngle(final ChargedParticle particle);

    protected abstract Vector rotateParticleSpeed(final ChargedParticle particle);

    //protected abstract double getPotential(final ChargedParticle particle);
}