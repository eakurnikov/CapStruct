package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars;

import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.reference_frames.ReferenceFrame;
import com.private_void.core.geometry.space_3D.vectors.Vector;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.smooth_surfaces.SmoothSurface;

import static com.private_void.utils.Constants.PI;
import static com.private_void.utils.Generator.generator;

public abstract  class SmoothCapillar extends SmoothSurface implements Capillar {
    protected final ReferenceFrame refFrame;
    protected final double radius;
    protected final double length;

    protected SmoothCapillar(final CartesianPoint front, final ReferenceFrame refFrame, double radius, double length,
                             double roughnessSize, double roughnessAngleR, double reflectivity, double criticalAngleR) {
        super(front, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        this.refFrame = refFrame;
        this.radius = radius;
        this.length = length;
    }

    @Override
    public void interact(Particle p) {
        Vector normal;
        NeutralParticle particle = (NeutralParticle) p;
        CartesianPoint newCoordinate = getHitPoint(particle);

        while (!particle.isAbsorbed() && isPointInside(newCoordinate)) {
            normal = getNormal(newCoordinate)
                    .rotateAroundVector(
                            Vector.E_X.rotateAroundOY(generator().uniformDouble(0.0, 2.0 * PI)),
                            generator().uniformDouble(0.0, roughnessAngleR));

            double angleWithSurface = particle.getSpeed().getAngle(normal) - PI / 2.0;
            particle.decreaseIntensity(reflectivity);

            if (angleWithSurface <= criticalAngleR) {
                particle
                        .setCoordinate(newCoordinate)
                        .rotateSpeed(
                                getParticleSpeedRotationAxis(newCoordinate, normal),
                                2.0 * Math.abs(angleWithSurface));
                newCoordinate = getHitPoint(particle);
            } else {
                particle.absorb();
                break;
            }
            particle.setChanneled();
        }
    }

    @Override
    public boolean willParticleGetInside(final Particle p) {
        double x = p.getCoordinate().getX();
        double y = p.getCoordinate().getY();
        double z = p.getCoordinate().getZ();

        double Vx = p.getSpeed().getX();
        double Vy = p.getSpeed().getY();
        double Vz = p.getSpeed().getZ();

        double newY = y - (Vy / Vx) * x;
        double newZ = z - (Vz / Vx) * x;

        return newY * newY + newZ * newZ < radius * radius;
    }

    @Override
    public ReferenceFrame getReferenceFrame() {
        return refFrame;
    }

    protected abstract boolean isPointInside(final CartesianPoint point);
}