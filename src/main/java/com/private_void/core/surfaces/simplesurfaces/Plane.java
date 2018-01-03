package com.private_void.core.surfaces.simplesurfaces;

import com.private_void.core.detectors.Detector;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.core.particles.Particle;

import java.util.Iterator;

import static com.private_void.utils.Constants.PI;

public class Plane extends SimpleSurface {
    private float size;

    public Plane(final Point3D frontCoordinate, float size, float roughnessSize, float roughnessAngleD, float reflectivity, float criticalAngleD) {
        super(frontCoordinate, roughnessSize, roughnessAngleD, reflectivity, criticalAngleD);
        this.size = size;
        this.detector = new Detector(new Point3D(frontCoordinate.getX() + size, frontCoordinate.getY(), frontCoordinate.getZ()), size);
    }

    @Override
    public void passThrough(Flux flux) {
        NeutralParticle p;
        Point3D hitPoint;
        float angleVN;
        Iterator<? extends Particle> iterator = flux.getParticles().iterator();

        while (iterator.hasNext()) {
            try {
                p = (NeutralParticle) iterator.next();
                hitPoint = getHitPoint(p);

                if (hitPoint.getX() <= size) {
                    angleVN = p.getSpeed().getAngle(getNormal(hitPoint).inverse());
                    p.decreaseIntensity(reflectivity);

                    if (angleVN >= antiCriticalAngleR && p.getIntensity() >= flux.getMinIntensity()) {
                        p.setCoordinate(hitPoint);
                        p.setSpeed(p.getSpeed().getNewByTurningAroundVector(2 * Math.abs(PI / 2 - angleVN), getAxis(hitPoint)));

                    } else {
                        p.setAbsorbed(true);
                        detector.increaseAbsorbedParticlesAmount();
                        detector.increaseAbsorbedIntensity(p.getIntensity());
                        iterator.remove();
                        break;
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

    @Override
    protected Point3D getHitPoint(NeutralParticle p) {
        float x = p.getCoordinate().getX();
        float y = p.getCoordinate().getY();
        float z = p.getCoordinate().getZ();

        float Vx = p.getSpeed().getX();
        float Vy = p.getSpeed().getY();
        float Vz = p.getSpeed().getZ();

        return new Point3D(
                (Vx / Vy) * (frontCoordinate.getY() - y) + x,
                frontCoordinate.getY(),
                (Vz / Vy) * (frontCoordinate.getY() - y) + z);
    }

    @Override
    protected Vector3D getNormal(Point3D point) {
        return new Vector3D(0.0f, 1.0f, 0.0f);
    }

    @Override
    protected Vector3D getAxis(Point3D point) {
        return new Vector3D(0.0f, 0.0f, 1.0f);
    }
}
