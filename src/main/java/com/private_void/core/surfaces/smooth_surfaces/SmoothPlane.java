package com.private_void.core.surfaces.smooth_surfaces;

import com.private_void.core.detectors.Detector;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.Vector;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.CapillarSystem;

import java.util.Iterator;

import static com.private_void.utils.Constants.PI;

public class SmoothPlane extends SmoothSurface implements CapillarSystem {
    private float size;
    protected Detector detector;

    public SmoothPlane(final CartesianPoint front, float size, float roughnessSize, float roughnessAngleR, float reflectivity,
                       float criticalAngleR) {
        super(front, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        this.size = size;
        this.detector = new Detector(
                new CartesianPoint(front.getX() + size, front.getY(), front.getZ()),
                size);
    }

    @Override
    public void interact(Flux flux) {
        NeutralParticle p;
        CartesianPoint hitPoint;
        float angleWithSurface;
        Iterator<? extends Particle> iterator = flux.getParticles().iterator();

        while (iterator.hasNext()) {
            try {
                p = (NeutralParticle) iterator.next();
                hitPoint = getHitPoint(p);

                if (true /*doesPointBelongToPlane(hitPoint)*/) {
                    angleWithSurface = p.getSpeed().getAngle(normal) - PI / 2;
                    p.decreaseIntensity(reflectivity);

                    if (angleWithSurface <= criticalAngleR && p.getIntensity() >= flux.getMinIntensity()) {
                        p.setCoordinate(hitPoint);
                        p.setSpeed(p.getSpeed().getNewByTurningAroundVector(2 * Math.abs(angleWithSurface), getAxis(hitPoint)));

                    } else {
                        p.setAbsorbed(true);
                        break;
                    }
                } else {
                    p.setOut(true);
                }
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
        detector.detect(flux);
    }

    @Override
    public Detector getDetector() {
        return detector;
    }

    @Override
    protected Vector getNormal(final CartesianPoint point) {
        return new Vector(0.0f, 1.0f, 0.0f);
    }

    @Override
    protected Vector getAxis(final CartesianPoint point) {
        return new Vector(0.0f, 0.0f, 1.0f);
    }

    @Override
    protected CartesianPoint getHitPoint(final NeutralParticle p) {
        float x = p.getCoordinate().getX();
        float y = p.getCoordinate().getY();
        float z = p.getCoordinate().getZ();

        float Vx = p.getSpeed().getX();
        float Vy = p.getSpeed().getY();
        float Vz = p.getSpeed().getZ();

        return new CartesianPoint(
                (Vx / Vy) * (front.getY() - y) + x,
                front.getY(),
                (Vz / Vy) * (front.getY() - y) + z);
    }

    private boolean doesPointBelongToPlane(final CartesianPoint point) {
        float x = point.getX();
        float z = point.getZ();

        return  x >= front.getX() &&
                x <= front.getX() + size &&
                z >= front.getZ() - size / 2 &&
                z <= front.getZ() + size / 2;
    }
}