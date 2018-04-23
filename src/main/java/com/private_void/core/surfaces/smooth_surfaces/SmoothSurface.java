package com.private_void.core.surfaces.smooth_surfaces;

import com.private_void.core.surfaces.Surface;
import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.particles.NeutralParticle;

public abstract class SmoothSurface extends Surface {
    protected final double roughnessSize;
    protected final double roughnessAngleR;
    protected final double reflectivity;
    protected final double criticalAngleR;

    protected SmoothSurface(final CartesianPoint front, double roughnessSize, double roughnessAngleR, double reflectivity,
                            double criticalAngleR) {
        super(front);
        this.roughnessSize = roughnessSize;
        this.roughnessAngleR = roughnessAngleR;
        this.reflectivity = reflectivity;
        this.criticalAngleR = criticalAngleR;
    }

    protected abstract CartesianPoint getHitPoint(final NeutralParticle p);
}