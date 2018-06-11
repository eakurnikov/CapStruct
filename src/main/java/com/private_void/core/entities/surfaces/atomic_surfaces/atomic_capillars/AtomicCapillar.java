package com.private_void.core.entities.surfaces.atomic_surfaces.atomic_capillars;

import com.private_void.core.entities.particles.AtomicChain;
import com.private_void.core.entities.particles.ChargedParticle;
import com.private_void.core.entities.particles.Particle;
import com.private_void.core.entities.surfaces.Capillar;
import com.private_void.core.entities.surfaces.atomic_surfaces.AtomicSurface;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.reference_frames.ReferenceFrame;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;

import java.util.List;

public abstract class AtomicCapillar extends AtomicSurface implements Capillar {
    protected final ReferenceFrame.Converter refFrameConverter;
    protected final List<AtomicChain> atomicChains;
    protected final int atomicChainsAmount;
    protected final double radius;
    protected final double length;

    public AtomicCapillar(final CartesianPoint front, final ReferenceFrame refFrame,
                          final AtomicChain.Factory chainFactory, int atomicChainsAmount, double chargeNumber,
                          double radius, double length) {
        super(front, chainFactory.getPeriod(), chargeNumber);
        this.refFrameConverter = new ReferenceFrame.Converter(refFrame);
        this.atomicChainsAmount = atomicChainsAmount;
        this.radius = radius;
        this.length = length;
        this.atomicChains = createAtomicChains(chainFactory);
    }

    @Override
    public void interact(Particle p) {
        CartesianPoint newCoordinate;
        Vector newSpeed;
        double angleWithAxis;

        ChargedParticle particle = (ChargedParticle) p;
        setCriticalAngle(particle);

        if (willParticleGetInside(particle)) {
            newCoordinate = particle.getCoordinate();
            newSpeed = particle.getSpeed();

            while (!particle.isAbsorbed() && isPointInside(newCoordinate)) {
                angleWithAxis = newSpeed.getAngle(getAxis(newCoordinate));

                if (angleWithAxis <= criticalAngle) {
                    particle
                            .setCoordinate(newCoordinate)
                            .setSpeed(newSpeed);

//                    newSpeed = rotateParticleSpeed(particle);
//                    newCoordinate = newCoordinate.shift(newSpeed);

                    newSpeed = getNextParticleSpeed(particle);
                    newCoordinate = getNextParticleCoordinate(particle);
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

    protected abstract List<AtomicChain> createAtomicChains(final AtomicChain.Factory chainFactory);

    protected abstract boolean isPointInside(final CartesianPoint point);
}