package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars;

import com.private_void.core.detectors.Detector;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.Vector;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.CapillarSystem;
import com.private_void.core.surfaces.smooth_surfaces.SmoothSurface;

import java.util.List;
import java.util.concurrent.*;

import static com.private_void.utils.Constants.PI;
import static com.private_void.utils.Generator.generator;

public abstract class SingleSmoothCapillar extends SmoothSurface implements CapillarSystem {
    protected double length;
    protected double radius;
    protected Detector detector;

    protected SingleSmoothCapillar(final CartesianPoint front, double radius, double roughnessSize, double roughnessAngleR,
                                   double reflectivity, double criticalAngleR) {
        super(front, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        this.radius = radius;
    }

    @Override
    public void interact(Flux flux) {
        long start = System.nanoTime();

        NeutralParticle p;
        CartesianPoint newCoordinate;
        double angleWithSurface;

        for (Particle particle : flux.getParticles()) {
            try {
                p = (NeutralParticle) particle;

                if (willParticleGetInside(p)) {
                    newCoordinate = getHitPoint(p);

                    while (!p.isAbsorbed() && isPointInside(newCoordinate)) {
                        axis = new Vector(1.0, 0.0, 0.0)
                                .turnAroundOY(generator().uniformDouble(0.0, 2.0 * PI));
                        normal = getNormal(newCoordinate)
                                .turnAroundVector(generator().uniformDouble(0.0, roughnessAngleR), axis);
                        axis = getAxis(newCoordinate);

                        angleWithSurface = p.getSpeed().getAngle(normal) - PI / 2.0;
                        p.decreaseIntensity(reflectivity);

                        if (angleWithSurface <= criticalAngleR && p.getIntensity() >= flux.getMinIntensity()) {
                            p.setCoordinate(newCoordinate);
                            p.setSpeed(p.getSpeed().getNewByTurningAroundVector(2.0 * Math.abs(angleWithSurface), axis));
                            newCoordinate = getHitPoint(p);
                        } else {
                            p.setAbsorbed(true);
                            break;
                        }
                    }
                } else {
                    p.setOut(true);
                }
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }

        long finish = System.nanoTime();
        System.out.println("time = " + (finish - start) / 1_000_000 + " ms");
    }

    public void interactParallel(Flux flux) {
        long start = System.nanoTime();

        ExecutorService exec = Executors.newFixedThreadPool(4);

        class Interaction implements Runnable {
            private NeutralParticle particle;
            private CartesianPoint newCoordinate;
            private double angleWithSurface;

            public Interaction(NeutralParticle particle) {
                this.particle = particle;
            }

            @Override
            public void run() {
                if (willParticleGetInside(particle)) {
                    newCoordinate = getHitPoint(particle);

                    while (isPointInside(newCoordinate)) {
                        axis = new Vector(1.0, 0.0, 0.0)
                                .turnAroundOY(generator().uniformDouble(0.0, 2.0 * PI));
                        normal = getNormal(newCoordinate)
                                .turnAroundVector(generator().uniformDouble(0.0, roughnessAngleR), axis);
                        axis = getAxis(newCoordinate);

                        angleWithSurface = particle.getSpeed().getAngle(normal) - PI / 2.0;
                        particle.decreaseIntensity(reflectivity);

                        if (angleWithSurface <= criticalAngleR && particle.getIntensity() >= flux.getMinIntensity()) {
                            particle.setCoordinate(newCoordinate);
                            particle.setSpeed(particle.getSpeed().getNewByTurningAroundVector(2.0 * Math.abs(angleWithSurface), axis));
                            newCoordinate = getHitPoint(particle);
                        } else {
                            particle.setAbsorbed(true);
                            break;
                        }
                    }
                } else {
                    particle.setOut(true);
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

        long finish = System.nanoTime();
        System.out.println("time = " + (finish - start) / 1_000_000 + " ms");
    }

    public void interactStream(Flux flux) {
        long start = System.nanoTime();

        flux.getParticles().forEach(p -> {
            NeutralParticle particle = (NeutralParticle) p;
            if (willParticleGetInside(particle)) {
                CartesianPoint newCoordinate = getHitPoint(particle);

                while (isPointInside(newCoordinate)) {
                    axis = new Vector(1.0, 0.0, 0.0)
                            .turnAroundOY(generator().uniformDouble(0.0, 2.0 * PI));
                    normal = getNormal(newCoordinate)
                            .turnAroundVector(generator().uniformDouble(0.0, roughnessAngleR), axis);
                    axis = getAxis(newCoordinate);

                    double angleWithSurface = particle.getSpeed().getAngle(normal) - PI / 2.0;
                    particle.decreaseIntensity(reflectivity);

                    if (angleWithSurface <= criticalAngleR && particle.getIntensity() >= flux.getMinIntensity()) {
                        particle.setCoordinate(newCoordinate);
                        particle.setSpeed(particle.getSpeed().getNewByTurningAroundVector(2.0 * Math.abs(angleWithSurface), axis));
                        newCoordinate = getHitPoint(particle);
                    } else {
                        particle.setAbsorbed(true);
                        break;
                    }
                }
            } else {
                particle.setOut(true);
            }
        });

        long finish = System.nanoTime();
        System.out.println("time = " + (finish - start) / 1_000_000 + " ms");
    }

    public void interactFork(Flux flux) {
        long start = System.nanoTime();

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

                    if (willParticleGetInside(particle)) {
                        CartesianPoint newCoordinate = getHitPoint(particle);

                        while (!particle.isAbsorbed() && isPointInside(newCoordinate)) {
                            axis = new Vector(1.0, 0.0, 0.0)
                                    .turnAroundOY(generator().uniformDouble(0.0, 2.0 * PI));
                            normal = getNormal(newCoordinate)
                                    .turnAroundVector(generator().uniformDouble(0.0, roughnessAngleR), axis);
                            axis = getAxis(newCoordinate);

                            double angleWithSurface = particle.getSpeed().getAngle(normal) - PI / 2.0;
                            particle.decreaseIntensity(reflectivity);

                            if (angleWithSurface <= criticalAngleR && particle.getIntensity() >= flux.getMinIntensity()) {
                                particle.setCoordinate(newCoordinate);
                                particle.setSpeed(particle.getSpeed().getNewByTurningAroundVector(2.0 * Math.abs(angleWithSurface), axis));
                                newCoordinate = getHitPoint(particle);
                            } else {
                                particle.setAbsorbed(true);
                                break;
                            }
                        }
                    } else {
                        particle.setOut(true);
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

        long finish = System.nanoTime();
        System.out.println("time = " + (finish - start) / 1_000_000 + " ms");
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

    protected abstract boolean isPointInside(CartesianPoint point);

    protected abstract CartesianPoint getDetectorsCoordinate();
}