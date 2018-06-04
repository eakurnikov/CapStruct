package com.private_void.core.entities.fluxes;

import com.private_void.app.notifiers.Logger;
import com.private_void.app.notifiers.MessagePool;
import com.private_void.core.entities.particles.Particle;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;

import java.util.ArrayList;
import java.util.List;

public class ParallelFlux extends Flux {
    private final int layersAmount;
    private final double layerDistance;

    public ParallelFlux(final Particle.Factory particleFactory, final CartesianPoint.Factory coordinateFactory,
                        final CartesianPoint fluxCoordinate, final Vector fluxAxis, int layersAmount,
                        int particlesAmount, double layerDistance, double minIntensity) {

        super(particleFactory, coordinateFactory, fluxCoordinate, fluxAxis, particlesAmount, minIntensity);
        this.layersAmount = layersAmount;
        this.layerDistance = layerDistance;
        createParticles();
    }

    @Override
    protected void createParticles() {
        Logger.info(MessagePool.fluxCreationStart());

        List<Particle> newParticles = new ArrayList<>();

        for (int i = 0; i < layersAmount; i++) {
            for (int j = 0; j < particlesAmount; j++) {
                CartesianPoint randomCoordinate = coordinateFactory.getCoordinate();

                CartesianPoint particleCoordinate = new CartesianPoint(
                        fluxCoordinate.getX() - i * layerDistance,
                        randomCoordinate.getY() + fluxCoordinate.getY() - i * fluxAxis.getY(),
                        randomCoordinate.getZ() + fluxCoordinate.getZ() - i * fluxAxis.getZ());

                newParticles.add(particleFactory.getNewParticle(particleCoordinate, Vector.set(fluxAxis)));
            }
        }

        particles = newParticles;

        Logger.info(MessagePool.fluxCreationFinish());
    }
}