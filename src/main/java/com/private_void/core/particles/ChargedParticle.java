package com.private_void.core.particles;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;

public class ChargedParticle extends Particle {
    private float chargeNumber;
    private float mass;
    private float energy;

    private ChargedParticle(final Point3D coordinate, final Vector3D speed, float chargeNumber, float mass, float energy) {
        super(coordinate, speed);
        this.chargeNumber = chargeNumber;
        this.mass = mass;
        this.energy = energy;
    }

    public static ParticleFactory getFactory(float chargeNumber, float mass, float energy) {
        return (final Point3D coordinate, final Vector3D speed) ->
                new ChargedParticle(coordinate, speed, chargeNumber, mass, energy);
    }

    public float getChargeNumber() {
        return chargeNumber;
    }

    public float getMass() {
        return mass;
    }

    public float getEnergy() {
        return energy;
    }
}
