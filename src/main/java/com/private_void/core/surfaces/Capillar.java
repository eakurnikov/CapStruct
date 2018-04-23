package com.private_void.core.surfaces;

import com.private_void.core.geometry.space_3D.reference_frames.ReferenceFrame;
import com.private_void.core.particles.Particle;

public interface Capillar {

    void interact(Particle particle);

    boolean willParticleGetInside(final Particle p);

    ReferenceFrame getReferenceFrame();
}