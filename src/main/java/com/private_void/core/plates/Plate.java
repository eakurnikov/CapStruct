package com.private_void.core.plates;

import com.private_void.app.Logger;
import com.private_void.core.detectors.Detector;
import com.private_void.core.detectors.Distribution;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.coordinates.Point3D;
import com.private_void.core.geometry.space_3D.vectors.Vector;
import com.private_void.utils.Interaction;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.CapillarSystem;

import java.util.ArrayList;
import java.util.List;

public abstract class Plate implements CapillarSystem {
    protected static final int CAPILLARS_PER_DOMAIN_AMOUNT = 4;

    protected final CartesianPoint center;
    protected final double capillarRadius;
    protected final double capillarsDensity;

    protected int capillarsAmount;
    protected List<Capillar> capillars;
    protected Detector detector;

    public Plate(final CartesianPoint center, double radius, double capillarsDensity) {
        this.center = center;
        this.capillarRadius = radius;
        this.capillarsDensity = capillarsDensity;
        this.capillars = new ArrayList<>();
    }

    public Distribution interact(Flux flux) {
        Logger.interactionStart();

        new Interaction(
                flux.getParticles(),
                0,
                flux.getParticles().size(),
                (particles, startIndex, length) -> {
                    for (int i = startIndex; i < startIndex + length; i++) {
                        Particle particle = particles.get(i);

                        for (Capillar capillar : capillars) {
                            CartesianPoint coordinateInGlobalRefFrame = new CartesianPoint(particle.getCoordinate());
                            Vector speedInGlobalRefFrame = Vector.set(particle.getSpeed());

                            capillar.getReferenceFrameConverter().convert(particle);

                            if (capillar.willParticleGetInside(particle)) {
                                capillar.interact(particle);
                                capillar.getReferenceFrameConverter().convertBack(particle);
                                particle.setChanneled();
                                break;
                            }

                            particle.setCoordinate(coordinateInGlobalRefFrame);
                            particle.setSpeed(speedInGlobalRefFrame);
                        }
                    }
                }).start();

        Logger.interactionFinish();

        return detector.detect(flux);
    }

    protected abstract CartesianPoint getDetectorsCoordinate();

    protected abstract void createCapillars();

    protected abstract boolean isCapillarCoordinateValid(final Point3D[] coordinates, Point3D coordinate);
}

/*
//    @Override
    public Distribution interactByCapillars(Flux flux) {
        Logger.interactionStart();

        ReferenceFrame.Converter converter;

        int capillarsCounter = 0;
        int tenPercentOfCapillarsAmount = capillarsAmount / 10;

        for (Capillar capillar : capillars) {
            converter = capillar.getReferenceFrameConverter();
//            converter = new ReferenceFrame.Converter(capillar.getReferenceFrame());

            if (++capillarsCounter % tenPercentOfCapillarsAmount == 0.0) {
                Logger.processedCapillarsPercent(capillarsCounter * 10 / tenPercentOfCapillarsAmount);
            }

            for (Particle particle : flux.getParticles()) {
                if (particle.isChanneled()) {
                    continue;
                }

                CartesianPoint coordinateInGlobalRefFrame = new CartesianPoint(particle.getCoordinate());
                Vector speedInGlobalRefFrame = Vector.set(particle.getSpeed());

                converter.convert(particle);

                if (capillar.willParticleGetInside(particle)) {
                    capillar.interact(particle);
                    converter.convertBack(particle);
                    particle.setChanneled();
                    continue;
                }

                particle.setCoordinate(coordinateInGlobalRefFrame);
                particle.setSpeed(speedInGlobalRefFrame);
            }
        }

        Logger.interactionFinish();

        return detector.detect(flux);
    }

//    @Override
    public Distribution interactByParticles(Flux flux) {
        Logger.interactionStart();

//        Flux.ParticleProducer producer = new Flux.ParticleProducer(flux);

        ReferenceFrame.Converter converter;

        List<? extends Particle> particles = flux.getParticles();
        int particlesCounter = 0;
        int tenPercentOfParticlesAmount = particles.size() / 10;

        for (Particle particle : particles) {
            if (++particlesCounter % tenPercentOfParticlesAmount == 0.0) {
                Logger.processedParticlesPercent(particlesCounter * 10 / tenPercentOfParticlesAmount);
            }

            for (Capillar capillar : capillars) {
                converter = capillar.getReferenceFrameConverter();
//                converter = new ReferenceFrame.Converter(capillar.getReferenceFrame());

                CartesianPoint coordinateInGlobalRefFrame = new CartesianPoint(particle.getCoordinate());
                Vector speedInGlobalRefFrame = Vector.set(particle.getSpeed());

                converter.convert(particle);

                if (capillar.willParticleGetInside(particle)) {
                    capillar.interact(particle);
                    converter.convertBack(particle);
                    particle.setChanneled();
                    break;
                }

                particle.setCoordinate(coordinateInGlobalRefFrame);
                particle.setSpeed(speedInGlobalRefFrame);
            }
        }

        Logger.interactionFinish();

        return detector.detect(flux);
    }

 */