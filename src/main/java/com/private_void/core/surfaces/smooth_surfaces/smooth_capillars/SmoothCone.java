package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.core.surfaces.CapillarFactory;
import com.private_void.utils.Utils;

import static com.private_void.utils.Constants.*;
import static com.private_void.utils.Generator.generator;

public class SmoothCone extends SmoothCapillar {
    private float length;
    private float divergentAngleR;

    public SmoothCone(final Point3D frontCoordinate, float radius, int divergentAngleD, float coneCoefficient,
                      float roughnessSize, float roughnessAngleD, float reflectivity, float criticalAngleD)
            throws IllegalArgumentException {

        super(frontCoordinate, radius, roughnessSize, roughnessAngleD, reflectivity, criticalAngleD);
        if (coneCoefficient >= 1 || coneCoefficient <= 0) {
            throw new IllegalArgumentException();
        }
        this.divergentAngleR = Utils.convertDegreesToRadians(divergentAngleD);
        this.length = (float) (radius * (1 / Math.tan(divergentAngleR)) * coneCoefficient);
    }

    public SmoothCone(final Point3D frontCoordinate, float radius, float length, float coneCoefficient, float roughnessSize,
                      float roughnessAngleD, float reflectivity, float criticalAngleD) throws IllegalArgumentException {

        super(frontCoordinate, radius, roughnessSize, roughnessAngleD, reflectivity, criticalAngleD);
        if (coneCoefficient >= 1 || coneCoefficient <= 0) {
            throw new IllegalArgumentException();
        }
        this.length = length;
        this.divergentAngleR = (float) Math.atan((radius / length) * coneCoefficient);
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

                W[0][0] = 1.0f;

                W[0][1] = (float) (solution[1] * (1.0f / Math.tan(divergentAngleR))
                        / (Math.sqrt(solution[1] * solution[1] + solution[2] * solution[2])));

                W[0][2] = (float) (solution[2] * (1.0f / Math.tan(divergentAngleR))
                        / (Math.sqrt(solution[1] * solution[1] + solution[2] * solution[2])));

                W[1][0] = 1.0f / p.getSpeed().getX();
                W[1][1] = -1.0f / p.getSpeed().getY();
                W[1][2] = 0.0f;

                W[2][0] = 1.0f / p.getSpeed().getX();
                W[2][1] = 0.0f;
                W[2][2] = -1.0f / p.getSpeed().getZ();

                F[0] = (float) (solution[1] + Math.sqrt(solution[1] * solution[1] + solution[2] * solution[2])
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
        return point.getX() <= front.getX() + length;
    }

    public static CapillarFactory getFactory(float radius, int divergentAngleD, float coneCoefficient, float roughnessSize,
                                             float roughnessAngleD, float reflectivity, float criticalAngleD) {
        return (final Point3D coordinate) ->
                new SmoothCone(coordinate, radius, divergentAngleD, coneCoefficient, roughnessSize, roughnessAngleD,
                        reflectivity, criticalAngleD);
    }
}