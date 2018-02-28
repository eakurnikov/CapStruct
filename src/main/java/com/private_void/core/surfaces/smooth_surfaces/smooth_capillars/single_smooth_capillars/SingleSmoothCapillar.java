package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars;

import com.private_void.core.detectors.Detector;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.CapillarSystem;
import com.private_void.core.surfaces.smooth_surfaces.SmoothSurface;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

import static com.private_void.utils.Constants.PI;
import static com.private_void.utils.Generator.generator;

public abstract class SingleSmoothCapillar extends SmoothSurface implements CapillarSystem {
    protected float length;
    protected float radius;
    protected Detector detector;

    protected SingleSmoothCapillar(final Point3D frontCoordinate, float radius, float roughnessSize, float roughnessAngleR,
                             float reflectivity, float criticalAngleR) {
        super(frontCoordinate, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        this.radius = radius;
    }

    @Override
    public void interact(Flux flux) {
        long start = System.nanoTime();

        NeutralParticle p;
        Point3D newCoordinate;
        float angleWithSurface;
        Iterator<? extends Particle> iterator = flux.getParticles().iterator();

        while (iterator.hasNext()) {
            try {
                p = (NeutralParticle) iterator.next();

                if (willParticleGetInside(p)) {
                    newCoordinate = getHitPoint(p);

                    while (isPointInside(newCoordinate)) {
                        axis = new Vector3D(1.0f, 0.0f, 0.0f)
                                .turnAroundOY(generator().uniformFloat(0.0f, 2.0f * PI));
                        normal = getNormal(newCoordinate)
                                .turnAroundVector(generator().uniformFloat(0.0f, roughnessAngleR), axis);
                        axis = getAxis(newCoordinate);

                        angleWithSurface = p.getSpeed().getAngle(normal) - PI / 2;
                        p.decreaseIntensity(reflectivity);

                        if (angleWithSurface <= criticalAngleR && p.getIntensity() >= flux.getMinIntensity()) {
                            p.setCoordinate(newCoordinate);
                            p.setSpeed(p.getSpeed().getNewByTurningAroundVector(2 * Math.abs(angleWithSurface), axis));
                            newCoordinate = getHitPoint(p);
                        } else {
                            p.setAbsorbed(true);
                            detector.increaseAbsorbedParticlesAmount();
                            detector.increaseAbsorbedIntensity(p.getIntensity());
                            //iterator.remove();
                            break;
                        }
                    }
                } else {
                    detector.increaseOutOfCapillarParticlesAmount();
                    detector.increaseOutOfCapillarIntensity(p.getIntensity());
                    iterator.remove();
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
            private Point3D newCoordinate;
            private float angleWithSurface;


            public Interaction(NeutralParticle particle) {
                this.particle = particle;
            }

            @Override
            public void run() {
                //count.set(count.get() + 1);
                if (willParticleGetInside(particle)) {
                    newCoordinate = getHitPoint(particle);

                    while (isPointInside(newCoordinate)) {
                        axis = new Vector3D(1.0f, 0.0f, 0.0f)
                                .turnAroundOY(generator().uniformFloat(0.0f, 2.0f * PI));
                        normal = getNormal(newCoordinate)
                                .turnAroundVector(generator().uniformFloat(0.0f, roughnessAngleR), axis);
                        axis = getAxis(newCoordinate);

                        angleWithSurface = particle.getSpeed().getAngle(normal) - PI / 2;
                        particle.decreaseIntensity(reflectivity);

                        if (angleWithSurface <= criticalAngleR && particle.getIntensity() >= flux.getMinIntensity()) {
                            particle.setCoordinate(newCoordinate);
                            particle.setSpeed(particle.getSpeed().getNewByTurningAroundVector(2 * Math.abs(angleWithSurface), axis));
                            newCoordinate = getHitPoint(particle);
                        } else {
                            particle.setAbsorbed(true);
                            detector.increaseAbsorbedParticlesAmount();
                            detector.increaseAbsorbedIntensity(particle.getIntensity());
                            //iterator.remove();
                            break;
                        }
                    }
                } else {
                    detector.increaseOutOfCapillarParticlesAmount();
                    detector.increaseOutOfCapillarIntensity(particle.getIntensity());
                    //iterator.remove();
                    particle.setAbsorbed(true);
                }

                Thread.yield();
                //return particle;
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
                Point3D newCoordinate = getHitPoint(particle);

                while (isPointInside(newCoordinate)) {
                    axis = new Vector3D(1.0f, 0.0f, 0.0f)
                            .turnAroundOY(generator().uniformFloat(0.0f, 2.0f * PI));
                    normal = getNormal(newCoordinate)
                            .turnAroundVector(generator().uniformFloat(0.0f, roughnessAngleR), axis);
                    axis = getAxis(newCoordinate);

                    float angleWithSurface = particle.getSpeed().getAngle(normal) - PI / 2;
                    particle.decreaseIntensity(reflectivity);

                    if (angleWithSurface <= criticalAngleR && particle.getIntensity() >= flux.getMinIntensity()) {
                        particle.setCoordinate(newCoordinate);
                        particle.setSpeed(particle.getSpeed().getNewByTurningAroundVector(2 * Math.abs(angleWithSurface), axis));
                        newCoordinate = getHitPoint(particle);
                    } else {
                        particle.setAbsorbed(true);
                        detector.increaseAbsorbedParticlesAmount();
                        detector.increaseAbsorbedIntensity(particle.getIntensity());
                        //iterator.remove();
                        break;
                    }
                }
            } else {
                detector.increaseOutOfCapillarParticlesAmount();
                detector.increaseOutOfCapillarIntensity(particle.getIntensity());
                //iterator.remove();
                particle.setAbsorbed(true);
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
                        Point3D newCoordinate = getHitPoint(particle);

                        while (isPointInside(newCoordinate)) {
                            axis = new Vector3D(1.0f, 0.0f, 0.0f)
                                    .turnAroundOY(generator().uniformFloat(0.0f, 2.0f * PI));
                            normal = getNormal(newCoordinate)
                                    .turnAroundVector(generator().uniformFloat(0.0f, roughnessAngleR), axis);
                            axis = getAxis(newCoordinate);

                            float angleWithSurface = particle.getSpeed().getAngle(normal) - PI / 2;
                            particle.decreaseIntensity(reflectivity);

                            if (angleWithSurface <= criticalAngleR && particle.getIntensity() >= flux.getMinIntensity()) {
                                particle.setCoordinate(newCoordinate);
                                particle.setSpeed(particle.getSpeed().getNewByTurningAroundVector(2 * Math.abs(angleWithSurface), axis));
                                newCoordinate = getHitPoint(particle);
                            } else {
                                particle.setAbsorbed(true);
                                detector.increaseAbsorbedParticlesAmount();
                                detector.increaseAbsorbedIntensity(particle.getIntensity());
                                //iterator.remove();
                                break;
                            }
                        }
                    } else {
                        detector.increaseOutOfCapillarParticlesAmount();
                        detector.increaseOutOfCapillarIntensity(particle.getIntensity());
                        //iterator.remove();
                        particle.setAbsorbed(true);
                    }
                }
            }

            @Override
            protected void compute() {
                if (length < particles.size() / 200) {
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
        float x0 = front.getX();

        float x = p.getCoordinate().getX();
        float y = p.getCoordinate().getY();
        float z = p.getCoordinate().getZ();

        float Vx = p.getSpeed().getX();
        float Vy = p.getSpeed().getY();
        float Vz = p.getSpeed().getZ();

        float newY = (Vy / Vx) * (x0 - x) + y;
        float newZ = (Vz / Vx) * (x0 - x) + z;

        return (newY - front.getY()) * (newY - front.getY()) + (newZ - front.getZ()) * (newZ - front.getZ())
                < radius * radius;
    }

    protected abstract boolean isPointInside(Point3D point);

    protected abstract Point3D getDetectorsCoordinate();
}