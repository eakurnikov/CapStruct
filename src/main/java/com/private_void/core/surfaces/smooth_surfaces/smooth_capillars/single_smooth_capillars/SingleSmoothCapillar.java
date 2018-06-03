package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars;

import com.private_void.core.detection.Detector;
import com.private_void.core.detection.Distribution;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.vectors.Vector;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.core.surfaces.CapillarSystem;
import com.private_void.core.surfaces.smooth_surfaces.SmoothSurface;
import com.private_void.utils.Interaction;
import com.private_void.utils.exceptions.BadParticleException;
import com.private_void.utils.newtons_method.NewtonsMethod;
import com.private_void.utils.notifiers.Logger;
import com.private_void.utils.notifiers.MessagePool;

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

//    public Distribution interactSingle(Flux flux) {
//        Logger.info(MessagePool.interactionStart());
//
//        NeutralParticle particle;
//        CartesianPoint newCoordinate;
//        Vector normal;
//        double angleWithSurface;
//
//        for (Particle p : flux.getParticles()) {
//            particle = (NeutralParticle) p;
//
//            if (willParticleGetInside(particle)) {
//                newCoordinate = getHitPoint(particle);
//
//                while (!particle.isAbsorbed() && isPointInside(newCoordinate)) {
//                    normal = getNormal(newCoordinate)
//                            .rotateAroundVector(
//                                    Vector.E_X.rotateAroundOY(generator().uniformDouble(0.0, 2.0 * PI)),
//                                    generator().uniformDouble(0.0, roughnessAngleR));
//
//                    angleWithSurface = particle.getSpeed().getAngle(normal) - PI / 2.0;
//                    particle.decreaseIntensity(reflectivity);
//
//                    if (angleWithSurface <= criticalAngleR && particle.getIntensity() >= flux.getMinIntensity()) {
//                        particle
//                                .setCoordinate(newCoordinate)
//                                .rotateSpeed(
//                                        getParticleSpeedRotationAxis(newCoordinate, normal),
//                                        2.0 * Math.abs(angleWithSurface));
//
//                        newCoordinate = getHitPoint(particle);
//
//                        if (newCoordinate == null) {
//                            Logger.warning(MessagePool.particleDeleted());
//                            particle.delete();
//                            break;
//                        }
//                    } else {
//                        particle.absorb();
//                        break;
//                    }
//                }
//
//                particle.setChanneled();
//            }
//        }
//
//        Logger.info(MessagePool.interactionFinish());
//
//        return detector.detect(flux);
//    }
//
//    public Distribution interactParallel(Flux flux) {
//        Logger.info(MessagePool.interactionStart());
//
//        ExecutorService exec = Executors.newFixedThreadPool(4);
//
//        class Interaction implements Runnable {
//            private NeutralParticle particle;
//            private CartesianPoint newCoordinate;
//            private Vector normal;
//            private double angleWithSurface;
//
//            public Interaction(NeutralParticle particle) {
//                this.particle = particle;
//            }
//
//            @Override
//            public void run() {
//                if (willParticleGetInside(particle)) {
//                    newCoordinate = getHitPoint(particle);
//
//                    while (isPointInside(newCoordinate)) {
//                        normal = getNormal(newCoordinate)
//                                .rotateAroundVector(
//                                        Vector.E_X.rotateAroundOY(generator().uniformDouble(0.0, 2.0 * PI)),
//                                        generator().uniformDouble(0.0, roughnessAngleR));
//
//                        angleWithSurface = particle.getSpeed().getAngle(normal) - PI / 2.0;
//                        particle.decreaseIntensity(reflectivity);
//
//                        if (angleWithSurface <= criticalAngleR && particle.getIntensity() >= flux.getMinIntensity()) {
//                            particle
//                                    .setCoordinate(newCoordinate)
//                                    .rotateSpeed(
//                                            getParticleSpeedRotationAxis(newCoordinate, normal),
//                                            2.0 * Math.abs(angleWithSurface));
//                            newCoordinate = getHitPoint(particle);
//                        } else {
//                            particle.absorb();
//                            break;
//                        }
//                    }
//                    particle.setChanneled();
//                }
//                Thread.yield();
//            }
//        }
//
//        for (Particle particle : flux.getParticles()) {
//            NeutralParticle neutralParticle = (NeutralParticle) particle;
//            exec.execute(new Interaction(neutralParticle));
//        }
//        exec.shutdown();
//
//        try {
//            exec.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Logger.info(MessagePool.interactionFinish());
//
//        return detector.detect(flux);
//    }
//
//    public Distribution interactStream(Flux flux) {
//        Logger.info(MessagePool.interactionStart());
//
//        flux.getParticles().forEach(p -> {
//            NeutralParticle particle = (NeutralParticle) p;
//            Vector normal;
//
//            if (willParticleGetInside(particle)) {
//                CartesianPoint newCoordinate = getHitPoint(particle);
//
//                while (isPointInside(newCoordinate)) {
//                    normal = getNormal(newCoordinate)
//                            .rotateAroundVector(
//                                    Vector.E_X.rotateAroundOY(generator().uniformDouble(0.0, 2.0 * PI)),
//                                    generator().uniformDouble(0.0, roughnessAngleR));
//
//                    double angleWithSurface = particle.getSpeed().getAngle(normal) - PI / 2.0;
//                    particle.decreaseIntensity(reflectivity);
//
//                    if (angleWithSurface <= criticalAngleR && particle.getIntensity() >= flux.getMinIntensity()) {
//                        particle
//                                .setCoordinate(newCoordinate)
//                                .rotateSpeed(
//                                        getParticleSpeedRotationAxis(newCoordinate, normal),
//                                        2.0 * Math.abs(angleWithSurface));
//                        newCoordinate = getHitPoint(particle);
//                    } else {
//                        particle.absorb();
//                        break;
//                    }
//                }
//                particle.setChanneled();
//            }
//        });
//
//        Logger.info(MessagePool.interactionFinish());
//
//        return detector.detect(flux);
//    }

    @Override
    public Distribution interact(Flux flux) {
        Logger.info(MessagePool.interactionStart());

        new Interaction(
                flux.getParticles(),
                0,
                flux.getParticles().size(),
                (particles, startIndex, length) -> {
                    for (int i = startIndex; i < startIndex + length; i++) {
                        NeutralParticle particle = (NeutralParticle) particles.get(i);
                        Vector normal;

                        try {
                            if (willParticleGetInside(particle)) {
                                CartesianPoint newCoordinate = getHitPoint(particle);

                                while (!particle.isAbsorbed() && isPointInside(newCoordinate)) {
                                    normal = getNormal(newCoordinate)
                                            .rotateAroundVector(
                                                    Vector.E_X.rotateAroundOY(generator().uniformDouble(0.0, 2.0 * PI)),
                                                    generator().uniformDouble(0.0, roughnessAngleR));

                                    double angleWithSurface = particle.getSpeed().getAngle(normal) - PI / 2.0;
                                    particle.decreaseIntensity(reflectivity);

                                    if (angleWithSurface <= criticalAngleR &&
                                            particle.getIntensity() >= flux.getMinIntensity()) {
                                        particle
                                                .setCoordinate(newCoordinate)
                                                .rotateSpeed(
                                                        getParticleSpeedRotationAxis(newCoordinate, normal),
                                                        2.0 * Math.abs(angleWithSurface));
                                        newCoordinate = getHitPoint(particle);
                                    } else {
                                        particle.absorb();
                                        break;
                                    }
                                }
                                particle.setChanneled();
                            }
                        } catch (BadParticleException e) {
                            Logger.warning(MessagePool.particleDeleted());
                            particle.delete();
                        }
                    }
                }).start();

        Logger.info(MessagePool.interactionFinish());

        return detector.detect(flux);
    }

    @Override
    protected CartesianPoint getHitPoint(final NeutralParticle particle) throws BadParticleException {
        if (particle.getSpeed().getX() <= 0.0) {
            throw new BadParticleException();
        }

        return new NewtonsMethod(getEquation(particle)).getSolution();
    }

    protected boolean willParticleGetInside(final NeutralParticle p) {
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

    protected abstract Detector createDetector();

    protected abstract NewtonsMethod.Equation getEquation(final NeutralParticle particle);
}