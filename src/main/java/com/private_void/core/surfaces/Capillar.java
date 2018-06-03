package com.private_void.core.surfaces;

import com.private_void.core.geometry.space_3D.reference_frames.ReferenceFrame;
import com.private_void.core.particles.Particle;
import com.private_void.utils.exceptions.BadParticleException;

public interface Capillar {

    void interact(Particle particle) throws BadParticleException;

    boolean willParticleGetInside(final Particle p);

    ReferenceFrame.Converter getReferenceFrameConverter();
}