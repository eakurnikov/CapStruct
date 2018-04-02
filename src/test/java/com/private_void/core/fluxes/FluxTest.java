package com.private_void.core.fluxes;

import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.CoordinateFactory;
import com.private_void.core.geometry.Vector;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.core.particles.Particle;
import com.private_void.core.particles.ParticleFactory;
import com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars.SingleSmoothCylinder;
import com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.test.SingleSmoothCylinderTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.private_void.utils.Generator.generator;
import static org.junit.Assert.assertTrue;

public class FluxTest {

    @Test
    public void fluxesCompartionTest() {
        ParallelFlux particlesDonor;

        ParticleFactory neutralParticleFactory = NeutralParticle.getFactory(1.0);
        CoordinateFactory gaussDistribution = generator().getGaussDistribution(0.0, 1.0);

        particlesDonor = new ParallelFlux(
                neutralParticleFactory,
                gaussDistribution,
                new CartesianPoint(0.0, 0.0, 0.0),
                new Vector(1.0, -0.1, 0.0),
                10,
                1000,
                1.0,
                0.5);

        List<? extends Particle> particles = particlesDonor.getParticles();
        List<? extends Particle> interactedParticles1 = interactionWithTurningTest(particles);
        List<? extends Particle> interactedParticles2 = interactionWithGetNewByTurningTest(particles);

        for (int i = 0; i < interactedParticles1.size(); i++) {
            if (!interactedParticles1.get(i).isAbsorbed() && !interactedParticles2.get(i).isAbsorbed()) {
                CartesianPoint p1 = interactedParticles1.get(i).getCoordinate();
                CartesianPoint p2 = interactedParticles2.get(i).getCoordinate();

                System.out.println(p1.getX() + " " + p1.getY() + " " + p1.getZ());
                System.out.println(p2.getX() + " " + p2.getY() + " " + p2.getZ());
                System.out.println();

                assertTrue(p1.getX() == p2.getX());
                assertTrue(p1.getY() == p2.getY());
                assertTrue(p1.getZ() == p2.getZ());
            }
        }
    }

    private List<? extends Particle> interactionWithGetNewByTurningTest(List<? extends Particle> particles) {
        ParallelFlux flux = new ParallelFlux(
                NeutralParticle.getFactory(1.0),
                generator().getGaussDistribution(0.0, 1.0),
                new CartesianPoint(0.0, 0.0, 0.0),
                new Vector(1.0, -0.1, 0.0),
                10,
                1000,
                1.0,
                0.5);
        flux.setParticles(copyParticles(particles));

        SingleSmoothCylinder capillar = new SingleSmoothCylinder(
                new CartesianPoint(0.0, 0.0, 0.0),
                20.0,
                1000.0,
                0.0,
                0.0,
                1.0,
                90.0);

        System.out.println("SingleSmoothCylinder interacton");
        capillar.interact(flux);
        capillar.getDetector().detect(flux);

        return flux.getParticles();
    }

    private List<? extends Particle> interactionWithTurningTest(List<? extends Particle> particles) {
        ParallelFlux flux = new ParallelFlux(
                NeutralParticle.getFactory(1.0),
                generator().getGaussDistribution(0.0, 1.0),
                new CartesianPoint(0.0, 0.0, 0.0),
                new Vector(1.0, -0.1, 0.0),
                10,
                1000,
                1.0,
                0.5);
        flux.setParticles(copyParticles(particles));

        SingleSmoothCylinderTest capillar = new SingleSmoothCylinderTest(
                new CartesianPoint(0.0, 0.0, 0.0),
                20.0,
                1000.0,
                0.0,
                0.0,
                1.0,
                90.0);

        System.out.println("SingleSmoothCylinderTest interacton");
        capillar.interact(flux);
        capillar.getDetector().detect(flux);

        return flux.getParticles();
    }

    private List<? extends Particle> copyParticles(List<? extends Particle> particles) {
        List<Particle> copiedParticles = new ArrayList<>();
        for (Particle p : particles) {
            copiedParticles.add(NeutralParticle
                    .getFactory(1.0)
                    .getNewParticle(
                            new CartesianPoint(p.getCoordinate().getX(), p.getCoordinate().getY(), p.getCoordinate().getZ()),
                            new Vector(p.getSpeed().getX(), p.getSpeed().getY(), p.getSpeed().getZ())));
        }
        return copiedParticles;
    }
}