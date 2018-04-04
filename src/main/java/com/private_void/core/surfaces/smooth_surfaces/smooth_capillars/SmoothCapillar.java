package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars;

import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.SphericalPoint;
import com.private_void.core.geometry.Vector;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.smooth_surfaces.SmoothSurface;

import static com.private_void.utils.Constants.PI;
import static com.private_void.utils.Generator.generator;

public abstract  class SmoothCapillar extends SmoothSurface implements Capillar {
    protected SphericalPoint position;
    protected double length;
    protected double radius;

    protected SmoothCapillar(final CartesianPoint front, double radius, double roughnessSize, double roughnessAngleR,
                             double reflectivity, double criticalAngleR) {
        super(front, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        this.radius = radius;
    }

    protected SmoothCapillar(final CartesianPoint front, final SphericalPoint position, double radius,
                             double roughnessSize, double roughnessAngleR, double reflectivity, double criticalAngleR) {
        super(front, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        this.position = position;
        this.radius = radius;
    }

    @Override
    public void interact(Particle particle) {
        Vector normal;

        toInnerReferenceFrame(particle);

        try {
            NeutralParticle p = (NeutralParticle) particle;
            CartesianPoint newCoordinate = getHitPoint(p);

            while (!p.isAbsorbed() && isPointInside(newCoordinate)) {
                normal = getNormal(newCoordinate).getNewByTurningAroundVector(
                        generator().uniformDouble(0.0, roughnessAngleR),
                        new Vector(1.0, 0.0, 0.0).getNewByTurningAroundOY(generator().uniformDouble(0.0, 2.0 * PI)));

                double angleWithSurface = p.getSpeed().getAngle(normal) - PI / 2.0;
                p.decreaseIntensity(reflectivity);

                if (angleWithSurface <= criticalAngleR) {
                    p.setCoordinate(newCoordinate);
                    p.getSpeed().turnAroundVector(
                            2.0 * Math.abs(angleWithSurface),
                            getParticleSpeedRotationAxis(newCoordinate, normal));
                    newCoordinate = getHitPoint(p);
                } else {
                    p.setAbsorbed(true);
                    break;
                }
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        toGlobalReferenceFrame(particle);
    }

    @Override //TODO работает некорректно
    public boolean willParticleGetInside(final Particle p) {
        double x0 = front.getX();

        double x = p.getCoordinate().getX();
        double y = p.getCoordinate().getY();
        double z = p.getCoordinate().getZ();

        double Vx = p.getSpeed().getX();
        double Vy = p.getSpeed().getY();
        double Vz = p.getSpeed().getZ();

        double newY = (Vy / Vx) * (x0 - x) + y;
        double newZ = (Vz / Vx) * (x0 - x) + z;

        double radiusY = radius * Math.cos(position.getTheta());
        double radiusZ = radius * Math.cos(position.getPhi());

        return  (newY - front.getY()) * (newY - front.getY()) / (radiusY * radiusY) +
                (newZ - front.getZ()) * (newZ - front.getZ()) / (radiusZ * radiusZ) < 1.0;
    }

    @Override
    public SphericalPoint getPosition() {
        return position;
    }

    protected abstract boolean isPointInside(CartesianPoint point);

    protected abstract void toInnerReferenceFrame(Particle particle);

    protected abstract void toGlobalReferenceFrame(Particle particle);
}