package com.private_void.core.fluxes;

import com.private_void.core.particles.Particle;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.utils.Utils;

import static com.private_void.utils.Constants.PI;
import static com.private_void.utils.Generator.generator;

public class DivergentFlux extends Flux {
    private float divergentAngleR;

    public DivergentFlux(final Point3D fluxCoordinate, final Vector3D fluxAxis, int particlesAmount, float divergentAngleDegrees, float minIntensity) {
        super(fluxCoordinate, fluxAxis, particlesAmount, minIntensity);
        this.divergentAngleR = Utils.convertDegreesToRadians(divergentAngleDegrees);
        createParticles();
    }

    @Override
    protected void createParticles() {
        for (int i = 0; i < particlesAmount; i++) {
            Vector3D axis = new Vector3D(new Point3D(-(fluxAxis.getY() + fluxAxis.getZ()) / fluxAxis.getX(), 1.0f, 1.0f))
                    .turnAroundVector(generator().uniformFloat(0.0f, 2.0f * PI), fluxAxis);
            particles.add(new Particle(fluxCoordinate, fluxAxis.getNewByTurningAroundVector(generator().gauss(0.0f, divergentAngleR).getY(), axis)));
        }
    }
}