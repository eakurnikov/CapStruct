package com.private_void.core.entities.surfaces.atomic_surfaces.atomic_capillars;

import com.private_void.core.entities.particles.ChargedParticle;
import com.private_void.core.entities.particles.Particle;
import com.private_void.core.entities.surfaces.Capillar;
import com.private_void.core.entities.surfaces.atomic_surfaces.AtomicSurface;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.reference_frames.ReferenceFrame;

import static com.private_void.core.constants.Constants.ELECTRON_CHARGE;

public abstract class AtomicCapillar extends AtomicSurface implements Capillar {
    protected final ReferenceFrame.Converter refFrameConverter;
    protected final double radius;
    protected final double length;

    public AtomicCapillar(final CartesianPoint front, final ReferenceFrame refFrame, double radius, double length,
                          double period, double chargeNumber) {
        super(front, period, chargeNumber);
        this.refFrameConverter = new ReferenceFrame.Converter(refFrame);
        this.radius = radius;
        this.length = length;
    }

    @Override
    public void interact(Particle p) {
        Particle.State state;
        double angleWithAxis;

        ChargedParticle particle = (ChargedParticle) p;
        setCriticalAngle(particle);

        if (willParticleGetInside(particle)) {
            state = new Particle.State(particle.getCoordinate(), particle.getSpeed());

            while (!particle.isAbsorbed() && isPointInside(state.getCoordinate())) {
                angleWithAxis = state.getSpeed().getAngle(getAxis(state.getCoordinate()));

                if (angleWithAxis <= criticalAngle) {
                    particle.setState(state);
                    state = getParticlesNewState(state, particle.getChargeNumber(), particle.getMass());
                } else {
                    particle.absorb();
                    break;
                }
            }

            particle.setChanneled();
        }
    }

    @Override
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

        return newY * newY + newZ * newZ < radius * radius;
    }

    @Override
    public ReferenceFrame.Converter getReferenceFrameConverter() {
        return refFrameConverter;
    }

    protected double getForce(double distance, double particleChargeNumber) {
        double C2 = C_SQUARE * shieldingDistance * shieldingDistance * SCALE;
        double sum = C2 + radius * radius + distance * distance;
        double sqrt = Math.sqrt(sum * sum - 4.0 * radius * radius * distance * distance);

        double coefficient = - 2.0 * Math.PI * particleChargeNumber * chargeNumber * ELECTRON_CHARGE * ELECTRON_CHARGE *
                radius / (period * period);

        double numerator = (2.0 * distance * sum - 4.0 * radius * radius * distance) / sqrt + 2.0 * distance;

        double denominator = sqrt + sum;

        return coefficient * numerator / denominator;
    }

    protected abstract boolean isPointInside(final CartesianPoint point);
}