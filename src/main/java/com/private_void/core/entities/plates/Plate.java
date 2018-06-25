package com.private_void.core.entities.plates;

import com.private_void.app.notifiers.Logger;
import com.private_void.app.notifiers.MessagePool;
import com.private_void.app.notifiers.ProgressProvider;
import com.private_void.core.entities.detectors.Detector;
import com.private_void.core.entities.detectors.Distribution;
import com.private_void.core.entities.fluxes.Flux;
import com.private_void.core.entities.particles.Particle;
import com.private_void.core.entities.surfaces.Capillar;
import com.private_void.core.entities.surfaces.CapillarSystem;
import com.private_void.core.exceptions.BadParticleException;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.coordinates.Point3D;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;
import com.private_void.core.math.utils.Interaction;

import java.util.ArrayList;
import java.util.List;

public abstract class Plate implements CapillarSystem {
    protected final CartesianPoint center;
    protected final double capillarRadius;
    protected final double capillarsDensity;

    protected int capillarsAmount;
    protected List<Capillar> capillars;
    protected Detector detector;

    private volatile int particleCounter;
    private final Object lock = new Object();

    public Plate(final CartesianPoint center, double radius, double capillarsDensity) {
        this.center = center;
        this.capillarRadius = radius;
        this.capillarsDensity = capillarsDensity;
        this.capillars = new ArrayList<>();
    }

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

                        Particle particle = particles.get(i);

                        for (Capillar capillar : capillars) {
                            CartesianPoint coordinateInGlobalRefFrame = new CartesianPoint(particle.getCoordinate());
                            Vector speedInGlobalRefFrame = Vector.set(particle.getSpeed());

                            capillar.getReferenceFrameConverter().convert(particle);

                            if (capillar.willParticleGetInside(particle)) {
                                try {
                                    capillar.interact(particle);
                                } catch (BadParticleException e) {
                                    Logger.warning(MessagePool.particleDeleted());
                                    particle.delete();
                                }

                                capillar.getReferenceFrameConverter().convertBack(particle);
                                particle
                                        .calculateExpansionAngle(Vector.E_X)
                                        .setChanneled();
                                break;
                            }

                            particle.setCoordinate(coordinateInGlobalRefFrame);
                            particle.setSpeed(speedInGlobalRefFrame);
                        }
                    }
                }).start();

        Logger.info(MessagePool.interactionFinish());

        return detector.detect(flux);
    }

    protected abstract CartesianPoint getDetectorsCoordinate();

    protected abstract void createCapillars();

    protected abstract boolean isCapillarCoordinateValid(final Point3D[] coordinates, Point3D coordinate);
}

//    @Override
//    public Distribution interact(Flux flux) {
//        Logger.info(MessagePool.interactionStart());
//
//        List<? extends Particle> particles = flux.getParticles();
//        int particlesCounter = 0;
//        int tenPercentOfParticlesAmount = particles.size() / 10;
//
//        for (Particle particle : particles) {
//            if (++particlesCounter % tenPercentOfParticlesAmount == 0.0) {
//                ProgressProvider.getInstance().setProgress(
//                        particleCounter * 10 / tenPercentOfParticlesAmount);
//            }
//
//            for (Capillar capillar : capillars) {
//                CartesianPoint coordinateInGlobalRefFrame = new CartesianPoint(particle.getCoordinate());
//                Vector speedInGlobalRefFrame = Vector.set(particle.getSpeed());
//
//                capillar.getReferenceFrameConverter().convert(particle);
//
//                if (capillar.willParticleGetInside(particle)) {
//                    try {
//                        capillar.interact(particle);
//                    } catch (BadParticleException e) {
//                        Logger.warning(MessagePool.particleDeleted());
//                        particle.delete();
//                    }
//
//                    capillar.getReferenceFrameConverter().convertBack(particle);
//                    particle.setChanneled();
//                    break;
//                }
//
//                particle.setCoordinate(coordinateInGlobalRefFrame);
//                particle.setSpeed(speedInGlobalRefFrame);
//            }
//        }
//
//        Logger.info(MessagePool.interactionFinish());
//
//        return detector.detect(flux);
//    }