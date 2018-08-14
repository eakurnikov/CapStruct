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

    private ParallelFlux(final Particle.Factory particleFactory, final CartesianPoint.Factory coordinateFactory,
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Particle.Factory particleFactory;
        private CartesianPoint.Factory coordinateFactory;
        private CartesianPoint fluxCoordinate;
        private Vector fluxAxis;
        private int layersAmount;
        private int particlesAmount;
        private double layerDistance;
        private double minIntensity;

        private Builder() {
            particleFactory = null;
            coordinateFactory = null;
            fluxCoordinate = null;
            fluxAxis = null;

            layersAmount = 0;
            particlesAmount = 0;
            layerDistance = 0.0d;
            minIntensity = 0.0d;
        }

        public Builder setParticleFactory(final Particle.Factory particleFactory) {
            this.particleFactory = particleFactory;
            return this;
        }

        public Builder setCoordinateFactory(final CartesianPoint.Factory coordinateFactory) {
            this.coordinateFactory = coordinateFactory;
            return this;
        }

        public Builder setFluxCoordinate(final CartesianPoint fluxCoordinate) {
            this.fluxCoordinate = fluxCoordinate;
            return this;
        }

        public Builder setFluxAxis(final Vector fluxAxis) {
            this.fluxAxis = fluxAxis;
            return this;
        }

        public Builder setLayersAmount(int layersAmount) {
            this.layersAmount = layersAmount;
            return this;
        }

        public Builder setParticlesAmount(int particlesAmount) {
            this.particlesAmount = particlesAmount;
            return this;
        }

        public Builder setLayerDistance(double layerDistance) {
            this.layerDistance = layerDistance;
            return this;
        }

        public Builder setMinIntensity(double minIntensity) {
            this.minIntensity = minIntensity;
            return this;
        }

        public ParallelFlux build() {
            return new ParallelFlux(particleFactory, coordinateFactory, fluxCoordinate, fluxAxis, layersAmount,
                    particlesAmount, layerDistance, minIntensity);
        }
    }
}