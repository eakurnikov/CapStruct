package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.rotated_smooth_capillars;

import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.coordinates.SphericalPoint;
import com.private_void.core.geometry.vectors.Vector;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.core.particles.Particle;
import com.private_void.utils.Utils;

import static com.private_void.utils.Constants.*;
import static com.private_void.utils.Generator.generator;

public class RotatedSmoothCylinder extends RotatedSmoothCapillar {

    public RotatedSmoothCylinder(final CartesianPoint front, final SphericalPoint position, double radius, double length,
                                 double roughnessSize, double roughnessAngleR, double reflectivity, double criticalAngleR) {
        super(front, position, radius, length, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
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
    protected CartesianPoint getHitPoint(final NeutralParticle p) {
        if (p.getSpeed().getX() <= 0.0) {
            p.setAbsorbed(true);
            return p.getCoordinate();
        }

        double[] solution = {
                p.getCoordinate().getX() + p.getSpeed().getX() * radius * p.getRecursiveIterationCount(),
                p.getCoordinate().getY() + p.getSpeed().getY() * radius * p.getRecursiveIterationCount(),
                p.getCoordinate().getZ() + p.getSpeed().getZ() * radius * p.getRecursiveIterationCount()};

        double[] delta = {1.0, 1.0, 1.0};
        double[] F  = new double[3];
        double[][] W = new double[3][3];

        double E = 0.05;
        int iterationsAmount = 0;

        double dr = generator().uniformDouble(0.0, roughnessSize);
        double r = radius - dr;
        double y, z;

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

                y = solution[1];
                z = solution[2];

                W[0][0] = 0.0;
                W[0][1] = 2.0 * y;
                W[0][2] = 2.0 * z;

                F[0] = y * y + z * z - r * r;

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

        CartesianPoint newCoordinate = new CartesianPoint(solution[0], solution[1], solution[2]);
        if ((newCoordinate.isNear(p.getCoordinate()) || newCoordinate.getX() <= p.getCoordinate().getX()) && !p.isRecursiveIterationsLimitReached()) {
            p.recursiveIteration();
            return getHitPoint(p);
        } else {
            if (p.isRecursiveIterationsLimitReached()) {
                p.setAbsorbed(true);
            }
            p.stopRecursiveIterations();
            return newCoordinate;
        }
    }

    @Override
    protected boolean isPointInside(final CartesianPoint point) {
        return point.getX() < length;
    }

//    @Override
//    public void toInnerRefFrame(Particle particle) {
//        particle
//                .shiftCoordinate(-front.getX(), -front.getY(), -front.getZ())
//                .rotateCoordinate(Vector.E_Y, position.getTheta())
//                .rotateCoordinate(Vector.E_Z, -position.getPhi())
//                .rotateSpeed(Vector.E_Y, position.getTheta())
//                .rotateSpeed(Vector.E_Z, -position.getPhi());
//    }
//
//    @Override
//    public void toGlobalRefFrame(Particle particle) {
//        particle
//                .rotateCoordinate(Vector.E_Z, position.getPhi())
//                .rotateCoordinate(Vector.E_Y, -position.getTheta())
//                .shiftCoordinate(front.getX(), front.getY(), front.getZ())
//                .rotateSpeed(Vector.E_Z, position.getPhi())
//                .rotateSpeed(Vector.E_Y, -position.getTheta());
//    }

    @Override
    public void toInnerRefFrame(Particle particle) {
        particle
                .shiftCoordinate(-front.getX(), -front.getY(), -front.getZ())
                .rotateRefFrameAroundOY(-position.getTheta())
                .rotateRefFrameAroundOZ(-position.getPhi());
    }

    @Override
    public void toGlobalRefFrame(Particle particle) {
        particle
                .rotateRefFrameAroundOZ(position.getPhi())
                .rotateRefFrameAroundOY(position.getTheta())
                .shiftCoordinate(front.getX(), front.getY(), front.getZ());
    }

    public static RotatedCapillar.Factory getFactory(double radius, double length, double roughnessSize, double roughnessAngleR,
                                             double reflectivity, double criticalAngleR) {
        return new RotatedCapillar.Factory() {

            @Override
            public RotatedCapillar getNewCapillar(final CartesianPoint coordinate, final SphericalPoint position) {
                return new RotatedSmoothCylinder(coordinate, position, radius, length, roughnessSize, roughnessAngleR,
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