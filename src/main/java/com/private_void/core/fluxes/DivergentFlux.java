package com.private_void.core.fluxes;

import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.Vector;
import com.private_void.core.particles.Particle;
import com.private_void.core.particles.ParticleFactory;
import com.private_void.core.geometry.CoordinateFactory;

import java.util.ArrayList;
import java.util.List;

import static com.private_void.utils.Constants.PI;
import static com.private_void.utils.Generator.generator;

public class DivergentFlux extends Flux {

    public DivergentFlux(final ParticleFactory particleFactory, final CoordinateFactory coordinateFactory,
                         final CartesianPoint fluxCoordinate, final Vector fluxAxis,
                         int particlesAmount, float minIntensity) {
        super(particleFactory, coordinateFactory, fluxCoordinate, fluxAxis, particlesAmount, minIntensity);
        createParticles();
    }

    @Override
    protected void createParticles() {
        List<Particle> newParticles = new ArrayList<>();
        for (int i = 0; i < particlesAmount; i++) {
            Vector axis = new Vector(-(fluxAxis.getY() + fluxAxis.getZ()) / fluxAxis.getX(), 1.0f, 1.0f)
                    .turnAroundVector(generator().uniformFloat(0.0f, 2.0f * PI), fluxAxis);
            newParticles.add(particleFactory.getNewParticle(fluxCoordinate,
                    fluxAxis.getNewByTurningAroundVector(coordinateFactory.getCoordinate().getY(), axis)));
        }
        particles = newParticles;
    }
}