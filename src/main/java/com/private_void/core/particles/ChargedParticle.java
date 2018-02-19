package com.private_void.core.particles;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;

public class ChargedParticle extends Particle {
    private float chargeNumber;
    private float energy;

    private ChargedParticle(Point3D coordinate, Vector3D speed, float chargeNumber, float energy) {
        super(coordinate, speed);
        this.chargeNumber = chargeNumber;
        this.energy = energy;
    }

    public static ParticleFactory getFactory() {
        return (final Point3D coordinate, final Vector3D speed) -> new ChargedParticle(coordinate, speed, -1.0f, 1);
    }

    public float getChargeNumber() {
        return chargeNumber;
    }

    public float getEnergy() {
        return energy;
    }
}
