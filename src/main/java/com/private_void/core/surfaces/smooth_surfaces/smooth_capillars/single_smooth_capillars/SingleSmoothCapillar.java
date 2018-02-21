package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars;

import com.private_void.core.detectors.Detector;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.smooth_surfaces.SmoothSurface;

import java.util.Iterator;

import static com.private_void.utils.Constants.PI;
import static com.private_void.utils.Generator.generator;

public abstract class SingleSmoothCapillar extends SmoothSurface {
    protected float radius;
    protected Detector detector;

    protected SingleSmoothCapillar(final Point3D frontCoordinate, float radius, float roughnessSize, float roughnessAngleD,
                             float reflectivity, float criticalAngleD) {
        super(frontCoordinate, roughnessSize, roughnessAngleD, reflectivity, criticalAngleD);
        this.radius = radius;
    }

    public void interact(Flux flux) {
        NeutralParticle p;
        Point3D newCoordinate;
        float angleWithSurface;
        Iterator<? extends Particle> iterator = flux.getParticles().iterator();

        while (iterator.hasNext()) {
            try {
                p = (NeutralParticle) iterator.next();

                if (willParticleGetInside(p)) {
                    newCoordinate = getHitPoint(p);

                    while (isPointInside(newCoordinate)) {
                        axis = new Vector3D(1.0f, 0.0f, 0.0f)
                                .turnAroundOY(generator().uniformFloat(0.0f, 2.0f * PI));
                        normal = getNormal(newCoordinate)
                                .turnAroundVector(generator().uniformFloat(0.0f, roughnessAngleR), axis);
                        axis = getAxis(newCoordinate);

                        angleWithSurface = p.getSpeed().getAngle(normal) - PI / 2;
                        p.decreaseIntensity(reflectivity);

                        if (angleWithSurface <= criticalAngleR && p.getIntensity() >= flux.getMinIntensity()) {
                            p.setCoordinate(newCoordinate);
                            p.setSpeed(p.getSpeed().getNewByTurningAroundVector(2 * Math.abs(angleWithSurface), axis));
                            newCoordinate = getHitPoint(p);
                        } else {
                            p.setAbsorbed(true);
                            detector.increaseAbsorbedParticlesAmount();
                            detector.increaseAbsorbedIntensity(p.getIntensity());
                            iterator.remove();
                            break;
                        }
                    }
                } else {
                    detector.increaseOutOfCapillarParticlesAmount();
                    detector.increaseOutOfCapillarIntensity(p.getIntensity());
                    iterator.remove();
                }
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
        detector.detect(flux);
    }

    protected boolean willParticleGetInside(final Particle p) {
        float x0 = front.getX();

        float x = p.getCoordinate().getX();
        float y = p.getCoordinate().getY();
        float z = p.getCoordinate().getZ();

        float Vx = p.getSpeed().getX();
        float Vy = p.getSpeed().getY();
        float Vz = p.getSpeed().getZ();

        float newY = (Vy / Vx) * (x0 - x) + y;
        float newZ = (Vz / Vx) * (x0 - x) + z;

        return newY * newY + newZ * newZ < radius * radius;
    }

    protected abstract boolean isPointInside(Point3D point);

    protected abstract Point3D getDetectorsCoordinate();

    public Detector getDetector() {
        return detector;
    }
}