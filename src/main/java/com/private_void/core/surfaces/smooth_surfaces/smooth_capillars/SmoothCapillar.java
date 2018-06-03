package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars;

import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.reference_frames.ReferenceFrame;
import com.private_void.core.geometry.space_3D.vectors.Vector;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.smooth_surfaces.SmoothSurface;
import com.private_void.utils.exceptions.BadParticleException;
import com.private_void.utils.newtons_method.NewtonsMethod;

import static com.private_void.utils.Constants.PI;
import static com.private_void.utils.Generator.generator;

public abstract class SmoothCapillar extends SmoothSurface implements Capillar {
    private final ReferenceFrame.Converter refFrameConverter;
    protected final double radius;
    protected final double length;

    protected SmoothCapillar(final CartesianPoint front, final ReferenceFrame refFrame, double radius, double length,
                             double roughnessSize, double roughnessAngleR, double reflectivity, double criticalAngleR) {
        super(front, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        this.refFrameConverter = new ReferenceFrame.Converter(refFrame);
        this.radius = radius;
        this.length = length;
    }

    @Override
    public void interact(Particle p) throws BadParticleException {
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
    public ReferenceFrame.Converter getReferenceFrameConverter() {
        return refFrameConverter;
    }

    @Override
    protected CartesianPoint getHitPoint(final NeutralParticle particle) throws BadParticleException {
        if (particle.getSpeed().getX() <= 0.0) {
            throw new BadParticleException();
        }

        return new NewtonsMethod(getEquation(particle)).getSolution();
    }

    protected abstract boolean isPointInside(final CartesianPoint point);

    protected abstract NewtonsMethod.Equation getEquation(final NeutralParticle particle);
}