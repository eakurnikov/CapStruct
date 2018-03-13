package com.private_void.core.particles;

import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.Vector;

public interface ParticleFactory {
    Particle getNewParticle(final CartesianPoint coordinate, final Vector speed);
}
