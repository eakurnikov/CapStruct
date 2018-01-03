package com.private_void.core.particles;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;

public class ChargedParticle extends Particle {
    private double charge;

    private ChargedParticle(Point3D coordinate, Vector3D speed, double charge) {
        super(coordinate, speed);
        this.charge = charge;
    }

    public static ParticleFactory getFactory() {
        return (final Point3D coordinate, final Vector3D speed) -> new ChargedParticle(coordinate, speed, -1.0f);
    }
}
