package com.private_void.core;

import com.private_void.utils.Generator;
import com.private_void.utils.Utils;

public class DivergentFlux extends Flux{
    private float divergentAngleR;

    public DivergentFlux(final Point3D fluxCoordinate, final Vector3D fluxAxis, int particlesAmount, float divergentAngleDegrees, float minIntensity) {
        super(fluxCoordinate, fluxAxis, particlesAmount, minIntensity);
        this.divergentAngleR = Utils.convertDegreesToRads(divergentAngleDegrees);
        createParticles();
    }

    @Override
    protected void createParticles() {
        for (int i = 0; i < particlesAmount; i++) {
            Vector3D axis = new Vector3D(new Point3D(-(fluxAxis.getY() + fluxAxis.getZ()) / fluxAxis.getX(), 1.0f, 1.0f));
            axis.turnAroundVector(Utils.convertDegreesToRads(Generator.getInstance().uniformInt(360)), fluxAxis);
            Vector3D particleSpeed = fluxAxis.getNewVectorByTurningAroundVector(Generator.getInstance().gauss(0.0f, divergentAngleR).getY(), axis);
            particles.add(new Particle(fluxCoordinate, particleSpeed));
        }
    }

}
