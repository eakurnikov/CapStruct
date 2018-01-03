package com.private_void.core.surfaces.simplesurfaces;

import com.private_void.core.surfaces.Surface;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.utils.Utils;

import static com.private_void.utils.Constants.PI;

//TODO припилить Stream API или просто распараллелить через ThreadPool

public abstract class SimpleSurface extends Surface {
    protected Vector3D normal;
    protected Vector3D axis;
    protected float roughnessSize;
    protected float roughnessAngleR;
    protected float reflectivity;
    protected float criticalAngleR;
    protected float antiCriticalAngleR;

    protected SimpleSurface(final Point3D frontCoordinate, float roughnessSize, float roughnessAngleD, float reflectivity, float criticalAngleD) {
        super(frontCoordinate);
        this.roughnessSize = roughnessSize;
        this.roughnessAngleR = Utils.convertDegreesToRadians(roughnessAngleD);
        this.reflectivity = reflectivity;
        this.criticalAngleR = Utils.convertDegreesToRadians(criticalAngleD);
        this.antiCriticalAngleR = PI / 2 - criticalAngleR;
        this.normal = new Vector3D(0.0f, 1.0f, 0.0f);
        this.axis = new Vector3D(1.0f, 0.0f, 0.0f);
    }

    public abstract void passThrough(Flux flux);

    protected abstract Point3D getHitPoint(final NeutralParticle p);

    protected abstract Vector3D getNormal(Point3D point);

    protected abstract Vector3D getAxis(Point3D point);
}