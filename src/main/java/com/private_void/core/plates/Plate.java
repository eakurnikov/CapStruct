package com.private_void.core.plates;

import com.private_void.core.detectors.Detector;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.SphericalPoint;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.CapillarFactory;
import com.private_void.core.surfaces.CapillarSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Plate implements CapillarSystem {
    protected static final int CAPILLARS_PER_DOMAIN_AMOUNT = 4;
    protected CapillarFactory capillarFactory;
    protected Point3D center;
    protected float width;
    protected int capillarsAmount;
    protected float capillarsDensity;
    protected float capillarRadius;
    protected List<Capillar> capillars;
    protected Detector detector;

    public Plate(final CapillarFactory capillarFactory, final Point3D center, float capillarsDensity) {
        this.capillarFactory = capillarFactory;
        this.center = center;
        this.width = capillarFactory.getLength();
        this.capillarsDensity = capillarsDensity;
        this.capillarRadius = capillarFactory.getRadius();
        this.capillars = new ArrayList<>();
    }

    @Override
    public void interact(Flux flux) {
        long start = System.nanoTime();

        boolean isOut;

        for (Particle particle : flux.getParticles()) {
            isOut = true;

//            for (Capillar capillar : capillars) {
//                if (capillar.willParticleGetInside(particle)) {
//                    capillar.interact(particle);
//                    isOut = false;
//                    break;
//                }
//            }

            for (Capillar capillar : capillars) {
                float theta = capillar.getPosition().getTheta();
                float phi = capillar.getPosition().getPhi();
                particle.getSpeed()
                        .turnAroundVector(-theta, new Vector3D(0.0f, 0.0f, 1.0f))
                        .turnAroundVector(-phi, new Vector3D(0.0f, 1.0f, 0.0f));

                if (capillar.willParticleGetInside(particle)) {
                    capillar.interact(particle);

                    particle.getSpeed()
                            .turnAroundVector(theta, new Vector3D(0.0f, 0.0f, 1.0f))
                            .turnAroundVector(phi, new Vector3D(0.0f, 1.0f, 0.0f));

                    isOut = false;
                    break;
                }
            }

            particle.setOut(isOut);
        }

        long finish = System.nanoTime();
        System.out.println("Particles-capillars interaction time = " + (finish - start) / 1_000_000 + " ms");
    }

    @Override
    public Detector getDetector() {
        return detector;
    }

    protected abstract Point3D getDetectorsCoordinate();

    protected abstract void createCapillars();

    protected abstract boolean isCapillarCoordinateValid(final Point3D[] coordinates, Point3D coordinate);
}