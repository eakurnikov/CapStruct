package com.private_void.core;

import com.private_void.utils.RandomNumberGenerator;

import java.util.ArrayList;

public class Flux {

    private Point3D fluxCoordinate;
    private Vector3D fluxSpeed;
    private int particlesAmount;
    private int fluxLength;
    private float layerDistance;
    private float divergentAngleR;
    private float divergentAngleD;
    private float minIntensity;
    private ArrayList<Particle> particles;

    public Flux(final Point3D fluxCoordinate, final Vector3D fluxSpeed, int fluxLength, int particlesAmountPerLayer,
                float layerDistance, float minIntensity) {

        this.fluxCoordinate = fluxCoordinate;
        this.fluxSpeed = fluxSpeed;
        this.fluxLength = fluxLength;
        this.particlesAmount = particlesAmountPerLayer;
        this.layerDistance = layerDistance;
        this.minIntensity = minIntensity;

        createParallelFlux(fluxLength, particlesAmount );

    }

    public void Flux(final Point3D fluxCoordinate, final Vector3D fluxAxis, int particlesAmountTotal,
                     float divergentAngleDegrees, float minIntensity) {

        this.fluxCoordinate = fluxCoordinate;
        this.fluxSpeed = fluxAxis;
        this.particlesAmount = particlesAmountTotal;
        this.divergentAngleD = divergentAngleDegrees;
        this.divergentAngleR = Utils.convertDegreesToRads(divergentAngleDegrees);
        this.minIntensity = minIntensity;

        createDivergentFlux(particlesAmount);

    }

    private void createParallelFlux(int fluxLength, int particlesAmountPerLayer) {

        for (int i = 0; i < fluxLength; i++) {

            for (int j = 0; j < particlesAmountPerLayer; j++) {

                Point3D particleCoordinate = RandomNumberGenerator.getInstance().gauss(0.0f, 1.0f);
                particleCoordinate.setX(fluxCoordinate.getX() - i * layerDistance);
                particleCoordinate.setY(particleCoordinate.getY() + fluxCoordinate.getY() - i * fluxSpeed.getY());
                particleCoordinate.setZ(particleCoordinate.getZ() + fluxCoordinate.getZ() - i * fluxSpeed.getZ());

                particles.add(new Particle(particleCoordinate, fluxSpeed));

            }

        }

    }

    private void createDivergentFlux(int particlesAmountTotal) {

        for (int i = 0; i < particlesAmountTotal; i++) {

            Vector3D axis = new Vector3D(new Point3D(-(fluxSpeed.getY() + fluxSpeed.getZ()) / fluxSpeed.getX(), 1.0f, 1.0f));
            axis.turnAroundVector(Utils.convertDegreesToRads(RandomNumberGenerator.getInstance().uniformInt(360)), fluxSpeed);
            Vector3D particleSpeed = fluxSpeed.getNewVectorByTurningAroundVector(RandomNumberGenerator.getInstance().gauss(0.0f, divergentAngleR).getY(), axis);

            particles.add(new Particle(fluxCoordinate, particleSpeed));

        }

    }

    public ArrayList<Particle> getParticles() {
        return particles;
    }

    public float getMinIntensity() {
        return minIntensity;
    }

    public void setMinIntensity(float minIntensity) {
        this.minIntensity = minIntensity;
    }

}
