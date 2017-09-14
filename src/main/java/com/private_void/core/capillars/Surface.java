package com.private_void.core.capillars;

import com.private_void.core.detectors.Detector;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.Particle;
import com.private_void.utils.Utils;

import static com.private_void.utils.Constants.PI;

public abstract class Surface {
    protected Detector detector;
    protected Point3D frontCoordinate;
    protected Vector3D normal;
    protected Vector3D axis;
    protected float radius;
    protected float roughnessSize;
    protected float roughnessAngleR;
    protected float reflectivity;
    protected float slideAngleR;
    protected float antiSlideAngleR;

    protected Surface(final Point3D frontCoordinate, float radius, float roughnessSize, float roughnessAngleD, float reflectivity, float slideAngleD) {
        this.frontCoordinate = frontCoordinate;
        this.radius = radius;
        this.roughnessSize = roughnessSize;
        this.roughnessAngleR = Utils.convertDegreesToRads(roughnessAngleD);
        this.reflectivity = reflectivity;
        this.slideAngleR = Utils.convertDegreesToRads(slideAngleD);
        this.antiSlideAngleR = PI / 2 - slideAngleR;

        normal = new Vector3D(0.0f, 1.0f, 0.0f);
        axis = new Vector3D(1.0f, 0.0f, 0.0f);
    }

    public abstract Point3D getHitPoint(final Particle particle);

    public abstract void passThrough(Flux flux);

    protected boolean willParticleGetInside(Particle particle) {
        float x0 = frontCoordinate.getX();

        float x = particle.getCoordinate().getX();
        float y = particle.getCoordinate().getY();
        float z = particle.getCoordinate().getZ();

        float Vx = particle.getSpeed().getX();
        float Vy = particle.getSpeed().getY();
        float Vz = particle.getSpeed().getZ();

        return ((Vy / Vx) * (x0 - x) + y) * ((Vy / Vx) * (x0 - x) + y) +
                ((Vz / Vx) * (x0 - x) + z) * ((Vz / Vx) * (x0 - x) + z) < radius * radius;
    }

    protected abstract boolean isPointInside(Point3D point);

    protected abstract Vector3D getNormal(Point3D point);

    public Detector getDetector() {
        return detector;
    }
}