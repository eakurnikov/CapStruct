package com.private_void.core.plates;

import com.private_void.app.Logger;
import com.private_void.core.detectors.Detector;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.coordinates.Point3D;
import com.private_void.core.geometry.reference_frames.ReferenceFrame;
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

    @Override
    public void interact(Flux flux) {
        Logger.interactionStart();

        ReferenceFrame.Converter converter;

        int capillarsCounter = 0;
        int tenPercentOfCapillarsAmount = capillarsAmount / 10;

        for (Capillar capillar : capillars) {
            converter = new ReferenceFrame.Converter(capillar.getReferenceFrame());

            if (++capillarsCounter % tenPercentOfCapillarsAmount == 0.0) {
                Logger.processedCapillarsPercent(capillarsCounter * 10 / tenPercentOfCapillarsAmount);
            }

            for (Particle particle : flux.getParticles()) {
                if (particle.isInteracted()) {
                    continue;
                }

                converter.convert(particle);

                if (capillar.willParticleGetInside(particle)) {
                    capillar.interact(particle);
                    converter.convertBack(particle);
                    particle.setInteracted();
                    continue;
                }

                converter.convertBack(particle);
            }
        }

        Logger.interactionFinish();
    }

    @Override
    public Detector getDetector() {
        return detector;
    }

    protected abstract CartesianPoint getDetectorsCoordinate();

    protected abstract void createCapillars();

    protected abstract boolean isCapillarCoordinateValid(final Point3D[] coordinates, Point3D coordinate);
}