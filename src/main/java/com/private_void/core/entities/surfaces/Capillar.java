package com.private_void.core.entities.surfaces;

import com.private_void.core.entities.particles.Particle;
import com.private_void.core.exceptions.BadParticleException;
import com.private_void.core.math.geometry.space_3D.reference_frames.ReferenceFrame;

public interface Capillar {

    void interact(Particle particle) throws BadParticleException;

    boolean willParticleGetInside(final Particle p);

    ReferenceFrame.Converter getReferenceFrameConverter();
}