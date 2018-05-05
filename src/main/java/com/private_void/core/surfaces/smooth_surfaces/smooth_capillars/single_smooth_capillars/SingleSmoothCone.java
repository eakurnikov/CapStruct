package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars;

import com.private_void.app.Logger;
import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.vectors.Vector;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.utils.Utils;

import static com.private_void.utils.Constants.ITERATIONS_MAX;
import static com.private_void.utils.Constants.PI;
import static com.private_void.utils.Generator.generator;

public class SingleSmoothCone extends SingleSmoothCapillar {
    private final double divergentAngleR;

    public SingleSmoothCone(final CartesianPoint front, double radius, int divergentAngleR, double coneCoefficient,
                            double roughnessSize, double roughnessAngleR, double reflectivity, double criticalAngleR)
            throws IllegalArgumentException {

        super(front, radius, Utils.getConeLength(radius, divergentAngleR, coneCoefficient),
                roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        if (coneCoefficient >= 1.0 || coneCoefficient <= 0.0) {
            throw new IllegalArgumentException();
        }
        this.divergentAngleR = divergentAngleR;
        this.detector = createDetector();
    }

    public SingleSmoothCone(final CartesianPoint front, double radius, double length, double coneCoefficient,
                            double roughnessSize, double roughnessAngleR, double reflectivity, double criticalAngleR)
            throws IllegalArgumentException {

        super(front, radius, length, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        if (coneCoefficient >= 1.0 || coneCoefficient <= 0.0) {
            throw new IllegalArgumentException();
        }
        this.divergentAngleR = Utils.getConeDivergentAngle(radius, length, coneCoefficient);
        this.detector = createDetector();
    }

    @Override
    protected Vector getNormal(final CartesianPoint point) {
        return Vector.set(
                -1.0,
                -(point.getY() - front.getY()) * (1.0 / Math.tan(divergentAngleR))
                        / (Math.sqrt((point.getY() - front.getY()) * (point.getY() - front.getY())
                        + (point.getZ() - front.getZ()) * (point.getZ() - front.getZ()))),

                -(point.getZ() - front.getZ()) * (1.0 / Math.tan(divergentAngleR))
                        / (Math.sqrt((point.getY() - front.getY()) * (point.getY() - front.getY())
                        + (point.getZ() - front.getZ()) * (point.getZ() - front.getZ()))));
    }

    @Override
    protected Vector getParticleSpeedRotationAxis(final CartesianPoint point, final Vector normal) {
        return normal.rotateAroundOX(PI / 2.0);
    }

    @Override
    protected CartesianPoint getHitPoint(final NeutralParticle p) {
        if (p.getSpeed().getX() <= 0.0) {
            Logger.particleDeleted();
            p.delete();
            return p.getCoordinate();
        }

        double[] solution = {p.getCoordinate().getX() + p.getSpeed().getX() * radius * p.getRecursiveIterationCount(),
                             p.getCoordinate().getY() + p.getSpeed().getY() * radius * p.getRecursiveIterationCount(),
                             p.getCoordinate().getZ() + p.getSpeed().getZ() * radius * p.getRecursiveIterationCount()};

        double[] delta = {1.0, 1.0, 1.0};
        double[] F  = new double[3];
        double[][] W = new double[3][3];

        double E = 0.05;
        int iterationsAmount = 0;

        double dr = generator().uniformDouble(0.0, roughnessSize);
        double r = radius - dr;
        double x, y, z;

        while (Utils.getMax(delta) > E) {
            try {
                // Костыль для уничтожения частиц, вычисление координат которых зациклилось
                iterationsAmount++;
                if (iterationsAmount > ITERATIONS_MAX) {
                    if (p.isRecursiveIterationsLimitReached()) {
                        Logger.particleDeleted();
                        p.delete();
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
                W[0][0] = 1.0;
                W[0][1] = 2.0 * y * (1.0 / Math.tan(divergentAngleR)) / Math.sqrt(y * y + z * z);
                W[0][2] = 2.0 * z * (1.0 / Math.tan(divergentAngleR)) / Math.sqrt(y * y + z * z);

                F[0] = x + Math.sqrt(y * y + z * z) * (1.0 / Math.tan(divergentAngleR)) - r * (1.0 / Math.tan(divergentAngleR));

                if (p.getSpeed().getY() == 0.0) {
                    W[1][0] = 0.0;
                    W[1][1] = 1.0;
                    W[1][2] = 0.0;

                    F[1] = solution[1] - p.getCoordinate().getY();
                } else {
                    W[1][0] = 1.0 / p.getSpeed().getX();
                    W[1][1] = -1.0 / p.getSpeed().getY();
                    W[1][2] = 0.0;

                    F[1] = (solution[0] - p.getCoordinate().getX()) / p.getSpeed().getX()
                            - (solution[1] - p.getCoordinate().getY()) / p.getSpeed().getY();
                }

                if (p.getSpeed().getZ() == 0.0) {
                    W[2][0] = 0.0;
                    W[2][1] = 0.0;
                    W[2][2] = 1.0;

                    F[2] = solution[2] - p.getCoordinate().getZ();
                } else {
                    W[2][0] = 1.0 / p.getSpeed().getX();
                    W[2][1] = 0.0;
                    W[2][2] = -1.0 / p.getSpeed().getZ();

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

        CartesianPoint newCoordinate = new CartesianPoint(solution);
        if ((newCoordinate.isNear(p.getCoordinate()) || newCoordinate.getX() <= p.getCoordinate().getX())
                && !p.isRecursiveIterationsLimitReached()) {
            p.recursiveIteration();
            return getHitPoint(p);
        } else {
            if (p.isRecursiveIterationsLimitReached()) {
                Logger.particleDeleted();
                p.delete();
            }
            p.stopRecursiveIterations();
            return newCoordinate;
        }
    }

    @Override
    protected boolean isPointInside(final CartesianPoint point) {
        return point.getX() <= front.getX() + length;
    }

    @Override
    protected Detector createDetector() {
        return new Detector(
                new CartesianPoint(front.getX() + length, front.getY(), front.getZ()),
                2.0 * radius);
    }
}