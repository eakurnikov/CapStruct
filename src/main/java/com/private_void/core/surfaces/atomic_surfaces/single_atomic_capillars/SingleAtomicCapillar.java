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

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public abstract class SingleAtomicCapillar extends AtomicSurface implements CapillarSystem {
    protected final List<AtomicChain> atomicChains;
    protected final int atomicChainsAmount;
    protected final double length;
    protected final double radius;
    protected Detector detector;

    public SingleAtomicCapillar(final AtomicChain.Factory factory, final CartesianPoint front, int atomicChainsAmount,
                                double chargeNumber, double radius, double length) {
        super(front, factory.getPeriod(), chargeNumber);
        this.atomicChainsAmount = atomicChainsAmount;
        this.radius = radius;
        this.length = length;
        this.atomicChains = createAtomicChains(factory);
    }

    @Override
    public Distribution interact(Flux flux) {
        Logger.interactionStart();

        class Interaction extends RecursiveAction {
            private List<? extends Particle> particles;
            private int startIndex;
            private int length;

            public Interaction(List<? extends Particle> particles, int startIndex, int length) {
                this.particles = particles;
                this.startIndex = startIndex;
                this.length = length;
            }

            private void interact() {
                for (int i = startIndex; i < startIndex + length; i++) {
                    ChargedParticle particle = (ChargedParticle) particles.get(i);
                    CartesianPoint newCoordinate;
                    Vector newSpeed;
                    double angleWithAxis;

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
            }

            @Override
            protected void compute() {
                if (length < particles.size() / 64.0) {
                    interact();
                } else {
                    int newLength = length / 2;
                    invokeAll(
                            new Interaction(particles, startIndex, newLength),
                            new Interaction(particles,startIndex + newLength, length - newLength));
                }
            }
        }

        List<? extends Particle> particles = flux.getParticles();
        Interaction interaction = new Interaction(particles, 0, particles.size());
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(interaction);

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

/*
public Distribution interactSingleThread(Flux flux) {
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

    public Distribution interactParallel(Flux flux) {
        Logger.interactionStart();

        setShieldingDistance(((ChargedParticle) flux.getParticles().get(0)).getChargeNumber());

        ExecutorService exec = Executors.newFixedThreadPool(4);

        class Interaction implements Runnable {
            private ChargedParticle particle;
            private CartesianPoint newCoordinate;
            private Vector newSpeed;
            private double angleWithAxis;

            public Interaction(ChargedParticle particle) {
                this.particle = particle;
            }

            @Override
            public void run() {
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

                Thread.yield();
            }
        }

        for (Iterator<? extends Particle> iterator = flux.getParticles().iterator(); iterator.hasNext(); ) {
            exec.execute(new Interaction((ChargedParticle) iterator.next()));
        }
        exec.shutdown();

        try {
            exec.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.interactionFinish();

        return detector.detect(flux);
    }
 */