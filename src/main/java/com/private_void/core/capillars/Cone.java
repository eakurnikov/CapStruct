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

public class Cone extends Surface {
    private float length;
    private float divergentAngleR;

    public Cone(final Point3D frontCoordinate, float radius, int divergentAngleD, float coneCoefficient, float roughnessSize,
                float roughnessAngleD, float reflectivity, float slideAngleD) throws IllegalArgumentException {

        super(frontCoordinate, radius, roughnessSize, roughnessAngleD, reflectivity, slideAngleD);
        if (coneCoefficient >= 1 || coneCoefficient <= 0) {
            throw new IllegalArgumentException();
        }
        this.divergentAngleR = Utils.convertDegreesToRads(divergentAngleD);
        this.length = (float) (radius * (1 / Math.tan(divergentAngleR)) * coneCoefficient);
        this.detector = new Detector(new Point3D(frontCoordinate.getX() + length, frontCoordinate.getY(), frontCoordinate.getZ()),
                                    2 * radius, CELL_SIZE);
    }

    public Cone(final Point3D frontCoordinate, float radius, float length, float coneCoefficient, float roughnessSize,
                float roughnessAngleD, float reflectivity, float slideAngleD) throws IllegalArgumentException {

        super(frontCoordinate, radius, roughnessSize, roughnessAngleD, reflectivity, slideAngleD);
        if (coneCoefficient >= 1 || coneCoefficient <= 0) {
            throw new IllegalArgumentException();
        }
        this.length = length;
        this.divergentAngleR = (float) Math.atan((radius / length) * coneCoefficient);
        this.detector = new Detector(new Point3D(frontCoordinate.getX() + length, frontCoordinate.getY(), frontCoordinate.getZ()),
                2 * radius, CELL_SIZE);
    }

    @Override
    public Point3D getHitPoint(Particle particle) {
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

                W[0][0] = 1.0f;
                W[0][1] = (float) (solution[1] * (1.0f / Math.tan(divergentAngleR)) / (Math.sqrt(solution[1] * solution[1] + solution[2] * solution[2])));
                W[0][2] = (float) (solution[2] * (1.0f / Math.tan(divergentAngleR)) / (Math.sqrt(solution[1] * solution[1] + solution[2] * solution[2])));

                W[1][0] = 1.0f / particle.getSpeed().getX();
                W[1][1] = -1.0f / particle.getSpeed().getY();
                W[1][2] = 0.0f;

                W[2][0] = 1.0f / particle.getSpeed().getX();
                W[2][1] = 0.0f;
                W[2][2] = -1.0f / particle.getSpeed().getZ();

                F[0] = (float) (solution[1] + Math.sqrt(solution[1] * solution[1] + solution[2] * solution[2]) * (1.0f / Math.tan(divergentAngleR)) - (radius - dr) * (1.0f / Math.tan(divergentAngleR)));
                F[1] = (solution[0] - particle.getCoordinate().getX()) / particle.getSpeed().getX() - (solution[1] - particle.getCoordinate().getY()) / particle.getSpeed().getY();
                F[2] = (solution[0] - particle.getCoordinate().getX()) / particle.getSpeed().getX() - (solution[2] - particle.getCoordinate().getZ()) / particle.getSpeed().getZ();

                delta = Utils.matrixMultiplication(Utils.inverseMatrix(W), F);

                for (int i = 0; i < 3; i++) {
                    solution[i] -= delta[i];
                }
            } catch (ArithmeticException e) {
                System.out.println(e.getMessage());
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

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
                // Костыль для уничтожения частиц, у которых произошло слишком много отражений внутри каплляра. В принципе он не нужен
                // так как, если будет много отражений, интенсивность просто убьется. Но нужно протестировать
                int reboundsCount = 0;
                newCoordinate = getHitPoint(particle);

                while (isPointInside(newCoordinate)) {
                    axis = new Vector3D(1.0f, 0.0f, 0.0f)
                            .turnAroundOY(generator().uniformFloat(0.0f, 2.0f * PI));

                    normal = getNormal(newCoordinate)
                            .turnAroundVector(generator().uniformFloat(0.0f, roughnessAngleR), axis);

                    axis = normal.getNewByTurningAroundOX(PI / 2);

                    angleVN = particle.getSpeed().getAngle(normal.inverse());

                    if (angleVN >= antiSlideAngleR && reboundsCount  < REBOUNDS_COUNT_MAX) {
                        reboundsCount++;
                        particle.setCoordinate(newCoordinate);
                        particle.setSpeed(particle.getSpeed().getNewByTurningAroundVector(2 * Math.abs(PI / 2 - angleVN), axis));
                        particle.decreaseIntensity(reflectivity);
                        newCoordinate = getHitPoint(particle);
                    } else {
                        particle.setAbsorbed(true);
                        break;
                    }
                }
            }
            else {
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
        return new Vector3D(-1.0f,
                (float) (-point.getY() * (1.0f / Math.tan(divergentAngleR)) / (Math.sqrt(point.getY() * point.getY() + point.getZ() * point.getZ()))),
                (float) (-point.getZ() * (1.0f / Math.tan(divergentAngleR)) / (Math.sqrt(point.getY() * point.getY() + point.getZ() * point.getZ()))));
    }
}