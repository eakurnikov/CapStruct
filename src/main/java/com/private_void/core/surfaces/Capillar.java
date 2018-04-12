package com.private_void.core.surfaces;

import com.private_void.core.geometry.reference_frames.ReferenceFrame;
import com.private_void.core.particles.Particle;

public interface Capillar {

    void interact(Particle particle);

    boolean willParticleGetInside(final Particle p);

    ReferenceFrame getReferenceFrame();
}