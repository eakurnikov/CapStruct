package com.private_void.core.entities.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars;

import com.private_void.core.entities.detectors.Detector;
import com.private_void.core.entities.particles.NeutralParticle;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;
import com.private_void.core.math.newtons_method.NewtonsMethod;
import com.private_void.core.math.utils.Utils;

import static com.private_void.core.constants.Constants.PI;
import static com.private_void.core.math.generators.Generator.generator;

public class SingleSmoothTorus extends SingleSmoothCapillar {
    private final double curvRadius;
    private final double curvAngleR;

    public SingleSmoothTorus(final CartesianPoint front, double radius, double curvRadius, double curvAngleR, double roughnessSize,
                             double roughnessAngleR, double reflectivity, double criticalAngleR) {
        super(front, radius, Utils.getTorusLength(curvRadius, curvAngleR),
                roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        this.curvRadius = curvRadius;
        this.curvAngleR = curvAngleR;
        this.detector = createDetector();
    }

    @Override
    protected Vector getNormal(final CartesianPoint point) {
        double x = point.getX() - front.getX();
        double y = point.getY() - front.getY();
        double z = point.getZ() - front.getZ() - curvRadius; // + curvRadius сместит влево

        return Vector.set(
                (-2.0 * (x * x + y * y + z * z + curvRadius * curvRadius - radius * radius) * 2.0 * x
                        + 8.0 * curvRadius * curvRadius * x),

                (-2.0 * (x * x + y * y + z * z + curvRadius * curvRadius - radius * radius) * 2.0 * y),

                (-2.0 * (x * x + y * y + z * z + curvRadius * curvRadius - radius * radius) * 2.0 * z
                        + 8.0 * curvRadius * curvRadius * z));
    }

    @Override
    protected Vector getParticleSpeedRotationAxis(final CartesianPoint point, final Vector normal) {
        return normal
                .rotateAroundOX(PI / 2.0)
                .rotateAroundOY(getPointsAngle(point));
    }

    @Override
    protected boolean isPointInside(final CartesianPoint point) {
//        double angle = getPointsAngle(point);
//        return angle >= 0 && angle <= curvAngleR;

        return point.getX() <= front.getX() + length;
    }

    @Override
    protected Detector createDetector() {
        return new Detector(
                new CartesianPoint(
                        front.getX() + curvRadius * Math.sin(curvAngleR),
                        front.getY(),
                        front.getZ() + curvRadius * (1 - Math.cos(curvAngleR))),
                2.0 * radius);
    }

    @Override
    protected NewtonsMethod.Equation getEquation(final NeutralParticle particle) {
        return new SingleTorusEquation(particle.getCoordinate(), particle.getSpeed());
    }

    private double getPointsAngle(final CartesianPoint point) {
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ() - curvRadius;

        return Math.asin(x / Math.sqrt(x * x + y * y + z * z));
    }

    public class SingleTorusEquation implements NewtonsMethod.Equation {
        private final CartesianPoint initialPoint;
        private final Vector speed;
        private final double radius;

        SingleTorusEquation(final CartesianPoint initialPoint, final Vector speed) {
            this.initialPoint = initialPoint;
            this.speed = speed;
            this.radius = SingleSmoothTorus.this.radius - generator().uniformDouble(0.0, roughnessSize);
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
            double z = currentApproximation[2] - front.getZ() - curvRadius;

            double[][] W = new double[3][3];
            double[] F = new double[3];

            W[0][0] = 2.0 * (x * x + y * y + z * z + curvRadius * curvRadius - radius * radius) * 2.0 * x
                    - 8.0 * curvRadius * curvRadius * x;

            W[0][1] = 2.0 * (x * x + y * y + z * z + curvRadius * curvRadius - radius * radius) * 2.0 * y;

            W[0][2] = 2.0 * (x * x + y * y + z * z + curvRadius * curvRadius - radius * radius) * 2.0 * z
                    - 8.0 * curvRadius * curvRadius * z;

            F[0] = (x * x + y * y + z * z + curvRadius * curvRadius - radius * radius) *
                    (x * x + y * y + z * z + curvRadius * curvRadius - radius * radius)
                    - 4.0 * curvRadius * curvRadius * (x * x + z * z);

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
                W[2][0] = 0.0;
                W[2][1] = 1.0 / speed.getY();
                W[2][2] = -1.0 / speed.getZ();

                F[2] = (currentApproximation[1] - initialPoint.getY()) / speed.getY()
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