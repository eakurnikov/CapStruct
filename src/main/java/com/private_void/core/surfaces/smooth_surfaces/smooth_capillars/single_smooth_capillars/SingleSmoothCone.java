package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars;

import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.utils.Utils;

import static com.private_void.utils.Constants.ITERATIONS_MAX;
import static com.private_void.utils.Constants.PI;
import static com.private_void.utils.Generator.generator;

public class SingleSmoothCone extends SingleSmoothCapillar {
    private float divergentAngleR;

    public SingleSmoothCone(final Point3D frontCoordinate, float radius, int divergentAngleR, float coneCoefficient,
                      float roughnessSize, float roughnessAngleR, float reflectivity, float criticalAngleR)
            throws IllegalArgumentException {

        super(frontCoordinate, radius, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        if (coneCoefficient >= 1 || coneCoefficient <= 0) {
            throw new IllegalArgumentException();
        }
        this.divergentAngleR = divergentAngleR;
        this.length = Utils.getConeLength(radius, divergentAngleR, coneCoefficient);
        this.detector = new Detector(getDetectorsCoordinate(), 2 * radius);
    }

    public SingleSmoothCone(final Point3D frontCoordinate, float radius, float length, float coneCoefficient,
                            float roughnessSize, float roughnessAngleR, float reflectivity, float criticalAngleR)
            throws IllegalArgumentException {

        super(frontCoordinate, radius, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        if (coneCoefficient >= 1 || coneCoefficient <= 0) {
            throw new IllegalArgumentException();
        }
        this.length = length;
        this.divergentAngleR = Utils.getConeDivergentAngle(radius, length, coneCoefficient);
        this.detector = new Detector(getDetectorsCoordinate(), 2 * radius);
    }

    @Override
    protected Vector3D getNormal(final Point3D point) {
        return new Vector3D(
                -1.0f,
                (float) (-point.getY() * (1.0f / Math.tan(divergentAngleR))
                        / (Math.sqrt(point.getY() * point.getY() + point.getZ() * point.getZ()))),
                (float) (-point.getZ() * (1.0f / Math.tan(divergentAngleR))
                        / (Math.sqrt(point.getY() * point.getY() + point.getZ() * point.getZ()))));
    }

    @Override
    protected Vector3D getAxis(final Point3D point) {
        return normal.getNewByTurningAroundOX(PI / 2);
    }

    @Override
    protected Point3D getHitPoint(final NeutralParticle p) {
        float[] solution = {
                p.getCoordinate().getX() + radius * p.getRecursiveIterationCount(),
                p.getCoordinate().getY() + radius * (p.getSpeed().getY() / Math.abs(p.getSpeed().getY())),
                p.getCoordinate().getZ() + radius * (p.getSpeed().getZ() / Math.abs(p.getSpeed().getZ()))
        };
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
                    if (p.isRecursiveIterationsLimitReached()) {
                        p.setAbsorbed(true);
                        return p.getCoordinate();
                    } else {
                        p.recursiveIteration();
                        return getHitPoint(p);
                    }
                }

                W[0][0] = 1.0f;

                W[0][1] = (float) (2 * (solution[1] - front.getY()) * (1.0f / Math.tan(divergentAngleR))
                        / Math.sqrt((solution[1] - front.getY()) * (solution[1] - front.getY())
                                  + (solution[2] - front.getZ()) * (solution[2] - front.getZ())));

                W[0][2] = (float) (2 * (solution[2] - front.getZ()) * (1.0f / Math.tan(divergentAngleR))
                        / Math.sqrt((solution[1] - front.getY()) * (solution[1] - front.getY())
                                  + (solution[2] - front.getZ()) * (solution[2] - front.getZ())));

                W[1][0] = 1.0f / p.getSpeed().getX();
                W[1][1] = -1.0f / p.getSpeed().getY();
                W[1][2] = 0.0f;

                W[2][0] = 1.0f / p.getSpeed().getX();
                W[2][1] = 0.0f;
                W[2][2] = -1.0f / p.getSpeed().getZ();

                //Некорректно работает, возможно, с уравнением что-то не так
                F[0] = (float) ((solution[0] - front.getX())
                            + Math.sqrt((solution[1] - front.getY()) * (solution[1] - front.getY())
                            + (solution[2] - front.getZ()) * (solution[2] - front.getZ()))
                        * (1.0f / Math.tan(divergentAngleR)) - (radius - dr) * (1.0f / Math.tan(divergentAngleR)));

                F[1] = (solution[0] - p.getCoordinate().getX()) / p.getSpeed().getX()
                        - (solution[1] - p.getCoordinate().getY()) / p.getSpeed().getY();

                F[2] = (solution[0] - p.getCoordinate().getX()) / p.getSpeed().getX()
                        - (solution[2] - p.getCoordinate().getZ()) / p.getSpeed().getZ();

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

    @Override
    protected Point3D getDetectorsCoordinate() {
        return new Point3D(front.getX() + length, front.getY(), front.getZ());
    }
}