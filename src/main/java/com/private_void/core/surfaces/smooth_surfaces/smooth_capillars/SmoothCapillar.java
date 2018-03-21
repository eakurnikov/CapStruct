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
        transformToReferenceFrame(particle, ReferenceFrame.INNER);

        try {
            NeutralParticle p = (NeutralParticle) particle;
            CartesianPoint newCoordinate = getHitPoint(p);

            while (!p.isAbsorbed() && isPointInside(newCoordinate)) {
                axis = new Vector(1.0, 0.0, 0.0)
                        .turnAroundOY(generator().uniformDouble(0.0, 2.0 * PI));
                normal = getNormal(newCoordinate)
                        .turnAroundVector(generator().uniformDouble(0.0, roughnessAngleR), axis);
                axis = getAxis(newCoordinate);

                double angleWithSurface = p.getSpeed().getAngle(normal) - PI / 2.0;
                p.decreaseIntensity(reflectivity);

                if (angleWithSurface <= criticalAngleR) {
                    p.setCoordinate(newCoordinate);
                    p.setSpeed(p.getSpeed().getNewByTurningAroundVector(2.0 * Math.abs(angleWithSurface), axis));
                    newCoordinate = getHitPoint(p);
                } else {
                    p.setAbsorbed(true);
                    break;
                }
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        transformToReferenceFrame(particle, ReferenceFrame.GLOBAL);
    }

    @Override //TODO протестить
    public boolean willParticleGetInside(final Particle p) {
        transformToReferenceFrame(p, ReferenceFrame.INNER);

        double x0 = front.getX();

        double x = p.getCoordinate().getX();
        double y = p.getCoordinate().getY();
        double z = p.getCoordinate().getZ();

        double Vx = p.getSpeed().getX();
        double Vy = p.getSpeed().getY();
        double Vz = p.getSpeed().getZ();

        double newY = (Vy / Vx) * (x0 - x) + y;
        double newZ = (Vz / Vx) * (x0 - x) + z;


        boolean result = (newY - front.getY()) * (newY - front.getY()) + (newZ - front.getZ()) * (newZ - front.getZ()) < radius * radius;

        transformToReferenceFrame(p, ReferenceFrame.GLOBAL);

        return result;
    }

    @Override
    public SphericalPoint getPosition() {
        return position;
    }

    protected abstract boolean isPointInside(CartesianPoint point);

    protected abstract void transformToReferenceFrame(Particle particle, ReferenceFrame frame);

    protected enum ReferenceFrame {
        INNER, GLOBAL
    }
}