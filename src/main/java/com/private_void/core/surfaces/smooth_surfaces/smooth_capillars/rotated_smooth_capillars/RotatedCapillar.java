package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.rotated_smooth_capillars;

import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.coordinates.SphericalPoint;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.Capillar;

public interface RotatedCapillar extends Capillar {

    SphericalPoint getPosition();

    void toInnerRefFrame(Particle particle);

    void toGlobalRefFrame(Particle particle);

    interface Factory {

        RotatedCapillar getNewCapillar(final CartesianPoint coordinate, final SphericalPoint position);

        double getRadius();

        double getLength();
    }
}