package com.private_void.core.entities.surfaces.smooth_surfaces.smooth_capillars;

import com.private_void.core.entities.particles.NeutralParticle;
import com.private_void.core.entities.surfaces.Capillar;
import com.private_void.core.entities.surfaces.capillar_factories.RotatedCylinderFactory;
import com.private_void.core.entities.surfaces.capillar_factories.StraightCapillarFactory;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.reference_frames.ReferenceFrame;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;
import com.private_void.core.math.newtons_method.NewtonsMethod;
import com.private_void.core.math.utils.Utils;

import static com.private_void.core.constants.Constants.PI;
import static com.private_void.core.math.generators.Generator.generator;

public class SmoothCylinder extends SmoothCapillar {

    private SmoothCylinder(final CartesianPoint front, final ReferenceFrame refFrame, double radius, double length,
                           double roughnessSize, double roughnessAngleR, double reflectivity, double criticalAngleR) {
        super(front, refFrame, radius, length, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
    }

    @Override
    protected Vector getNormal(final CartesianPoint point) {
        return Vector.set(0.0, -2.0 * point.getY(), -2.0 * point.getZ());
    }

    @Override
    protected Vector getParticleSpeedRotationAxis(final CartesianPoint point, final Vector normal) {
        return normal.rotateAroundOX(PI / 2.0);
    }

    @Override
    protected boolean isPointInside(final CartesianPoint point) {
        return point.getX() <= length;
    }

    @Override
    protected NewtonsMethod.Equation getEquation(final NeutralParticle particle) {
        return new CylinderEquation(particle.getCoordinate(), particle.getSpeed());
    }

    public class CylinderEquation implements NewtonsMethod.Equation {
        private final CartesianPoint initialPoint;
        private final Vector speed;
        private final double radius;

        CylinderEquation(final CartesianPoint initialPoint, final Vector speed) {
            this.initialPoint = initialPoint;
            this.speed = speed;
            this.radius = SmoothCylinder.this.radius - generator().uniformDouble(0.0, roughnessSize);
        }

        @Override
        public double[] getInitialApproximation(int recursiveIterationCounter) {
            return new double[]{
                    initialPoint.getX() + radius * recursiveIterationCounter,
                    initialPoint.getY() + radius * Math.signum(speed.getY()),
                    initialPoint.getZ() + radius * Math.signum(speed.getZ())
            };
        }

        @Override
        public double[] solve(double[] currentApproximation) {
            double y = currentApproximation[1];
            double z = currentApproximation[2];

            double[][] W = new double[3][3];
            double[] F = new double[3];

            W[0][0] = 0.0;
            W[0][1] = 2.0 * y;
            W[0][2] = 2.0 * z;

            F[0] = y * y + z * z - radius * radius;

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

                F[2] = -(currentApproximation[2] - initialPoint.getZ()) / speed.getZ()
                        + (currentApproximation[0] - initialPoint.getX()) / speed.getX();
            }

            return Utils.matrixMultiplication(Utils.inverseMatrix(W), F);
        }

        @Override
        public boolean isSolutionValid(final CartesianPoint solution) {
            return !solution.isNear(initialPoint) && solution.getX() > initialPoint.getX();
        }
    }

    public static StraightCapillarFactory getCapillarFactory(double radius, double length, double roughnessSize,
                                                             double roughnessAngleR, double reflectivity,
                                                             double criticalAngleR) {
        return new StraightCapillarFactory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate) {
                return new SmoothCylinder(coordinate, ReferenceFrame.builder().atPoint(coordinate).build(),
                        radius, length, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
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

    public static RotatedCylinderFactory getRotatedCapillarFactory(double radius, double length, double roughnessSize,
                                                                   double roughnessAngleR, double reflectivity,
                                                                   double criticalAngleR) {
        return new RotatedCylinderFactory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate, final ReferenceFrame refFrame) {
                return new SmoothCylinder(coordinate, refFrame, radius, length, roughnessSize, roughnessAngleR,
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