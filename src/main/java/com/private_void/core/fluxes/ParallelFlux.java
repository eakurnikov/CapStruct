package com.private_void.core.fluxes;

import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.Vector;
import com.private_void.core.particles.Particle;
import com.private_void.core.particles.ParticleFactory;
import com.private_void.core.geometry.CoordinateFactory;

import java.util.ArrayList;
import java.util.List;

public class ParallelFlux extends Flux {
    private int layersAmount;
    private double layerDistance;

    public ParallelFlux(final ParticleFactory particleFactory, final CoordinateFactory coordinateFactory,
                        final CartesianPoint fluxCoordinate, final Vector fluxAxis, int layersAmount,
                        int particlesAmount, double layerDistance, double minIntensity) {

        super(particleFactory, coordinateFactory, fluxCoordinate, fluxAxis, particlesAmount, minIntensity);
        this.layersAmount = layersAmount;
        this.layerDistance = layerDistance;
        createParticles();
    }

    @Override
    protected void createParticles() {
        List<Particle> newParticles = new ArrayList<>();
        for (int i = 0; i < layersAmount; i++) {
            for (int j = 0; j < particlesAmount; j++) {
                CartesianPoint particleCoordinate = coordinateFactory.getCoordinate();
                particleCoordinate.setX(fluxCoordinate.getX() - i * layerDistance);
                particleCoordinate.setY(particleCoordinate.getY() + fluxCoordinate.getY() - i * fluxAxis.getY());
                particleCoordinate.setZ(particleCoordinate.getZ() + fluxCoordinate.getZ() - i * fluxAxis.getZ());
                newParticles.add(particleFactory.getNewParticle(particleCoordinate, new Vector(fluxAxis.getX(), fluxAxis.getY(), fluxAxis.getZ())));
            }
        }
        particles = newParticles;
    }
}