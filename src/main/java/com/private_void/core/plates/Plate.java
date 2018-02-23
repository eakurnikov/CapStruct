package com.private_void.core.plates;

import com.private_void.core.detectors.Detector;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.CapillarFactory;

import java.util.List;

public abstract class Plate {
    protected CapillarFactory capillarFactory;
    protected Point3D center;
    protected float length;
    protected float width;
    protected float height;
    protected float capillarsDensity;
    protected float capillarRadius;
    protected List<Capillar> capillars;
    protected Detector detector;

    public Plate(final CapillarFactory capillarFactory, final Point3D center, float length, float width, float height,
                 float capillarsDensity, float capillarRadius) {
        this.capillarFactory = capillarFactory;
        this.center = center;
        this.length = length;
        this.width = width;
        this.height = height;
        this.capillarsDensity = capillarsDensity;
        this.capillarRadius = capillarRadius;
    }

    protected abstract void createCapillars();

    protected abstract Point3D getDetectorsCoordinate();

    protected abstract float getFrontSquare();

    protected void interact(Flux flux) {
        for (Particle particle : flux.getParticles()) {
            for (Capillar capillar : capillars) {
                if (capillar.willParticleGetInside(particle)) {
                    capillar.interact(particle);
                }
            }
        }

        detector.detect(flux);
    }

    public Detector getDetector() {
        return detector;
    }
}