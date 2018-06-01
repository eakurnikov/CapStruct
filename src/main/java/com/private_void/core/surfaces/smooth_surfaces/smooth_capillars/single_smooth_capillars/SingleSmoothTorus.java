package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars;

import com.private_void.core.detection.Detector;
import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.vectors.Vector;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.utils.Utils;
import com.private_void.utils.newtons_method.NewtonsMethod;

import static com.private_void.utils.Constants.PI;
import static com.private_void.utils.Generator.generator;

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
    protected CartesianPoint getHitPoint(final NeutralParticle particle) {
        if (particle.getSpeed().getX() <= 0.0) {
            return null;
        }

        return new NewtonsMethod(getEquation(particle)).getSolution();
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

        public SingleTorusEquation(final CartesianPoint initialPoint, final Vector speed) {
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

//        if (p.getSpeed().getX() <= 0.0) {
//            Logger.warning(MessagePool.particleDeleted());
//            p.delete();
//            return p.getCoordinate();
//        }
//
//        double[] solution = {p.getCoordinate().getX() + p.getSpeed().getX() * radius * p.getRecursiveIterationCount(),
//                             p.getCoordinate().getY() + p.getSpeed().getY() * radius * p.getRecursiveIterationCount(),
//                             p.getCoordinate().getZ() + p.getSpeed().getZ() * radius * p.getRecursiveIterationCount()};
//
//        double[] delta = {1.0, 1.0, 1.0};
//        double[] F  = new double[3];
//        double[][] W = new double[3][3];
//
//        double E = 0.05;
//        int iterationsAmount = 0;
//
//        double dr = generator().uniformDouble(0.0, roughnessSize);
//        double r = radius - dr;
//        double x, y ,z;

//        while (Utils.getMax(delta) > E) {
//            try {
//                // Костыль для уничтожения частиц, вычисление координат которых зациклилось
//                iterationsAmount++;
//                if (iterationsAmount > ITERATIONS_MAX) {
//                    if (p.isRecursiveIterationsLimitReached()) {
//                        Logger.warning(MessagePool.particleDeleted());
//                        p.delete();
//                        return p.getCoordinate();
//                    } else {
//                        p.recursiveIteration();
//                        return getHitPoint(p);
//                    }
//                }
//
//                x = solution[0] - front.getX();
//                y = solution[1] - front.getY();
//                z = solution[2] - front.getZ() - curvRadius;
//
//                W[0][0] = 2.0 * (x * x + y * y + z * z + curvRadius * curvRadius - r * r) * 2.0 * x
//                        - 8.0 * curvRadius * curvRadius * x;
//
//                W[0][1] = 2.0 * (x * x + y * y + z * z + curvRadius * curvRadius - r * r) * 2.0 * y;
//
//                W[0][2] = 2.0 * (x * x + y * y + z * z + curvRadius * curvRadius - r * r) * 2.0 * z
//                        - 8.0 * curvRadius * curvRadius * z;
//
//                F[0] = (x * x + y * y + z * z + curvRadius * curvRadius - r * r) *
//                       (x * x + y * y + z * z + curvRadius * curvRadius - r * r)
//                        - 4.0 * curvRadius * curvRadius * (x * x + z * z);
//
//                if (p.getSpeed().getY() == 0.0) {
//                    W[1][0] = 0.0;
//                    W[1][1] = 1.0;
//                    W[1][2] = 0.0;
//
//                    F[1] = solution[1] - p.getCoordinate().getY();
//                } else {
//                    W[1][0] = 1.0 / p.getSpeed().getX();
//                    W[1][1] = -1.0 / p.getSpeed().getY();
//                    W[1][2] = 0.0;
//
//                    F[1] = (solution[0] - p.getCoordinate().getX()) / p.getSpeed().getX()
//                            - (solution[1] - p.getCoordinate().getY()) / p.getSpeed().getY();
//                }
//
//                if (p.getSpeed().getZ() == 0.0) {
//                    W[2][0] = 0.0;
//                    W[2][1] = 0.0;
//                    W[2][2] = 1.0;
//
//                    F[2] = solution[2] - p.getCoordinate().getZ();
//                } else {
//                    W[2][0] = 0.0;
//                    W[2][1] = 1.0 / p.getSpeed().getY();
//                    W[2][2] = -1.0 / p.getSpeed().getZ();
//
//                    F[2] = (solution[1] - p.getCoordinate().getY()) / p.getSpeed().getY()
//                            - (solution[2] - p.getCoordinate().getZ()) / p.getSpeed().getZ();
//                }
//
//                delta = Utils.matrixMultiplication(Utils.inverseMatrix(W), F);
//
//                for (int i = 0; i < 3; i++) {
//                    solution[i] -= delta[i];
//                }
//            } catch (ArithmeticException e) {
//                System.out.println(e.getMessage());
//            } catch (Exception e){
//                System.out.println(e.getMessage());
//            }
//        }
//
//        CartesianPoint newCoordinate = new CartesianPoint(solution);
//        if ((newCoordinate.isNear(p.getCoordinate()) || newCoordinate.getX() <= p.getCoordinate().getX())
//                && !p.isRecursiveIterationsLimitReached()) {
//            p.recursiveIteration();
//            return getHitPoint(p);
//        } else {
//            if (p.isRecursiveIterationsLimitReached()) {
//                Logger.warning(MessagePool.particleDeleted());
//                p.delete();
//            }
//            p.stopRecursiveIterations();
//            return newCoordinate;
//        }