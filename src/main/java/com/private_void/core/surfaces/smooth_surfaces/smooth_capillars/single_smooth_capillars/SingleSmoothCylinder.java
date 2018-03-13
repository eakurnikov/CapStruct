package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars;

import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.Vector;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.utils.Utils;

import static com.private_void.utils.Constants.ITERATIONS_MAX;
import static com.private_void.utils.Constants.PI;
import static com.private_void.utils.Generator.generator;

public class SingleSmoothCylinder extends SingleSmoothCapillar {

    public SingleSmoothCylinder(final CartesianPoint front, float radius, float length, float roughnessSize,
                                float roughnessAngleR, float reflectivity, float criticalAngleR) {
        super(front, radius, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        this.length = length;
        this.detector = new Detector(getDetectorsCoordinate(), 2 * radius);
    }

    @Override
    protected Vector getNormal(final CartesianPoint point) {
        return new Vector(0.0f, -2 * (point.getY() - front.getY()), -2 * (point.getZ() - front.getZ()));
    }

    @Override
    protected Vector getAxis(final CartesianPoint point) {
        return normal.getNewByTurningAroundOX(PI / 2);
    }

    @Override
    protected CartesianPoint getHitPoint(final NeutralParticle p) {
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
        float y, z;

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

                y = solution[1] - front.getY();
                z = solution[2] - front.getZ();

                W[0][0] = 0.0f;
                W[0][1] = 2.0f * y;
                W[0][2] = 2.0f * z;

                F[0] = y * y + z * z - r * r;

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

                    F[2] = -(solution[2] - p.getCoordinate().getZ()) / p.getSpeed().getZ()
                            + (solution[0] - p.getCoordinate().getX()) / p.getSpeed().getX();
                }

                delta = Utils.matrixMultiplication(Utils.inverseMatrix(W), F);

                for (int i = 0; i < 3; i++) {
                    solution[i] -= delta[i];
                }
            } catch (ArithmeticException e) {
                e.printStackTrace();
                return p.getCoordinate();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

//        CartesianPoint newCoordinate = new CartesianPoint(solution[0], solution[1], solution[2]);
//        if (newCoordinate.isNear(p.getCoordinate())) {
//            if (p.isRecursiveIterationsLimitReached()) {
//                p.setAbsorbed(true);
//                return newCoordinate;
//            } else {
//                p.recursiveIteration();
//                return getHitPoint(p);
//            }
//        } else {
//            p.stopRecursiveIterations();
//            return newCoordinate;
//        }

        CartesianPoint newCoordinate = new CartesianPoint(solution[0], solution[1], solution[2]);
        if (newCoordinate.isNear(p.getCoordinate()) && !p.isRecursiveIterationsLimitReached()) {
            p.recursiveIteration();
            return getHitPoint(p);
        } else {
            p.stopRecursiveIterations();
            return newCoordinate;
        }
    }

    @Override
    protected boolean isPointInside(final CartesianPoint point) {
        return point.getX() < front.getX() + length;
    }

    @Override
    protected CartesianPoint getDetectorsCoordinate() {
        return new CartesianPoint(front.getX() + length, front.getY(), front.getZ());
    }
}