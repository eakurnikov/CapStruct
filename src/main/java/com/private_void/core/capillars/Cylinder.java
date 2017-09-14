package com.private_void.core.capillars;

import com.private_void.core.detectors.Detector;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.Particle;
import com.private_void.utils.Utils;

import java.util.Iterator;

import static com.private_void.utils.Constants.*;
import static com.private_void.utils.Generator.generator;

public class Cylinder extends Surface {
    private float length;

    public Cylinder(final Point3D frontCoordinate, float radius, float length, float roughnessSize, float roughnessAngleD,
                    float reflectivity, float slideAngleD) {

        super(frontCoordinate, radius, roughnessSize, roughnessAngleD, reflectivity, slideAngleD);
        this.length = length;
        this.detector = new Detector(new Point3D(frontCoordinate.getX() + length, frontCoordinate.getY(), frontCoordinate.getZ()),
                                    2 * radius, CELL_SIZE);
    }

    @Override
    public Point3D getHitPoint(final Particle particle) {
        float[] solution = {particle.getCoordinate().getX() + radius * particle.getRecursiveIterationCount(),
                            particle.getCoordinate().getY() + (particle.getSpeed().getY() / Math.abs(particle.getSpeed().getY())) * radius,
                            particle.getCoordinate().getZ() + (particle.getSpeed().getZ() / Math.abs(particle.getSpeed().getZ())) * radius};
        float[] delta = {1.0f, 1.0f, 1.0f};
        float[] F  = new float[3];
        float[][] W = new float[3][3];

        float E = 0.05f;
        float dr = generator().uniformFloat(0.0f, roughnessSize);

        int iterationsAmount = 0;

        while (Utils.getMax(delta) > E) {
            try {
                // Костыль для уничтожения частиц, вычисление координат которых зациклилось
                iterationsAmount++;
                if (iterationsAmount > ITERATIONS_MAX) {
                    if (particle.isRecursiveIterationsLimitReached()) {
                        particle.setAbsorbed(true);
                        return particle.getCoordinate();
                    } else {
                        particle.recursiveIteration();
                        return getHitPoint(particle);
                    }
                }

                W[0][0] = 0.0f;
                W[0][1] = 2.0f * solution[1];
                W[0][2] = 2.0f * solution[2];

                W[1][0] = 1.0f / particle.getSpeed().getX();
                W[1][1] = -1.0f / particle.getSpeed().getY();
                W[1][2] = 0.0f;

                W[2][0] = 1.0f / particle.getSpeed().getX();
                W[2][1] = 0.0f;
                W[2][2] = -1.0f / particle.getSpeed().getZ();

                F[0] = solution[1] * solution[1] + solution[2] * solution[2] - (radius - dr) * (radius - dr);
                F[1] = (solution[0] - particle.getCoordinate().getX()) / particle.getSpeed().getX() - (solution[1] - particle.getCoordinate().getY()) / particle.getSpeed().getY();
                F[2] = -(solution[2] - particle.getCoordinate().getZ()) / particle.getSpeed().getZ() + (solution[0] - particle.getCoordinate().getX()) / particle.getSpeed().getX();

                delta = Utils.matrixMultiplication(Utils.inverseMatrix(W), F);

                for (int i = 0; i < 3; i++) {
                    solution[i] -= delta[i];
                }
            } catch (ArithmeticException e) {
                System.out.println(e.getMessage());
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

//        Point3D newCoordinate = new Point3D(solution[0], solution[1], solution[2]);
//        if (newCoordinate.isNear(particle.getCoordinate())) {
//            if (particle.isRecursiveIterationsLimitReached()) {
//                particle.setAbsorbed(true);
//                return newCoordinate;
//            } else {
//                particle.recursiveIteration();
//                return getHitPoint(particle);
//            }
//        } else {
//            particle.stopRecursiveIterations();
//            return newCoordinate;
//        }

        Point3D newCoordinate = new Point3D(solution[0], solution[1], solution[2]);
        if (newCoordinate.isNear(particle.getCoordinate()) && !particle.isRecursiveIterationsLimitReached()) {
            particle.recursiveIteration();
            return getHitPoint(particle);
        } else {
            particle.stopRecursiveIterations();
            return newCoordinate;
        }
    }

    @Override
    public void passThrough(Flux flux) {
        Particle particle;
        Point3D newCoordinate;
        float angleVN;

        Iterator<Particle> iterator = flux.getParticles().iterator();
        while (iterator.hasNext()) {
            particle = iterator.next();

            if (willParticleGetInside(particle)) {
                newCoordinate = getHitPoint(particle);

                while (isPointInside(newCoordinate)) {
                    axis = new Vector3D(1.0f, 0.0f, 0.0f)
                            .turnAroundOY(generator().uniformFloat(0.0f, 2.0f * PI));

                    normal = getNormal(newCoordinate)
                            .turnAroundVector(generator().uniformFloat(0.0f, roughnessAngleR), axis);

                    axis = normal.getNewByTurningAroundOX(PI / 2);

                    angleVN = particle.getSpeed().getAngle(normal.inverse());

                    if (angleVN >= antiSlideAngleR) {
                        particle.setCoordinate(newCoordinate);
                        particle.setSpeed(particle.getSpeed().getNewByTurningAroundVector(2 * Math.abs(PI / 2 - angleVN), axis));
                        particle.decreaseIntensity(reflectivity);
                        newCoordinate = getHitPoint(particle);
                    } else {
                        particle.setAbsorbed(true);
                        break;
                    }
                }
            } else {
                detector.increaseOutOfCapillarParticlesAmount();
                detector.increaseOutOfCapillarInensity(particle.getIntensity());
                iterator.remove();
            }
        }
        detector.detect(flux);
    }

    @Override
    protected boolean isPointInside(Point3D point) {
        return point.getX() <= length;
    }

    @Override
    protected Vector3D getNormal(Point3D point) {
        return new Vector3D(0.0f, -2 * point.getY(), -2 * point.getZ());
    }
}