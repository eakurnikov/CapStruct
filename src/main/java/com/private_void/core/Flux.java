package com.private_void.core;

import com.private_void.utils.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.Random;

//TODO: почитать про ключевое слово this, может быть его следует по возможности избегать, или лучше наоборот писать везде
public class Flux {

    private Point3D fluxCoordinate;
    private Vector3D fluxSpeed;
    private int particlesAmount;
    private int fluxLength;
    private float layerDistance;
    private float divergentAngleRadians;
    private float divergentAngleDegrees;
    private float minIntensity;
    private ArrayList<Particle> particles;
    private Random rand;

    public Flux(final Point3D fluxCoordinate, final Vector3D fluxSpeed, int fluxLength, int particlesAmountPerLayer, float layerDistance, float minIntensity) {

        rand = new Random();

        this.fluxCoordinate = fluxCoordinate;
        this.fluxSpeed = fluxSpeed;
        this.fluxLength = fluxLength;
        this.particlesAmount = particlesAmountPerLayer;
        this.layerDistance = layerDistance;
        this.minIntensity = minIntensity;

        createParallelFlux(this.fluxLength, this.particlesAmount );

    }

    public void Flux(final Point3D fluxCoordinate, final Vector3D fluxAxis, int particlesAmountTotal, float divergentAngleDegrees, float minIntensity) {

        rand = new Random();

        this.fluxCoordinate = fluxCoordinate;
        this.fluxSpeed = fluxAxis;
        this.particlesAmount = particlesAmountTotal;
        this.divergentAngleDegrees = divergentAngleDegrees;
        this.divergentAngleRadians = Utils.convertDegreesToRads(divergentAngleDegrees);
        this.minIntensity = minIntensity;

        createDivergentFlux(this.particlesAmount);

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
            axis.turnAroundVector(Utils.convertDegreesToRads(rand.nextInt(360)), fluxSpeed);
            Vector3D particleSpeed = fluxSpeed.getNewVectorByTurningAroundVector(RandomNumberGenerator.getInstance().gauss(0.0f,  divergentAngleRadians).getY(), axis);

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
