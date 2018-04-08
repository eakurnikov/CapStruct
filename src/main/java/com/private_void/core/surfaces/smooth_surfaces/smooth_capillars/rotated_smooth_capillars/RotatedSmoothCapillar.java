package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.rotated_smooth_capillars;

import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.coordinates.SphericalPoint;
import com.private_void.core.geometry.vectors.Vector;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.smooth_surfaces.SmoothSurface;

import static com.private_void.utils.Constants.PI;
import static com.private_void.utils.Generator.generator;

public abstract  class RotatedSmoothCapillar extends SmoothSurface implements RotatedCapillar {
    protected final SphericalPoint position;
    protected final double radius;
    protected final double length;

    protected RotatedSmoothCapillar(final CartesianPoint front, final SphericalPoint position, double radius, double length,
                                    double roughnessSize, double roughnessAngleR, double reflectivity, double criticalAngleR) {
        super(front, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        this.position = position;
        this.radius = radius;
        this.length = length;
    }

    @Override
    public void interact(Particle p) {
        Vector normal;

        toInnerRefFrame(p);

        try {
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
                    particle.setAbsorbed(true);
                    break;
                }
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        toGlobalRefFrame(p);
    }

    @Override
    public boolean willParticleGetInside(Particle p) {
        toInnerRefFrame(p);

        double x = p.getCoordinate().getX();
        double y = p.getCoordinate().getY();
        double z = p.getCoordinate().getZ();

        double Vx = p.getSpeed().getX();
        double Vy = p.getSpeed().getY();
        double Vz = p.getSpeed().getZ();

        double newY = y -(Vy / Vx) * x;
        double newZ = z -(Vz / Vx) * x;

        boolean result = newY * newY + newZ * newZ < radius * radius;

        toGlobalRefFrame(p);

        return result;
    }

    @Override
    public SphericalPoint getPosition() {
        return position;
    }

    protected abstract boolean isPointInside(CartesianPoint point);
}