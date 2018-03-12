package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.SphericalPoint;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.CapillarFactory;
import com.private_void.utils.Utils;

import static com.private_void.utils.Constants.*;
import static com.private_void.utils.Generator.generator;

public class SmoothCone extends SmoothCapillar {
    private float divergentAngleR;

    public SmoothCone(final Point3D front, float radius, int divergentAngleR, float coneCoefficient, float roughnessSize,
                      float roughnessAngleR, float reflectivity, float criticalAngleR)
            throws IllegalArgumentException {

        super(front, radius, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        if (coneCoefficient >= 1 || coneCoefficient <= 0) {
            throw new IllegalArgumentException();
        }
        this.divergentAngleR = divergentAngleR;
        this.length = Utils.getConeLength(radius, divergentAngleR, coneCoefficient);
    }

    public SmoothCone(final Point3D front, float radius, float length, float coneCoefficient, float roughnessSize,
                      float roughnessAngleR, float reflectivity, float criticalAngleR)
            throws IllegalArgumentException {

        super(front, radius, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        if (coneCoefficient >= 1 || coneCoefficient <= 0) {
            throw new IllegalArgumentException();
        }
        this.length = length;
        this.divergentAngleR = Utils.getConeDivergentAngle(radius, length, coneCoefficient);
    }

    public SmoothCone(final Point3D front, final SphericalPoint position, float radius, int divergentAngleR,
                      float coneCoefficient, float roughnessSize, float roughnessAngleR, float reflectivity,
                      float criticalAngleR)
            throws IllegalArgumentException {

        super(front, radius, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        if (coneCoefficient >= 1 || coneCoefficient <= 0) {
            throw new IllegalArgumentException();
        }
        this.position = position;
        this.divergentAngleR = divergentAngleR;
        this.length = Utils.getConeLength(radius, divergentAngleR, coneCoefficient);
    }

    public SmoothCone(final Point3D front, final SphericalPoint position, float radius, float length,
                      float coneCoefficient, float roughnessSize, float roughnessAngleR, float reflectivity,
                      float criticalAngleR)
            throws IllegalArgumentException {

        super(front, radius, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        if (coneCoefficient >= 1 || coneCoefficient <= 0) {
            throw new IllegalArgumentException();
        }
        this.position = position;
        this.length = length;
        this.divergentAngleR = Utils.getConeDivergentAngle(radius, length, coneCoefficient);
    }

    @Override
    protected Vector3D getNormal(final Point3D point) {
        return new Vector3D(-1.0f,
                (float) (-(point.getY() - front.getY()) * (1.0f / Math.tan(divergentAngleR))
                        / (Math.sqrt((point.getY() - front.getY()) * (point.getY() - front.getY())
                        + (point.getZ() - front.getZ()) * (point.getZ() - front.getZ())))),

                (float) (-(point.getZ() - front.getZ()) * (1.0f / Math.tan(divergentAngleR))
                        / (Math.sqrt((point.getY() - front.getY()) * (point.getY() - front.getY())
                        + (point.getZ() - front.getZ()) * (point.getZ() - front.getZ())))));
    }

    @Override
    protected Vector3D getAxis(final Point3D point) {
        return normal.getNewByTurningAroundOX(PI / 2);
    }

    @Override
    protected Point3D getHitPoint(final NeutralParticle p) {
        if (p.getSpeed().getX() <= 0.0f) {
            p.setAbsorbed(true);
            return p.getCoordinate();
        }

        float[] solution = {
                p.getCoordinate().getX() + radius * p.getRecursiveIterationCount(),
                p.getCoordinate().getY() + radius * Math.signum(p.getSpeed().getY()),
                p.getCoordinate().getZ() + radius * Math.signum(p.getSpeed().getZ())};

        float[] delta = {1.0f, 1.0f, 1.0f};
        float[] F  = new float[3];
        float[][] W = new float[3][3];

        float E = 0.05f;
        int iterationsAmount = 0;

        float dr = generator().uniformFloat(0.0f, roughnessSize);
        float r = radius - dr;
        float x, y, z;

        while (Utils.getMax(delta) > E) {
            try {
                // Костыль для уничтожения частиц, вычисление координат которых зациклилось
                iterationsAmount++;
                if (iterationsAmount > ITERATIONS_MAX) {
                    if (p.isRecursiveIterationsLimitReached()) {
                        p.setAbsorbed(true);
                        return p.getCoordinate();
                    } else {
                        p.recursiveIteration();
                        return getHitPoint(p);
                    }
                }

                x = solution[0] - front.getX();
                y = solution[1] - front.getY();
                z = solution[2] - front.getZ();

                //Возможно, с уравнением что-то не так
                W[0][0] = 1.0f;
                W[0][1] = (float) (2 * y * (1.0f / Math.tan(divergentAngleR)) / Math.sqrt(y * y + z * z));
                W[0][2] = (float) (2 * z * (1.0f / Math.tan(divergentAngleR)) / Math.sqrt(y * y + z * z));

                F[0] = (float) (x + Math.sqrt(y * y + z * z) * (1.0f / Math.tan(divergentAngleR)) - r * (1.0f / Math.tan(divergentAngleR)));

                if (p.getSpeed().getY() == 0.0f) {
                    W[1][0] = 0.0f;
                    W[1][1] = 1.0f;
                    W[1][2] = 0.0f;

                    F[1] = solution[1] - p.getCoordinate().getY();
                } else {
                    W[1][0] = 1.0f / p.getSpeed().getX();
                    W[1][1] = -1.0f / p.getSpeed().getY();
                    W[1][2] = 0.0f;

                    F[1] = (solution[0] - p.getCoordinate().getX()) / p.getSpeed().getX()
                            - (solution[1] - p.getCoordinate().getY()) / p.getSpeed().getY();
                }

                if (p.getSpeed().getZ() == 0.0f) {
                    W[2][0] = 0.0f;
                    W[2][1] = 0.0f;
                    W[2][2] = 1.0f;

                    F[2] = solution[2] - p.getCoordinate().getZ();
                } else {
                    W[2][0] = 1.0f / p.getSpeed().getX();
                    W[2][1] = 0.0f;
                    W[2][2] = -1.0f / p.getSpeed().getZ();

                    F[2] = (solution[0] - p.getCoordinate().getX()) / p.getSpeed().getX()
                            - (solution[2] - p.getCoordinate().getZ()) / p.getSpeed().getZ();
                }

                delta = Utils.matrixMultiplication(Utils.inverseMatrix(W), F);

                for (int i = 0; i < 3; i++) {
                    solution[i] -= delta[i];
                }
            } catch (ArithmeticException e) {
                e.printStackTrace();
                return p.getCoordinate();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        Point3D newCoordinate = new Point3D(solution[0], solution[1], solution[2]);
        if (newCoordinate.isNear(p.getCoordinate()) && !p.isRecursiveIterationsLimitReached()) {
            p.recursiveIteration();
            return getHitPoint(p);
        } else {
            p.stopRecursiveIterations();
            return newCoordinate;
        }
    }

    @Override
    protected boolean isPointInside(final Point3D point) {
        return point.getX() < front.getX() + length;
    }

    public static CapillarFactory getFactory(float radius, float length, float coneCoefficient, float roughnessSize,
                                             float roughnessAngleR, float reflectivity, float criticalAngleR) {
        return new CapillarFactory() {

            @Override
            public Capillar getNewCapillar(final Point3D coordinate, final SphericalPoint position) {
                return new SmoothCone(coordinate, position, radius, length, coneCoefficient, roughnessSize,
                        roughnessAngleR, reflectivity, criticalAngleR);
            }

            @Override
            public float getRadius() {
                return radius;
            }

            @Override
            public float getLength() {
                return length;
            }
        };
    }
}