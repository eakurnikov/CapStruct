package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars;

import com.private_void.app.Logger;
import com.private_void.core.detectors.Detector;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.vectors.Vector;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.CapillarSystem;
import com.private_void.core.surfaces.smooth_surfaces.SmoothSurface;

import java.util.List;
import java.util.concurrent.*;

import static com.private_void.utils.Constants.PI;
import static com.private_void.utils.Generator.generator;

public abstract class SingleSmoothCapillar extends SmoothSurface implements CapillarSystem {
    protected final double radius;
    protected final double length;
    protected Detector detector;

    protected SingleSmoothCapillar(final CartesianPoint front, double radius, double length,
                                   double roughnessSize, double roughnessAngleR, double reflectivity, double criticalAngleR) {
        super(front, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        this.radius = radius;
        this.length = length;
    }

    @Override
    public Flux interact(Flux flux) {
        Logger.interactionStart();

        NeutralParticle particle;
        CartesianPoint newCoordinate;
        Vector normal;
        double angleWithSurface;

        for (Particle p : flux.getParticles()) {
            particle = (NeutralParticle) p;

            if (willParticleGetInside(particle)) {
                newCoordinate = getHitPoint(particle);

                while (!particle.isAbsorbed() && isPointInside(newCoordinate)) {
                    normal = getNormal(newCoordinate)
                            .rotateAroundVector(
                                    Vector.E_X.rotateAroundOY(generator().uniformDouble(0.0, 2.0 * PI)),
                                    generator().uniformDouble(0.0, roughnessAngleR));

                    angleWithSurface = particle.getSpeed().getAngle(normal) - PI / 2.0;
                    particle.decreaseIntensity(reflectivity);

                    if (angleWithSurface <= criticalAngleR && particle.getIntensity() >= flux.getMinIntensity()) {
                        particle
                                .setCoordinate(newCoordinate)
                                .rotateSpeed(
                                        getParticleSpeedRotationAxis(newCoordinate, normal),
                                        2.0 * Math.abs(angleWithSurface));
                        newCoordinate = getHitPoint(particle);
                    } else {
                        particle.setAbsorbed(true);
                        break;
                    }
                }
                particle.setInteracted();
            }
        }

        Logger.interactionFinish();

        return detector.detect(flux);
    }

    public Flux interactParallel(Flux flux) {
        Logger.interactionStart();

        ExecutorService exec = Executors.newFixedThreadPool(4);

        class Interaction implements Runnable {
            private NeutralParticle particle;
            private CartesianPoint newCoordinate;
            private Vector normal;
            private double angleWithSurface;

            public Interaction(NeutralParticle particle) {
                this.particle = particle;
            }

            @Override
            public void run() {
                if (willParticleGetInside(particle)) {
                    newCoordinate = getHitPoint(particle);

                    while (isPointInside(newCoordinate)) {
                        normal = getNormal(newCoordinate)
                                .rotateAroundVector(
                                        Vector.E_X.rotateAroundOY(generator().uniformDouble(0.0, 2.0 * PI)),
                                        generator().uniformDouble(0.0, roughnessAngleR));

                        angleWithSurface = particle.getSpeed().getAngle(normal) - PI / 2.0;
                        particle.decreaseIntensity(reflectivity);

                        if (angleWithSurface <= criticalAngleR && particle.getIntensity() >= flux.getMinIntensity()) {
                            particle
                                    .setCoordinate(newCoordinate)
                                    .rotateSpeed(
                                            getParticleSpeedRotationAxis(newCoordinate, normal),
                                            2.0 * Math.abs(angleWithSurface));
                            newCoordinate = getHitPoint(particle);
                        } else {
                            particle.setAbsorbed(true);
                            break;
                        }
                    }
                    particle.setInteracted();
                }
                Thread.yield();
            }
        }

        for (Particle particle : flux.getParticles()) {
            NeutralParticle neutralParticle = (NeutralParticle) particle;
            exec.execute(new Interaction(neutralParticle));
        }
        exec.shutdown();

        try {
            exec.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.interactionFinish();

        return detector.detect(flux);
    }

    public Flux interactStream(Flux flux) {
        Logger.interactionStart();

        flux.getParticles().forEach(p -> {
            NeutralParticle particle = (NeutralParticle) p;
            Vector normal;

            if (willParticleGetInside(particle)) {
                CartesianPoint newCoordinate = getHitPoint(particle);

                while (isPointInside(newCoordinate)) {
                    normal = getNormal(newCoordinate)
                            .rotateAroundVector(
                                    Vector.E_X.rotateAroundOY(generator().uniformDouble(0.0, 2.0 * PI)),
                                    generator().uniformDouble(0.0, roughnessAngleR));

                    double angleWithSurface = particle.getSpeed().getAngle(normal) - PI / 2.0;
                    particle.decreaseIntensity(reflectivity);

                    if (angleWithSurface <= criticalAngleR && particle.getIntensity() >= flux.getMinIntensity()) {
                        particle
                                .setCoordinate(newCoordinate)
                                .rotateSpeed(
                                        getParticleSpeedRotationAxis(newCoordinate, normal),
                                        2.0 * Math.abs(angleWithSurface));
                        newCoordinate = getHitPoint(particle);
                    } else {
                        particle.setAbsorbed(true);
                        break;
                    }
                }
                particle.setInteracted();
            }
        });

        Logger.interactionFinish();

        return detector.detect(flux);
    }

    public Flux interactFork(Flux flux) {
        Logger.interactionStart();

        class Interaction extends RecursiveAction {
            private List<? extends Particle> particles;
            private int startIndex;
            private int length;

            public Interaction(List<? extends Particle> particles, int startIndex, int length) {
                this.particles = particles;
                this.startIndex = startIndex;
                this.length = length;
            }

            private void interact() {
                for (int i = startIndex; i < startIndex + length; i++) {
                    NeutralParticle particle = (NeutralParticle) particles.get(i);
                    Vector normal;

                    if (willParticleGetInside(particle)) {
                        CartesianPoint newCoordinate = getHitPoint(particle);

                        while (!particle.isAbsorbed() && isPointInside(newCoordinate)) {
                            normal = getNormal(newCoordinate)
                                    .rotateAroundVector(
                                            Vector.E_X.rotateAroundOY(generator().uniformDouble(0.0, 2.0 * PI)),
                                            generator().uniformDouble(0.0, roughnessAngleR));

                            double angleWithSurface = particle.getSpeed().getAngle(normal) - PI / 2.0;
                            particle.decreaseIntensity(reflectivity);

                            if (angleWithSurface <= criticalAngleR && particle.getIntensity() >= flux.getMinIntensity()) {
                                particle
                                        .setCoordinate(newCoordinate)
                                        .rotateSpeed(
                                                getParticleSpeedRotationAxis(newCoordinate, normal),
                                                2.0 * Math.abs(angleWithSurface));
                                newCoordinate = getHitPoint(particle);
                            } else {
                                particle.setAbsorbed(true);
                                break;
                            }
                        }
                        particle.setInteracted();
                    }
                }
            }

            @Override
            protected void compute() {
                if (length < particles.size() / 200.0) {
                    interact();
                } else {
                    int newLength = length / 2;
                    invokeAll(
                            new Interaction(particles, startIndex, newLength),
                            new Interaction(particles,startIndex + newLength, length - newLength));
                }
            }
        }

        List<? extends Particle> particles = flux.getParticles();
        Interaction interaction = new Interaction(particles, 0, particles.size());
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(interaction);

        Logger.interactionFinish();

        return detector.detect(flux);
    }

    @Override
    public Detector getDetector() {
        return detector;
    }

    protected boolean willParticleGetInside(final Particle p) {
        double x0 = front.getX();

        double x = p.getCoordinate().getX();
        double y = p.getCoordinate().getY();
        double z = p.getCoordinate().getZ();

        double Vx = p.getSpeed().getX();
        double Vy = p.getSpeed().getY();
        double Vz = p.getSpeed().getZ();

        double newY = (Vy / Vx) * (x0 - x) + y;
        double newZ = (Vz / Vx) * (x0 - x) + z;

        return (newY - front.getY()) * (newY - front.getY()) + (newZ - front.getZ()) * (newZ - front.getZ())
                < radius * radius;
    }

    protected abstract boolean isPointInside(final CartesianPoint point);

    protected abstract CartesianPoint getDetectorsCoordinate();
}