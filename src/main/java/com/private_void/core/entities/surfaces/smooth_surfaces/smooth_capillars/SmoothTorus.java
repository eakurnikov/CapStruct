package com.private_void.core.entities.surfaces.smooth_surfaces.smooth_capillars;

import com.private_void.core.entities.particles.NeutralParticle;
import com.private_void.core.entities.surfaces.Capillar;
import com.private_void.core.entities.surfaces.capillar_factories.RotatedTorusFactory;
import com.private_void.core.entities.surfaces.capillar_factories.StraightCapillarFactory;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.reference_frames.ReferenceFrame;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;
import com.private_void.core.math.newtons_method.NewtonsMethod;
import com.private_void.core.math.utils.Utils;

import static com.private_void.core.constants.Constants.PI;
import static com.private_void.core.math.generators.Generator.generator;

public class SmoothTorus extends SmoothCapillar {
    private final double curvRadius;
    private final double curvAngleR;

    private SmoothTorus(final CartesianPoint front, double radius, double curvRadius, double curvAngleR,
                        double roughnessSize, double roughnessAngleR, double reflectivity, double criticalAngleR) {
        super(front, ReferenceFrame.builder().atPoint(front).build(), radius, Utils.getTorusLength(curvRadius, curvAngleR),
                roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);

        this.curvRadius = curvRadius;
        this.curvAngleR = curvAngleR;
    }

    public SmoothTorus(double length, final CartesianPoint front, double radius, double curvAngleR,
                       double roughnessSize, double roughnessAngleR, double reflectivity, double criticalAngleR) {
        super(front, ReferenceFrame.builder().atPoint(front).build(), radius, length,
                roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);

        this.curvRadius = Utils.getTorusCurvRadius(length, curvAngleR);
        this.curvAngleR = curvAngleR;
    }

    public SmoothTorus(double length, final CartesianPoint front, final ReferenceFrame refFrame, double radius,
                       double curvAngleR, double roughnessSize, double roughnessAngleR, double reflectivity,
                       double criticalAngleR) {
        super(front, refFrame, radius, length, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        this.curvRadius = Utils.getTorusCurvRadius(length, curvAngleR);
        this.curvAngleR = curvAngleR;
    }

    @Override
    protected Vector getNormal(final CartesianPoint point) {
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ() + curvRadius;

        return Vector.set(
                (-2.0 * (x * x + y * y + z * z + curvRadius * curvRadius - radius * radius) * 2.0 * x
                        + 8 * curvRadius * curvRadius * x),

                (-2.0 * (x * x + y * y + z * z + curvRadius * curvRadius - radius * radius) * 2.0 * y),

                (-2.0 * (x * x + y * y + z * z + curvRadius * curvRadius - radius * radius) * 2.0 * z
                        + 8 * curvRadius * curvRadius * z));
    }

    @Override
    protected Vector getParticleSpeedRotationAxis(final CartesianPoint point, final Vector normal) {
        return normal
                .rotateAroundOX(PI / 2.0)
                .rotateAroundOY(getPointsAngle(point));
    }

    @Override
    protected boolean isPointInside(final CartesianPoint point) {
        return point.getX() <= length;
    }

    @Override
    protected NewtonsMethod.Equation getEquation(final NeutralParticle particle) {
        return new TorusEquation(particle.getCoordinate(), particle.getSpeed());
    }

    private double getPointsAngle(final CartesianPoint point) {
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ() + curvRadius;

        return Math.asin(x / Math.sqrt(x * x + y * y + z * z));
    }

    public class TorusEquation implements NewtonsMethod.Equation {
        private final CartesianPoint initialPoint;
        private final Vector speed;
        private final double radius;

        public TorusEquation(final CartesianPoint initialPoint, final Vector speed) {
            this.initialPoint = initialPoint;
            this.speed = speed;
            this.radius = SmoothTorus.this.radius - generator().uniformDouble(0.0, roughnessSize);
        }

        @Override
        public double[] getInitialApproximation(int recursiveIterationCounter) {
            return new double[]{
                    initialPoint.getX() + radius * recursiveIterationCounter,
                    initialPoint.getY() + radius * Math.signum(speed.getY()),
                    initialPoint.getZ() + radius * Math.signum(speed.getZ())
            };

//            return new double[]{
//                    initialPoint.getX() + speed.getX() * radius * recursiveIterationCounter,
//                    initialPoint.getY() + speed.getY() * radius * recursiveIterationCounter,
//                    initialPoint.getZ() + speed.getZ() * radius * recursiveIterationCounter
//            };
        }

        @Override
        public double[] solve(double[] currentApproximation) {
            double x = currentApproximation[0];
            double y = currentApproximation[1];
            double z = currentApproximation[2] + curvRadius;

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

    public static StraightCapillarFactory getCapillarFactory(double radius, double curvRadius, double curvAngleR,
                                                             double roughnessSize, double roughnessAngleR,
                                                             double reflectivity, double criticalAngleR) {
        return new StraightCapillarFactory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate) {
                return new SmoothTorus(coordinate, radius, curvRadius, curvAngleR, roughnessSize, roughnessAngleR,
                        reflectivity, criticalAngleR);
            }

            @Override
            public double getRadius() {
                return radius;
            }

            @Override
            public double getLength() {
                return Utils.getTorusLength(curvRadius, curvAngleR);
            }
        };
    }

    public static StraightCapillarFactory getCapillarFactoryWithLength(double radius, double length, double curvAngleR,
                                                                       double roughnessSize, double roughnessAngleR,
                                                                       double reflectivity, double criticalAngleR) {
        return new StraightCapillarFactory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate) {
                return new SmoothTorus(length, coordinate, radius, curvAngleR, roughnessSize, roughnessAngleR,
                        reflectivity, criticalAngleR);
            }

            @Override
            public double getRadius() {
                return radius;
            }

            @Override
            public double getLength() {
                return length;
            }
        };
    }

    public static RotatedTorusFactory getRotatedCapillarFactory(double radius, double length, double roughnessSize,
                                                                double roughnessAngleR, double reflectivity,
                                                                double criticalAngleR) {
        return new RotatedTorusFactory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate, double curvAngleR,
                                           final ReferenceFrame refFrame) {
                return new SmoothTorus(length, coordinate, refFrame, radius, curvAngleR, roughnessSize, roughnessAngleR,
                        reflectivity, criticalAngleR);
            }

            @Override
            public double getRadius() {
                return radius;
            }

            @Override
            public double getLength() {
                return length;
            }
        };
    }
}