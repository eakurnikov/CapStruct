package com.private_void.core.entities.surfaces.atomic_surfaces;

import com.private_void.core.entities.particles.ChargedParticle;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;

import static com.private_void.core.constants.Constants.BOHR_RADIUS;

public abstract class AtomicSurface {
    protected final CartesianPoint front;
    protected final double period;
    protected final double chargeNumber;
    protected double criticalAngle;
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

    protected abstract void setCriticalAngle(final ChargedParticle particle);

    protected abstract Vector rotateParticleSpeed(final ChargedParticle particle);

    protected abstract Vector getNextParticleSpeed(final ChargedParticle particle);

    protected abstract CartesianPoint getNextParticleCoordinate(final ChargedParticle particle);
}