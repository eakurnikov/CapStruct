package com.private_void.core.capillars;

import com.private_void.core.detectors.RotatedDetector;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.Particle;
import com.private_void.utils.Utils;

import static com.private_void.utils.Constants.*;
import static com.private_void.utils.Generator.generator;

public class Torus extends Surface {
    private float torusRadius;
    private float curvAngleR;

    public Torus(final Point3D frontCoordinate, float radius, float torusRadius, float curvAngleD, float roughnessSize,
                 float roughnessAngleD, float reflectivity, float slideAngleD) {
        super(frontCoordinate, radius, roughnessSize, roughnessAngleD, reflectivity, slideAngleD);
        this.torusRadius = torusRadius;
        this.curvAngleR = Utils.convertDegreesToRads(curvAngleD);
        this.detector = new RotatedDetector(new Point3D((float) (frontCoordinate.getX() + torusRadius * Math.sin(curvAngleD)),
                                                        frontCoordinate.getY(),
                                                        (float) (frontCoordinate.getZ() - torusRadius * (1 - Math.cos(curvAngleR)))),
                                            2 * radius,
                                            curvAngleD);
    }

    @Override
    public Point3D getHitPoint(final Particle particle) {
        float[] solution = {particle.getCoordinate().getX() + particle.getSpeed().getX() * radius * particle.getRecursiveIterationCount(),
                            particle.getCoordinate().getY() + particle.getSpeed().getY() * radius * particle.getRecursiveIterationCount(),
                            particle.getCoordinate().getZ() + particle.getSpeed().getZ() * radius * particle.getRecursiveIterationCount()};
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

                W[0][0] = 2 * (solution[0] * solution[0] + solution[1] * solution[1] + (solution[2] + torusRadius) * (solution[2] + torusRadius) + torusRadius * torusRadius - (radius - dr) * (radius - dr)) * 2 * solution[0] - 8 * torusRadius * torusRadius * solution[0];
                W[0][1] = 2 * (solution[0] * solution[0] + solution[1] * solution[1] + (solution[2] + torusRadius) * (solution[2] + torusRadius) + torusRadius * torusRadius - (radius - dr) * (radius - dr)) * 2 * solution[1];
                W[0][2] = 2 * (solution[0] * solution[0] + solution[1] * solution[1] + (solution[2] + torusRadius) * (solution[2] + torusRadius) + torusRadius * torusRadius - (radius - dr) * (radius - dr)) * 2 * solution[0] - 8 * torusRadius * torusRadius * (solution[2] + torusRadius);

                W[1][0] =  1.0f / particle.getSpeed().getX();
                W[1][1] = -1.0f / particle.getSpeed().getY();
                W[1][2] =  0.0f;

                W[2][0] =  0.0f;
                W[2][1] =  1.0f / particle.getSpeed().getY();
                W[2][2] = -1.0f / particle.getSpeed().getZ();

                F[0] = (solution[0] * solution[0] + solution[1] * solution[1] + (solution[2] + torusRadius) * (solution[2] + torusRadius) + torusRadius * torusRadius - (radius - dr) * (torusRadius - dr)) * (solution[0] * solution[0] + solution[1] * solution[1] + (solution[2] + torusRadius) * (solution[2] + torusRadius) + torusRadius * torusRadius - (radius - dr) * (radius - dr)) - 4 * torusRadius * torusRadius * (solution[0] * solution[0] + (solution[2] + torusRadius) * (solution[2] + torusRadius));
                F[1] = (solution[0] - particle.getCoordinate().getX()) / particle.getSpeed().getX() - (solution[1] - particle.getCoordinate().getY()) / particle.getSpeed().getY();
                F[2] = (solution[1] - particle.getCoordinate().getY()) / particle.getSpeed().getY() - (solution[2] - particle.getCoordinate().getZ()) / particle.getSpeed().getZ();

                delta = Utils.matrixMultiplication(Utils.inverseMatrix(W), F);

                for (int i = 0; i < 3; i++) {
                    solution[i] -= delta[i]; //возможно, нужно разыменовывать дельту
                }
            } catch (ArithmeticException e) {
                System.out.println(e.getMessage());
            } catch (Exception e){
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
    protected boolean isPointInside(Point3D point) {
        float x = point.getX();
        float y = point.getY();
        float z = point.getZ();

        return Math.asin(x / Math.sqrt(x * x + y * y + (z + torusRadius) * (z + torusRadius))) <= curvAngleR;
    }

    @Override
    protected Vector3D getNormal(Point3D point) {
        float x = point.getX();
        float y = point.getY();
        float z = point.getZ();

        return new Vector3D((-2 * (x * x + y * y + (z + torusRadius) * (z + torusRadius) + torusRadius * torusRadius - radius * radius) * 2 * x + 8 * torusRadius * torusRadius * x),
                (-2 * (x * x + y * y + (z + torusRadius) * (z + torusRadius) + torusRadius * torusRadius - radius * radius) * 2 * y),
                (-2 * (x * x + y * y + (z + torusRadius) * (z + torusRadius) + torusRadius * torusRadius - radius * radius) * 2 * (z + torusRadius) + 8 * torusRadius * torusRadius * (z + torusRadius)));
    }
}