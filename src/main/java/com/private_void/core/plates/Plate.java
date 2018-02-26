package com.private_void.core.plates;

import com.private_void.core.detectors.Detector;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.CoordinateFactory;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.CapillarFactory;
import com.private_void.core.surfaces.CapillarSystem;

import java.util.List;

public abstract class Plate implements CapillarSystem {
    protected CapillarFactory capillarFactory;
    protected CoordinateFactory coordinateFactory;
    protected Point3D center;
    protected float length;
    protected float width;
    protected float height;
    protected float capillarsDensity;
    protected float capillarRadius;
    protected List<Capillar> capillars;
    protected Detector detector;

    public Plate(final CapillarFactory capillarFactory, final CoordinateFactory coordinateFactory,
                 final Point3D center, float length, float height, float capillarsDensity) {
        this.capillarFactory = capillarFactory;
        this.coordinateFactory = coordinateFactory;
        this.center = center;
        this.length = length;
        this.width = capillarFactory.getLength();
        this.height = height;
        this.capillarsDensity = capillarsDensity;
        this.capillarRadius = capillarFactory.getRadius();
    }

    @Override
    public void interact(Flux flux) {
        for (Particle particle : flux.getParticles()) {
            for (Capillar capillar : capillars) {
                if (capillar.willParticleGetInside(particle)) {
                    capillar.interact(particle);
                } else {
                    particle.setAbsorbed(true);
                }
            }
        }
    }

    @Override
    public Detector getDetector() {
        return detector;
    }

    protected abstract Point3D getDetectorsCoordinate();

    protected abstract void createCapillars();

    protected abstract float getFrontSquare();
}