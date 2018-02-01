package com.private_void.core.fluxes;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.Particle;
import com.private_void.core.particles.ParticleFactory;

import java.util.LinkedList;
import java.util.List;

import static com.private_void.utils.Generator.generator;

public class ParallelFlux extends Flux {
    private int layersAmount;
    private float layerDistance;

    public ParallelFlux(final ParticleFactory factory, final Point3D fluxCoordinate, final Vector3D fluxAxis,
                        int layersAmount, int particlesAmount, float layerDistance, float minIntensity) {

        super(factory, fluxCoordinate, fluxAxis, particlesAmount, minIntensity);
        this.layersAmount = layersAmount;
        this.layerDistance = layerDistance;
        createParticles();
    }

    @Override
    protected void createParticles() {
        List<Particle> newParticles = new LinkedList<>();
        for (int i = 0; i < layersAmount; i++) {
            for (int j = 0; j < particlesAmount; j++) {
                Point3D particleCoordinate = generator().gauss(0.0f, 1.0f);
                particleCoordinate.setX(fluxCoordinate.getX() - i * layerDistance);
                particleCoordinate.setY(particleCoordinate.getY() + fluxCoordinate.getY() - i * fluxAxis.getY());
                particleCoordinate.setZ(particleCoordinate.getZ() + fluxCoordinate.getZ() - i * fluxAxis.getZ());
                newParticles.add(particleFactory.getNewParticle(particleCoordinate, fluxAxis));
            }
        }
        particles = newParticles;
    }

}
