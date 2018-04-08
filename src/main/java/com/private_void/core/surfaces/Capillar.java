package com.private_void.core.surfaces;

import com.private_void.core.particles.Particle;

public interface Capillar {

    void interact(Particle particle);

    boolean willParticleGetInside(final Particle p);

    void toInnerRefFrame(Particle particle);

    void toGlobalRefFrame(Particle particle);
}