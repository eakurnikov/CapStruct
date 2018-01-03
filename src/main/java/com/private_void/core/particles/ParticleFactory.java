package com.private_void.core.particles;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;

public interface ParticleFactory {
    Particle getNewParticle(final Point3D coordinate, final Vector3D speed);
}
