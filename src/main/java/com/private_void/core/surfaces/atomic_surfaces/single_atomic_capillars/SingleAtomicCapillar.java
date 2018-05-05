package com.private_void.core.surfaces.atomic_surfaces.single_atomic_capillars;

import com.private_void.app.Logger;
import com.private_void.core.detectors.Detector;
import com.private_void.core.detectors.Distribution;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.vectors.Vector;
import com.private_void.core.particles.AtomicChain;
import com.private_void.core.particles.ChargedParticle;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.CapillarSystem;
import com.private_void.core.surfaces.atomic_surfaces.AtomicSurface;

import java.util.Iterator;
import java.util.List;

public abstract class SingleAtomicCapillar extends AtomicSurface implements CapillarSystem {
    protected final List<AtomicChain> atomicChains;
    protected final double length;
    protected final double radius;
    protected Detector detector;

    public SingleAtomicCapillar(final AtomicChain.Factory factory, final CartesianPoint front, double period,
                                double chargeNumber, double radius, double length) {
        super(front, period, chargeNumber);
        this.radius = radius;
        this.length = length;
        this.atomicChains = createAtomicChains(factory);
    }

    @Override
    public Distribution interact(Flux flux) {
        Logger.interactionStart();

        ChargedParticle particle;
        CartesianPoint newCoordinate;
        Vector newSpeed;
        double angleWithAxis;

        int particlesCounter = 0;
        int tenPercentOfParticlesAmount = flux.getParticles().size() / 10;

        setShieldingDistance(((ChargedParticle) flux.getParticles().get(0)).getChargeNumber());

        for (Iterator<? extends Particle> iterator = flux.getParticles().iterator(); iterator.hasNext(); particlesCounter++) {
            if (particlesCounter % tenPercentOfParticlesAmount == 0.0) {
                Logger.processedParticlesPercent(particlesCounter * 10 / tenPercentOfParticlesAmount);
            }

            particle = (ChargedParticle) iterator.next();

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

        Logger.interactionFinish();

        return detector.detect(flux);
    }

    public boolean willParticleGetInside(final ChargedParticle p) {
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

    protected abstract List<AtomicChain> createAtomicChains(final AtomicChain.Factory factory);

    protected abstract boolean isPointInside(final CartesianPoint point);

    protected abstract Detector createDetector();
}