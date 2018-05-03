package com.private_void.core.surfaces.smooth_surfaces;

import com.private_void.core.detectors.Detector;
import com.private_void.core.detectors.Distribution;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.vectors.Vector;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.CapillarSystem;

import java.util.Iterator;

import static com.private_void.utils.Constants.PI;

public class SmoothPlane extends SmoothSurface implements CapillarSystem {
    private final double size;
    private final Detector detector;

    public SmoothPlane(final CartesianPoint front, double size, double roughnessSize, double roughnessAngleR,
                       double reflectivity, double criticalAngleR) {
        super(front, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        this.size = size;
        this.detector = new Detector(new CartesianPoint(front.getX() + size, front.getY(), front.getZ()), size);
    }

    @Override
    public Distribution interact(Flux flux) {
        NeutralParticle particle;
        CartesianPoint hitPoint;
        double angleWithSurface;
        Vector normal = Vector.E_Y;

        for (Iterator<? extends Particle> iterator = flux.getParticles().iterator(); iterator.hasNext(); ) {
            particle = (NeutralParticle) iterator.next();
            hitPoint = getHitPoint(particle);

            if (true /*doesPointBelongToPlane(hitPoint)*/) {
                angleWithSurface = particle.getSpeed().getAngle(normal) - PI / 2.0;
                particle.decreaseIntensity(reflectivity);

                if (angleWithSurface <= criticalAngleR && particle.getIntensity() >= flux.getMinIntensity()) {
                    particle
                            .setCoordinate(hitPoint)
                            .rotateSpeed(
                                    getParticleSpeedRotationAxis(hitPoint, normal),
                                    2.0 * Math.abs(angleWithSurface));

                } else {
                    particle.absorb();
                    break;
                }

                particle.setChanneled();
            }
        }

        return detector.detect(flux);
    }

    @Override
    protected Vector getNormal(final CartesianPoint point) {
        return Vector.E_Y;
    }

    @Override
    protected Vector getParticleSpeedRotationAxis(final CartesianPoint point, final Vector normal) {
        return Vector.E_Z;
    }

    @Override
    protected CartesianPoint getHitPoint(final NeutralParticle p) {
        double x = p.getCoordinate().getX();
        double y = p.getCoordinate().getY();
        double z = p.getCoordinate().getZ();

        double Vx = p.getSpeed().getX();
        double Vy = p.getSpeed().getY();
        double Vz = p.getSpeed().getZ();

        return new CartesianPoint(
                (Vx / Vy) * (front.getY() - y) + x,
                front.getY(),
                (Vz / Vy) * (front.getY() - y) + z);
    }

    private boolean doesPointBelongToPlane(final CartesianPoint point) {
        double x = point.getX();
        double z = point.getZ();

        return  x >= front.getX() &&
                x <= front.getX() + size &&
                z >= front.getZ() - size / 2.0 &&
                z <= front.getZ() + size / 2.0;
    }
}