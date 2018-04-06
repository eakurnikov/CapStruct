package com.private_void.core.plates;

import com.private_void.core.detectors.Detector;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.coordinates.Point3D;
import com.private_void.core.geometry.coordinates.SphericalPoint;
import com.private_void.core.geometry.vectors.Vector;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.CapillarSystem;

import java.util.ArrayList;
import java.util.List;

public abstract class Plate implements CapillarSystem {
    protected static final int CAPILLARS_PER_DOMAIN_AMOUNT = 4;

    protected final CartesianPoint center;
    protected final double width;
    protected final double capillarRadius;
    protected final double capillarsDensity;


    protected int capillarsAmount;
    protected List<Capillar> capillars;
    protected Detector detector;

    public Plate(final CartesianPoint center, double radius, double length, double capillarsDensity) {
        this.center = center;
        this.width = length;
        this.capillarRadius = radius;
        this.capillarsDensity = capillarsDensity;
        this.capillars = new ArrayList<>();
    }

    public void testInteract(Flux flux) {
        System.out.println("Particles-capillars interaction start ...");
        long start = System.nanoTime();

        int particlesCounter = 0;
        int particlesAmount = flux.getParticles().size();

        for (Particle particle : flux.getParticles()) {
            if (particlesCounter % (particlesAmount / 10) == 0.0) System.out.println("    ... " + (particlesCounter * 100 / particlesAmount) + "% paricles processed");
            particlesCounter++;

            SphericalPoint position = new SphericalPoint(1_000, Math.toRadians(45.0), Math.toRadians(45.0));

            particle
                    .rotateRefFrameAroundVector(Vector.E_Y, -position.getTheta())
                    .rotateRefFrameAroundVector(Vector.E_Z, -position.getPhi());

            particle
                    .rotateRefFrameAroundVector(Vector.E_Z, position.getPhi())
                    .rotateRefFrameAroundVector(Vector.E_Y, position.getTheta());
        }

        long finish = System.nanoTime();
        System.out.println("Particles-capillars interaction finish. Total time = " + (finish - start) / 1_000_000 + " ms");
        System.out.println();
    }

    @Override
    public void interact(Flux flux) {
        System.out.println("Particles-capillars interaction start ...");
        long start = System.nanoTime();

        boolean isOut;

        int particlesCounter = 0;
        int particlesAmount = flux.getParticles().size();

        for (Particle particle : flux.getParticles()) {
            isOut = true;

            if (particlesCounter % (particlesAmount / 10) == 0.0) {
                System.out.println("    ... " + (particlesCounter * 100 / particlesAmount) + "% paricles processed");
            }
            particlesCounter++;

            int capillarsCounter = 0;

            for (Capillar capillar : capillars) {
                capillarsCounter++;

                if (capillar.willParticleGetInside(particle)) {
                    capillar.interact(particle);
                    isOut = false;
                    break;
                }
            }

            particle.setOut(isOut);
        }

        long finish = System.nanoTime();
        System.out.println("Particles-capillars interaction finish. Total time = " + (finish - start) / 1_000_000 + " ms");
        System.out.println();
    }

    @Override
    public Detector getDetector() {
        return detector;
    }

    public List<Capillar> getCapillars() {
        return capillars;
    }

    protected abstract CartesianPoint getDetectorsCoordinate();

    protected abstract void createCapillars();

    protected abstract boolean isCapillarCoordinateValid(final Point3D[] coordinates, Point3D coordinate);
}