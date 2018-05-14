package com.private_void.core.surfaces.atomic_surfaces.atomic_capillars;

import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.reference_frames.ReferenceFrame;
import com.private_void.core.geometry.space_3D.vectors.Vector;
import com.private_void.core.particles.AtomicChain;
import com.private_void.core.particles.ChargedParticle;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.atomic_surfaces.AtomicSurface;

import java.util.List;

public abstract class AtomicCapillar extends AtomicSurface implements Capillar {
    protected final ReferenceFrame refFrame;
    protected final AtomicChain.Factory factory;
    protected final List<AtomicChain> atomicChains;
    protected final int atomicChainsAmount;
    protected final double length;
    protected final double radius;

    public AtomicCapillar(final AtomicChain.Factory factory, final CartesianPoint front, int atomicChainsAmount,
                          double chargeNumber, double radius, double length) {
        super(front, factory.getPeriod(), chargeNumber);
        this.refFrame = ReferenceFrame.builder().atPoint(front).build();
        this.factory = factory;
        this.atomicChainsAmount = atomicChainsAmount;
        this.atomicChains = createAtomicChains(factory);
        this.radius = radius;
        this.length = length;
    }

    @Override
    public void interact(Particle p) {
        CartesianPoint newCoordinate;
        Vector newSpeed;
        double angleWithAxis;

        ChargedParticle particle = (ChargedParticle) p;

        if (willParticleGetInside(particle)) {
            newCoordinate = particle.getCoordinate();
            newSpeed = particle.getSpeed();

            while (!particle.isAbsorbed() && isPointInside(newCoordinate)) {
                angleWithAxis = newSpeed.getAngle(getAxis(newCoordinate));

                if (angleWithAxis <= getCriticalAngle(particle)) {
                    particle
                            .setCoordinate(newCoordinate)
                            .setSpeed(newSpeed);

                    newSpeed = rotateParticleSpeed(particle);
                    newCoordinate = newCoordinate.shift(newSpeed);
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
    public ReferenceFrame getReferenceFrame() {
        return refFrame;
    }

    protected abstract List<AtomicChain> createAtomicChains(final AtomicChain.Factory factory);

    protected abstract boolean isPointInside(CartesianPoint point);
}