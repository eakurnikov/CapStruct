package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars;

import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.utils.Utils;

import static com.private_void.utils.Constants.ITERATIONS_MAX;
import static com.private_void.utils.Constants.PI;
import static com.private_void.utils.Generator.generator;

public class SingleSmoothCylinder extends SingleSmoothCapillar {

    public SingleSmoothCylinder(final Point3D frontCoordinate, float radius, float length, float roughnessSize,
                          float roughnessAngleR, float reflectivity, float criticalAngleR) {
        super(frontCoordinate, radius, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        this.length = length;
        this.detector = new Detector(getDetectorsCoordinate(), 2 * radius);
    }

    @Override
    protected Vector3D getNormal(final Point3D point) {
        return new Vector3D(0.0f, -2 * point.getY(), -2 * point.getZ());
    }

    @Override
    protected Vector3D getAxis(final Point3D point) {
        return normal.getNewByTurningAroundOX(PI / 2);
    }

    @Override
    protected Point3D getHitPoint(final NeutralParticle p) {
        float[] solution = {
                p.getCoordinate().getX() + radius * p.getRecursiveIterationCount(),
                p.getCoordinate().getY() + (p.getSpeed().getY() / Math.abs(p.getSpeed().getY())) * radius,
                p.getCoordinate().getZ() + (p.getSpeed().getZ() / Math.abs(p.getSpeed().getZ())) * radius
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

                W[0][0] = 0.0f;
                W[0][1] = 2.0f * solution[1];
                W[0][2] = 2.0f * solution[2];

                W[1][0] = 1.0f / p.getSpeed().getX();
                W[1][1] = -1.0f / p.getSpeed().getY();
                W[1][2] = 0.0f;

                W[2][0] = 1.0f / p.getSpeed().getX();
                W[2][1] = 0.0f;
                W[2][2] = -1.0f / p.getSpeed().getZ();

                F[0] = solution[1] * solution[1] + solution[2] * solution[2] - (radius - dr) * (radius - dr);

                F[1] = (solution[0] - p.getCoordinate().getX()) / p.getSpeed().getX()
                        - (solution[1] - p.getCoordinate().getY()) / p.getSpeed().getY();

                F[2] = -(solution[2] - p.getCoordinate().getZ()) / p.getSpeed().getZ()
                        + (solution[0] - p.getCoordinate().getX()) / p.getSpeed().getX();

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

//        Point3D newCoordinate = new Point3D(solution[0], solution[1], solution[2]);
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
        return point.getX() <= front.getX() + length;
    }

    @Override
    protected Point3D getDetectorsCoordinate() {
        return new Point3D(front.getX() + length, front.getY(), front.getZ());
    }
}