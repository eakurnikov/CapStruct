package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.smooth_surfaces.SmoothSurface;

import static com.private_void.utils.Constants.PI;
import static com.private_void.utils.Generator.generator;

public abstract  class SmoothCapillar extends SmoothSurface implements Capillar {
    protected float length;
    protected float radius;

    protected SmoothCapillar(final Point3D frontCoordinate, float radius, float roughnessSize, float roughnessAngleD,
                             float reflectivity, float criticalAngleD) {
        super(frontCoordinate, roughnessSize, roughnessAngleD, reflectivity, criticalAngleD);
        this.radius = radius;
    }

    @Override
    public void interact(Particle particle) {
        try {
            NeutralParticle p = (NeutralParticle) particle;
            Point3D newCoordinate = getHitPoint(p);

            while (isPointInside(newCoordinate)) {
                axis = new Vector3D(1.0f, 0.0f, 0.0f)
                        .turnAroundOY(generator().uniformFloat(0.0f, 2.0f * PI));
                normal = getNormal(newCoordinate)
                        .turnAroundVector(generator().uniformFloat(0.0f, roughnessAngleR), axis);
                axis = getAxis(newCoordinate);

                float angleWithSurface = p.getSpeed().getAngle(normal) - PI / 2;
                p.decreaseIntensity(reflectivity);

                if (angleWithSurface <= criticalAngleR) {
                    p.setCoordinate(newCoordinate);
                    p.setSpeed(p.getSpeed().getNewByTurningAroundVector(2 * Math.abs(angleWithSurface), axis));
                    newCoordinate = getHitPoint(p);
                } else {
                    p.setAbsorbed(true);
                    break;
                }
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean willParticleGetInside(final Particle p) {
        float x0 = front.getX();

        float x = p.getCoordinate().getX();
        float y = p.getCoordinate().getY();
        float z = p.getCoordinate().getZ();

        float Vx = p.getSpeed().getX();
        float Vy = p.getSpeed().getY();
        float Vz = p.getSpeed().getZ();

        float newY = (Vy / Vx) * (x0 - x) + y;
        float newZ = (Vz / Vx) * (x0 - x) + z;

        return (newY - front.getY()) * (newY - front.getY()) + (newZ - front.getZ()) * (newZ - front.getZ())
                < radius * radius;
    }

    protected abstract boolean isPointInside(Point3D point);
}