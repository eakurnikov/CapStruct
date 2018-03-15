package com.private_void.core.particles;

import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.Vector;

public class ChargedParticle extends Particle {
    private double chargeNumber;
    private double mass;
    private double energy;

    private ChargedParticle(final CartesianPoint coordinate, final Vector speed, double chargeNumber, double mass, double energy) {
        super(coordinate, speed);
        this.chargeNumber = chargeNumber;
        this.mass = mass;
        this.energy = energy;
    }

    public static ParticleFactory getFactory(double chargeNumber, double mass, double energy) {
        return (final CartesianPoint coordinate, final Vector speed) ->
                new ChargedParticle(coordinate, speed, chargeNumber, mass, energy);
    }

    public double getChargeNumber() {
        return chargeNumber;
    }

    public double getMass() {
        return mass;
    }

    public double getEnergy() {
        return energy;
    }
}
