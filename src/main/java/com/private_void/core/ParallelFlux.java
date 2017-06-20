package com.private_void.core;

import com.private_void.utils.Generator;

public class ParallelFlux extends Flux{
    private int layersAmount;
    private float layerDistance;

    public ParallelFlux(final Point3D fluxCoordinate, final Vector3D fluxAxis, int layersAmount, int particlesAmount, float layerDistance, float minIntensity) {
        super(fluxCoordinate, fluxAxis, particlesAmount, minIntensity);
        this.layersAmount = layersAmount;
        this.layerDistance = layerDistance;
        createParticles();
    }

    @Override
    protected void createParticles() {
        for (int i = 0; i < layersAmount; i++) {
            for (int j = 0; j < particlesAmount; j++) {
                Point3D particleCoordinate = Generator.getInstance().gauss(0.0f, 1.0f);
                particleCoordinate.setX(fluxCoordinate.getX() - i * layerDistance);
                particleCoordinate.setY(particleCoordinate.getY() + fluxCoordinate.getY() - i * fluxAxis.getY());
                particleCoordinate.setZ(particleCoordinate.getZ() + fluxCoordinate.getZ() - i * fluxAxis.getZ());
                particles.add(new Particle(particleCoordinate, fluxAxis));
            }
        }
    }

}
