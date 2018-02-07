package com.private_void.core.surfaces.simplesurfaces;

import com.private_void.core.surfaces.Surface;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.utils.Utils;

import static com.private_void.utils.Constants.PI;

public abstract class SimpleSurface extends Surface {
    protected float roughnessSize;
    protected float roughnessAngleR;
    protected float reflectivity;
    protected float criticalAngleR;
    protected float antiCriticalAngleR;

    protected SimpleSurface(final Point3D frontCoordinate, float roughnessSize, float roughnessAngleD,
                            float reflectivity, float criticalAngleD) {
        super(frontCoordinate);
        this.roughnessSize = roughnessSize;
        this.roughnessAngleR = Utils.convertDegreesToRadians(roughnessAngleD);
        this.reflectivity = reflectivity;
        this.criticalAngleR = Utils.convertDegreesToRadians(criticalAngleD);
        this.antiCriticalAngleR = PI / 2 - criticalAngleR;
    }

    protected abstract Point3D getHitPoint(final NeutralParticle p);
}