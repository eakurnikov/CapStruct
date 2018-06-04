package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars;

import com.private_void.core.detection.Detector;
import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.vectors.Vector;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.utils.Utils;
import com.private_void.utils.newtons_method.NewtonsMethod;

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
    protected boolean isPointInside(final CartesianPoint point) {
        return point.getX() <= front.getX() + length;
    }

    @Override
    protected Detector createDetector() {
        return new Detector(
                new CartesianPoint(front.getX() + length, front.getY(), front.getZ()),
                2.0 * radius);
    }

    @Override
    protected NewtonsMethod.Equation getEquation(final NeutralParticle particle) {
        return new SingleConeEquation(particle.getCoordinate(), particle.getSpeed());
    }

    public class SingleConeEquation implements NewtonsMethod.Equation {
        private final CartesianPoint initialPoint;
        private final Vector speed;
        private final double radius;

        private SingleConeEquation(final CartesianPoint initialPoint, final Vector speed) {
            this.initialPoint = initialPoint;
            this.speed = speed;
            this.radius = SingleSmoothCone.this.radius - generator().uniformDouble(0.0, roughnessSize);
        }

        @Override
        public double[] getInitialApproximation(int recursiveIterationCounter) {
            return new double[]{
                    initialPoint.getX() + speed.getX() * radius * recursiveIterationCounter,
                    initialPoint.getY() + speed.getY() * radius * recursiveIterationCounter,
                    initialPoint.getZ() + speed.getZ() * radius * recursiveIterationCounter
            };
        }

        @Override
        public double[] solve(double[] currentApproximation) {
            double x = currentApproximation[0] - front.getX();
            double y = currentApproximation[1] - front.getY();
            double z = currentApproximation[2] - front.getZ();

            double[][] W = new double[3][3];
            double[] F = new double[3];

            //Возможно, с уравнением что-то не так
            W[0][0] = 1.0;
            W[0][1] = 2.0 * y * (1.0 / Math.tan(divergentAngleR)) / Math.sqrt(y * y + z * z);
            W[0][2] = 2.0 * z * (1.0 / Math.tan(divergentAngleR)) / Math.sqrt(y * y + z * z);

            F[0] = x + Math.sqrt(y * y + z * z) * (1.0 / Math.tan(divergentAngleR))
                    - radius * (1.0 / Math.tan(divergentAngleR));

            if (speed.getY() == 0.0) {
                W[1][0] = 0.0;
                W[1][1] = 1.0;
                W[1][2] = 0.0;

                F[1] = currentApproximation[1] - initialPoint.getY();
            } else {
                W[1][0] = 1.0 / speed.getX();
                W[1][1] = -1.0 / speed.getY();
                W[1][2] = 0.0;

                F[1] = (currentApproximation[0] - initialPoint.getX()) / speed.getX()
                        - (currentApproximation[1] - initialPoint.getY()) / speed.getY();
            }

            if (speed.getZ() == 0.0) {
                W[2][0] = 0.0;
                W[2][1] = 0.0;
                W[2][2] = 1.0;

                F[2] = currentApproximation[2] - initialPoint.getZ();
            } else {
                W[2][0] = 1.0 / speed.getX();
                W[2][1] = 0.0;
                W[2][2] = -1.0 / speed.getZ();

                F[2] = (currentApproximation[0] - initialPoint.getX()) / speed.getX()
                        - (currentApproximation[2] - initialPoint.getZ()) / speed.getZ();
            }

            return Utils.matrixMultiplication(Utils.inverseMatrix(W), F);
        }

        @Override
        public boolean isSolutionValid(final CartesianPoint solution) {
            return !solution.isNear(initialPoint) && solution.getX() > initialPoint.getX();
        }
    }
}