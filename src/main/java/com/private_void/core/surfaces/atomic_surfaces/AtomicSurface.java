package com.private_void.core.surfaces.atomic_surfaces;

import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.vectors.Vector;
import com.private_void.core.particles.ChargedParticle;

import static com.private_void.utils.Constants.BOHR_RADIUS;

public abstract class AtomicSurface {
    protected final CartesianPoint front;
    protected final double period;
    protected final double chargeNumber;
    protected double shieldingDistance;

    public AtomicSurface(final CartesianPoint front, double period, double chargeNumber) {
        this.front = front;
        this.period = period;
        this.chargeNumber = chargeNumber;
    }

    protected void setShieldingDistance(double particleChargeNumber) {
        this.shieldingDistance = 0.885 * BOHR_RADIUS * Math.pow(Math.sqrt(particleChargeNumber) +
                Math.sqrt(chargeNumber),  2.0 / 3.0);
    }

    protected abstract Vector getAxis(final CartesianPoint point);

    protected abstract double getCriticalAngle(final ChargedParticle particle);

    protected abstract Vector rotateParticleSpeed(final ChargedParticle particle);

//    protected abstract void createAtoms();

//    protected abstract double getPotential(final ChargedParticle particle);
}