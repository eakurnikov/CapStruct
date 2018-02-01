package com.private_void.core.surfaces.simplesurfaces.capillars;

import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.simplesurfaces.SimpleSurface;

import java.util.Iterator;

import static com.private_void.utils.Constants.PI;
import static com.private_void.utils.Generator.generator;

public abstract  class Capillar extends SimpleSurface {
    protected float radius;

    protected Capillar(final Point3D frontCoordinate, float radius, float roughnessSize, float roughnessAngleD, float reflectivity, float criticalAngleD) {
        super(frontCoordinate, roughnessSize, roughnessAngleD, reflectivity, criticalAngleD);
        this.radius = radius;
    }

    @Override
    public void interact(Flux flux) {
        NeutralParticle p;
        Point3D newCoordinate;
        float angleVN;
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

                        angleVN = p.getSpeed().getAngle(normal.inverse());
                        p.decreaseIntensity(reflectivity);

                        if (angleVN >= antiCriticalAngleR && p.getIntensity() >= flux.getMinIntensity()) {
                            p.setCoordinate(newCoordinate);
                            p.setSpeed(p.getSpeed().getNewByTurningAroundVector(2 * Math.abs(PI / 2 - angleVN), axis));
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

    protected boolean willParticleGetInside(Particle p) {
        float x0 = frontCoordinate.getX();

        float x = p.getCoordinate().getX();
        float y = p.getCoordinate().getY();
        float z = p.getCoordinate().getZ();

        float Vx = p.getSpeed().getX();
        float Vy = p.getSpeed().getY();
        float Vz = p.getSpeed().getZ();

        return ((Vy / Vx) * (x0 - x) + y) * ((Vy / Vx) * (x0 - x) + y) +
               ((Vz / Vx) * (x0 - x) + z) * ((Vz / Vx) * (x0 - x) + z) < radius * radius;
    }

    protected abstract boolean isPointInside(Point3D point);

    @Override
    protected abstract Point3D getHitPoint(final NeutralParticle p);

    @Override
    protected abstract Vector3D getNormal(Point3D point);

    @Override
    protected abstract Vector3D getAxis(Point3D point);
}
