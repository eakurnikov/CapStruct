package com.private_void.core.surfaces;

import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.particles.Particle;

public interface Capillar {

    boolean willParticleGetInside(final Particle p);

    void interact(Particle particle);

    interface Factory {

        Capillar getNewCapillar(final CartesianPoint coordinate);

        double getRadius();

        double getLength();
    }
}
