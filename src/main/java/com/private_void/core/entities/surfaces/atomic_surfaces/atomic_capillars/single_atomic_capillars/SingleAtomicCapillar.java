package com.private_void.core.entities.surfaces.atomic_surfaces.atomic_capillars.single_atomic_capillars;

import com.private_void.app.notifiers.Logger;
import com.private_void.app.notifiers.MessagePool;
import com.private_void.app.notifiers.ProgressProvider;
import com.private_void.core.entities.detectors.Detector;
import com.private_void.core.entities.detectors.Distribution;
import com.private_void.core.entities.fluxes.Flux;
import com.private_void.core.entities.particles.ChargedParticle;
import com.private_void.core.entities.particles.Particle;
import com.private_void.core.entities.surfaces.CapillarSystem;
import com.private_void.core.entities.surfaces.atomic_surfaces.AtomicSurface;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.utils.Interaction;

import static com.private_void.core.constants.Constants.ELECTRON_CHARGE;

public abstract class SingleAtomicCapillar extends AtomicSurface implements CapillarSystem {
    protected final double length;
    protected final double radius;
    protected Detector detector;

    private volatile int particleCounter;
    private final Object lock = new Object();

    public SingleAtomicCapillar(final CartesianPoint front, double radius, double length, double period,
                                double chargeNumber) {
        super(front, period, chargeNumber);
        this.radius = radius;
        this.length = length;
    }

    @Override
    public Distribution interact(Flux flux) {
        Logger.info(MessagePool.interactionStart());

        setCriticalAngle((ChargedParticle) flux.getParticles().get(0));
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
                        Particle.State state;

                        if (willParticleGetInside(particle)) {
                            state = new Particle.State(particle.getCoordinate(), particle.getSpeed());

                            while (!particle.isAbsorbed() && isPointInside(state.getCoordinate())) {
                                double angleWithAxis = state.getSpeed().getAngle(getAxis(state.getCoordinate()));

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
                }).start();

        Logger.info(MessagePool.interactionFinish());

        return detector.detect(flux);
    }

    double getForce(double distance, double particleChargeNumber) {
        double C2 = C_SQUARE * shieldingDistance * shieldingDistance * 10_000;
        double sum = C2 + radius * radius + distance * distance;
        double sqrt = Math.sqrt(sum * sum - 4.0 * radius * radius * distance * distance);

        double coefficient = - 2.0 * Math.PI * particleChargeNumber * chargeNumber * ELECTRON_CHARGE * ELECTRON_CHARGE *
                radius / (period * period);

        double numerator = (2.0 * distance * sum - 4.0 * radius * radius * distance) / sqrt + 2.0 * distance;

        double denominator = sqrt + sum;

        return coefficient * numerator / denominator;
    }

    private boolean willParticleGetInside(final ChargedParticle p) {
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

    protected abstract boolean isPointInside(final CartesianPoint point);

    protected abstract Detector createDetector();
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