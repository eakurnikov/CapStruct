package com.private_void.core.surfaces;

import com.private_void.core.particles.Particle;

public interface Capillar {
    boolean willParticleGetInside(final Particle p);
    void interact(Particle particle);
}
