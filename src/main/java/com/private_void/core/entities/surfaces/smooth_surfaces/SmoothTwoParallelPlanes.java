package com.private_void.core.entities.surfaces.smooth_surfaces;

import com.private_void.core.entities.detectors.Detector;
import com.private_void.core.entities.detectors.Distribution;
import com.private_void.core.entities.fluxes.Flux;
import com.private_void.core.entities.particles.NeutralParticle;
import com.private_void.core.entities.particles.Particle;
import com.private_void.core.entities.surfaces.CapillarSystem;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;

import java.util.Iterator;

import static com.private_void.core.constants.Constants.PI;

public class SmoothTwoParallelPlanes extends SmoothSurface implements CapillarSystem {
    private final double size;
    private final double width;
    private final Detector detector;

    public SmoothTwoParallelPlanes(final CartesianPoint front, double size, double width, double roughnessSize,
                                   double roughnessAngleR, double reflectivity, double criticalAngleR) {
        super(front, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        this.size = size;
        this.width = width;
        this.detector = new Detector(new CartesianPoint(front.getX() + size, front.getY(), front.getZ()), size);
    }

    @Override
    public Distribution interact(Flux flux) {
        NeutralParticle particle;
        double angleWithSurface;
        boolean isInteractingWithUpperPlane;
        CartesianPoint newCoordinate;

        Vector normal = Vector.E_Y;
        Vector normalOfUpperPlane = Vector.E_Y.inverse();

        for (Iterator<? extends Particle> iterator = flux.getParticles().iterator(); iterator.hasNext(); ) {
            particle = (NeutralParticle) iterator.next();

            isInteractingWithUpperPlane = particle.getSpeed().getY() > 0;
            newCoordinate = isInteractingWithUpperPlane ?
                    getHitPointWithUpperPlane(particle) :
                    getHitPoint(particle);

            if (doesPointBelongToPlane(newCoordinate)) {
                while (!particle.isAbsorbed() && isPointInside(newCoordinate)) {
                    angleWithSurface = particle.getSpeed()
                            .getAngle(isInteractingWithUpperPlane ? normalOfUpperPlane : normal) - PI / 2.0;
                    particle.decreaseIntensity(reflectivity);

                    if (angleWithSurface <= criticalAngleR && particle.getIntensity() >= flux.getMinIntensity()) {
                        particle
                                .setCoordinate(newCoordinate)
                                .rotateSpeed(isInteractingWithUpperPlane ?
                                                getParticleSpeedRotationAxisOfUpperPlane(newCoordinate, normalOfUpperPlane) :
                                                getParticleSpeedRotationAxis(newCoordinate, normal),
                                        2.0 * Math.abs(angleWithSurface));

                        isInteractingWithUpperPlane = particle.getSpeed().getY() > 0;
                        newCoordinate = isInteractingWithUpperPlane ?
                                getHitPointWithUpperPlane(particle) :
                                getHitPoint(particle);
                    } else {
                        particle.absorb();
                        break;
                    }
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

    protected Vector getNormalOfUpperPlane(final CartesianPoint point) {
        return Vector.E_Y.inverse();
    }

    @Override
    protected Vector getParticleSpeedRotationAxis(final CartesianPoint point, final Vector normal) {
        return Vector.E_Z;
    }

    private Vector getParticleSpeedRotationAxisOfUpperPlane(final CartesianPoint point, final Vector normal) {
        return Vector.E_Z.inverse();
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

    private CartesianPoint getHitPointWithUpperPlane(final NeutralParticle p) {
        double x = p.getCoordinate().getX();
        double y = p.getCoordinate().getY();
        double z = p.getCoordinate().getZ();

        double Vx = p.getSpeed().getX();
        double Vy = p.getSpeed().getY();
        double Vz = p.getSpeed().getZ();

        return new CartesianPoint(
                (Vx / Vy) * (front.getY() + width - y) + x,
                front.getY() + width,
                (Vz / Vy) * (front.getY() + width - y) + z);
    }

    private boolean doesPointBelongToPlane(final CartesianPoint point) {
        double x = point.getX();
        double z = point.getZ();

        return  x >= front.getX() &&
                x <= front.getX() + size &&
                z >= front.getZ() - size / 2.0 &&
                z <= front.getZ() + size / 2.0;
    }

    private boolean isPointInside(final CartesianPoint point) {
        return point.getX() <= front.getX() + size;
    }
}