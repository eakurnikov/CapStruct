package com.private_void.core.entities.fluxes;

import com.private_void.app.notifiers.Logger;
import com.private_void.app.notifiers.MessagePool;
import com.private_void.core.entities.particles.Particle;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;

import java.util.ArrayList;
import java.util.List;

import static com.private_void.core.constants.Constants.PI;
import static com.private_void.core.math.generators.Generator.generator;

public class DivergentFlux extends Flux {

    public DivergentFlux(final Particle.Factory particleFactory, final CartesianPoint.Factory coordinateFactory,
                         final CartesianPoint fluxCoordinate, final Vector fluxAxis,
                         int particlesAmount, double minIntensity) {

        super(particleFactory, coordinateFactory, fluxCoordinate, fluxAxis, particlesAmount, minIntensity);
        createParticles();
    }

    @Override
    protected void createParticles() {
        Logger.info(MessagePool.fluxCreationStart());

        List<Particle> newParticles = new ArrayList<>();

        for (int i = 0; i < particlesAmount; i++) {
            Vector axis = Vector
                    .set(-(fluxAxis.getY() + fluxAxis.getZ()) / fluxAxis.getX(), 1.0, 1.0)
                    .rotateAroundVector(fluxAxis, generator().uniformDouble(0.0, 2.0 * PI));

            newParticles.add(particleFactory.getNewParticle(
                    fluxCoordinate,
                    fluxAxis.rotateAroundVector(axis, coordinateFactory.getCoordinate().getY())));
        }

        particles = newParticles;

        Logger.info(MessagePool.fluxCreationFinish());
    }
}