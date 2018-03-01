package com.private_void.core.plates;

import com.private_void.core.detectors.Detector;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.CoordinateFactory;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.CapillarFactory;
import com.private_void.core.surfaces.CapillarSystem;

import java.util.ArrayList;
import java.util.List;

public abstract class Plate implements CapillarSystem {
    protected static final int CAPILLARS_PER_DOMAIN_AMOUNT = 4;

    protected CapillarFactory capillarFactory;
    protected Point3D center;
    protected float sideLength;
    protected float width;
    protected float capillarsAmount;
    protected float capillarsDensity;
    protected float capillarRadius;
    protected List<Capillar> capillars;
    protected Detector detector;

    public Plate(final CapillarFactory capillarFactory, final Point3D center, int capillarsAmount,
                 float capillarsDensity) {
        this.capillarFactory = capillarFactory;
        this.center = center;
        this.width = capillarFactory.getLength();
        this.capillarsAmount = capillarsAmount;
        this.capillarsDensity = capillarsDensity;
        this.capillarRadius = capillarFactory.getRadius();
        this.capillars = new ArrayList<>();
    }

    @Override
    public void interact(Flux flux) {
        long start = System.nanoTime();

        boolean isAbsorbed;
        for (Particle particle : flux.getParticles()) {
            isAbsorbed = true;
            for (Capillar capillar : capillars) {
                if (capillar.willParticleGetInside(particle)) {
                    capillar.interact(particle);
                    isAbsorbed = false;
                }
            }
            particle.setAbsorbed(!isAbsorbed);
        }

        long finish = System.nanoTime();
        System.out.println("time = " + (finish - start) / 1_000_000 + " ms");
    }

    @Override
    public Detector getDetector() {
        return detector;
    }

    protected abstract Point3D getDetectorsCoordinate();

    protected abstract void createCapillars() throws IllegalArgumentException;

    protected abstract boolean isCapillarCoordinateValid(final Point3D[] coordinates, Point3D coordinate);
}