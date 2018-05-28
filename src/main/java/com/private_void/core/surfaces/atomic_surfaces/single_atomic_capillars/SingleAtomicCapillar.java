package com.private_void.core.surfaces.atomic_surfaces.single_atomic_capillars;

import com.private_void.core.detection.Detector;
import com.private_void.core.detection.Distribution;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.vectors.Vector;
import com.private_void.core.particles.AtomicChain;
import com.private_void.core.particles.ChargedParticle;
import com.private_void.core.surfaces.CapillarSystem;
import com.private_void.core.surfaces.atomic_surfaces.AtomicSurface;
import com.private_void.utils.Interaction;
import com.private_void.utils.notifiers.Logger;
import com.private_void.utils.notifiers.MessagePool;
import com.private_void.utils.notifiers.ProgressProvider;

import java.util.List;

public abstract class SingleAtomicCapillar extends AtomicSurface implements CapillarSystem {
    protected final List<AtomicChain> atomicChains;
    protected final int atomicChainsAmount;
    protected final double length;
    protected final double radius;
    protected Detector detector;

    private volatile int particleCounter;
    private final Object lock = new Object();

    public SingleAtomicCapillar(final AtomicChain.Factory factory, final CartesianPoint front, int atomicChainsAmount,
                                double chargeNumber, double radius, double length) {
        super(front, factory.getPeriod(), chargeNumber);
        this.atomicChainsAmount = atomicChainsAmount;
        this.radius = radius;
        this.length = length;
        this.atomicChains = createAtomicChains(factory);
    }

//    public Distribution interact(Flux flux) {
//        Logger.info(MessagePool.interactionStart());
//
//        ChargedParticle particle;
//        CartesianPoint newCoordinate;
//        Vector newSpeed;
//        double angleWithAxis;
//
//        setShieldingDistance(((ChargedParticle) flux.getParticles().get(0)).getChargeNumber());
//
//        for (Iterator<? extends Particle> iterator = flux.getParticles().iterator(); iterator.hasNext();) {
//            particle = (ChargedParticle) iterator.next();
//
//            if (willParticleGetInside(particle)) {
//                newCoordinate = particle.getCoordinate();
//                newSpeed = particle.getSpeed();
//
//                while (!particle.isAbsorbed() && isPointInside(newCoordinate)) {
//                    angleWithAxis = newSpeed.getAngle(getAxis(newCoordinate));
//
//                    if (angleWithAxis <= getCriticalAngle(particle)) {
//                        particle
//                                .setCoordinate(newCoordinate)
//                                .setSpeed(newSpeed);
//
//                        newSpeed = rotateParticleSpeed(particle);
//                        newCoordinate = newCoordinate.shift(newSpeed);
//                    } else {
//                        particle.absorb();
//                        break;
//                    }
//                }
//
//                particle.setChanneled();
//            }
//        }
//
//        Logger.info(MessagePool.interactionFinish());
//
//        return detector.detect(flux);
//    }

    @Override
    public Distribution interact(Flux flux) {
        Logger.info(MessagePool.interactionStart());

        particleCounter = 0;
        final int tenPercentOfParticlesAmount = flux.getParticles().size() / 10;

        new Interaction(
                flux.getParticles(),
                0,
                flux.getParticles().size(),
                (particles, startIndex, length) -> {
                    for (int i = startIndex; i < startIndex + length; i++) {
                        synchronized (lock) {
                            if (++particleCounter % tenPercentOfParticlesAmount == 0.0) {
                                ProgressProvider.getInstance().setProgress(
                                        particleCounter * 10 / tenPercentOfParticlesAmount);
                            }
                        }

                        ChargedParticle particle = (ChargedParticle) particles.get(i);
                        CartesianPoint newCoordinate;
                        Vector newSpeed;

                        if (willParticleGetInside(particle)) {
                            newCoordinate = particle.getCoordinate();
                            newSpeed = particle.getSpeed();

                            while (!particle.isAbsorbed() && isPointInside(newCoordinate)) {
                                double angleWithAxis = newSpeed.getAngle(getAxis(newCoordinate));

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
                }).start();

        Logger.info(MessagePool.interactionFinish());

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