package com.private_void.core.surfaces.smooth_surfaces;

import com.private_void.core.surfaces.Surface;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.particles.NeutralParticle;

public abstract class SmoothSurface extends Surface {
    protected float roughnessSize;
    protected float roughnessAngleR;
    protected float reflectivity;
    protected float criticalAngleR;

    protected SmoothSurface(final Point3D front, float roughnessSize, float roughnessAngleR, float reflectivity,
                            float criticalAngleR) {
        super(front);
        this.roughnessSize = roughnessSize;
        this.roughnessAngleR = roughnessAngleR;
        this.reflectivity = reflectivity;
        this.criticalAngleR = criticalAngleR;
    }

    protected abstract Point3D getHitPoint(final NeutralParticle p);
}