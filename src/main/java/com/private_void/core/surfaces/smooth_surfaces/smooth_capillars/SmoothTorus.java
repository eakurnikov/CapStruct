package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.CapillarFactory;
import com.private_void.utils.Utils;

import static com.private_void.utils.Constants.*;
import static com.private_void.utils.Generator.generator;

public class SmoothTorus extends SmoothCapillar {
    private float curvRadius;
    private float curvAngleR;

    public SmoothTorus(final Point3D frontCoordinate, float radius, float curvRadius, float curvAngleR,
                       float roughnessSize, float roughnessAngleR, float reflectivity, float criticalAngleR) {
        super(frontCoordinate, radius, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        this.curvRadius = curvRadius;
        this.curvAngleR = curvAngleR;
        this.length = Utils.getTorusLength(curvRadius, curvAngleR);
    }

    @Override
    protected Vector3D getNormal(final Point3D point) {
        float x = point.getX();
        float y = point.getY();
        float z = point.getZ();

        return new Vector3D(
                (-2 * (x * x + y * y + (z + curvRadius) * (z + curvRadius) + curvRadius * curvRadius - radius * radius)
                        * 2 * x + 8 * curvRadius * curvRadius * x),

                (-2 * (x * x + y * y + (z + curvRadius) * (z + curvRadius) + curvRadius * curvRadius - radius * radius)
                        * 2 * y),

                (-2 * (x * x + y * y + (z + curvRadius) * (z + curvRadius) + curvRadius * curvRadius - radius * radius)
                        * 2 * (z + curvRadius) + 8 * curvRadius * curvRadius * (z + curvRadius)));
    }

    @Override
    protected Vector3D getAxis(final Point3D point) {
        return normal.getNewByTurningAroundOX(PI / 2).turnAroundOY(getPointsAngle(point));
    }

    @Override
    protected Point3D getHitPoint(final NeutralParticle p) {
        float[] solution = {p.getCoordinate().getX() + p.getSpeed().getX() * radius * p.getRecursiveIterationCount(),
                            p.getCoordinate().getY() + p.getSpeed().getY() * radius * p.getRecursiveIterationCount(),
                            p.getCoordinate().getZ() + p.getSpeed().getZ() * radius * p.getRecursiveIterationCount()};
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

                W[0][0] = 2 * (solution[0] * solution[0] + solution[1] * solution[1] + (solution[2] + curvRadius) * (solution[2] + curvRadius)
                        + curvRadius * curvRadius - (radius - dr) * (radius - dr)) * 2 * solution[0]
                        - 8 * curvRadius * curvRadius * solution[0];

                W[0][1] = 2 * (solution[0] * solution[0] + solution[1] * solution[1] + (solution[2] + curvRadius) * (solution[2] + curvRadius)
                        + curvRadius * curvRadius - (radius - dr) * (radius - dr)) * 2 * solution[1];

                W[0][2] = 2 * (solution[0] * solution[0] + solution[1] * solution[1] + (solution[2] + curvRadius) * (solution[2] + curvRadius)
                        + curvRadius * curvRadius - (radius - dr) * (radius - dr)) * 2 * (solution[2] + curvRadius)
                        - 8 * curvRadius * curvRadius * (solution[2] + curvRadius);

                W[1][0] =  1.0f / p.getSpeed().getX();
                W[1][1] = -1.0f / p.getSpeed().getY();
                W[1][2] =  0.0f;

                W[2][0] =  0.0f;
                W[2][1] =  1.0f / p.getSpeed().getY();
                W[2][2] = -1.0f / p.getSpeed().getZ();

                F[0] = (solution[0] * solution[0] + solution[1] * solution[1] + (solution[2] + curvRadius) * (solution[2] + curvRadius)
                        + curvRadius * curvRadius - (radius - dr) * (radius - dr))
                            * (solution[0] * solution[0] + solution[1] * solution[1] + (solution[2] + curvRadius) * (solution[2] + curvRadius)
                        + curvRadius * curvRadius - (radius - dr) * (radius - dr))
                        - 4 * curvRadius * curvRadius * (solution[0] * solution[0] + (solution[2] + curvRadius) * (solution[2] + curvRadius));

                F[1] = (solution[0] - p.getCoordinate().getX()) / p.getSpeed().getX()
                        - (solution[1] - p.getCoordinate().getY()) / p.getSpeed().getY();

                F[2] = (solution[1] - p.getCoordinate().getY()) / p.getSpeed().getY()
                        - (solution[2] - p.getCoordinate().getZ()) / p.getSpeed().getZ();

                delta = Utils.matrixMultiplication(Utils.inverseMatrix(W), F);

                for (int i = 0; i < 3; i++) {
                    solution[i] -= delta[i];
                }
            } catch (ArithmeticException e) {
                System.out.println(e.getMessage());
            } catch (Exception e){
                System.out.println(e.getMessage());
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
        float angle = getPointsAngle(point);
        return angle >= 0 && angle <= curvAngleR;
    }

    private float getPointsAngle(final Point3D point) {
        float x = point.getX();
        float y = point.getY();
        float z = point.getZ();

        return (float) Math.asin(x / Math.sqrt(x * x + y * y + (z + curvRadius) * (z + curvRadius)));
    }

    public static CapillarFactory getFactory(float radius, float curvRadius, float curvAngleR, float roughnessSize,
                                             float roughnessAngleR, float reflectivity, float criticalAngleR) {
        return new CapillarFactory() {

            @Override
            public Capillar getNewCapillar(Point3D coordinate) {
                return new SmoothTorus(coordinate, radius, curvRadius, curvAngleR, roughnessSize, roughnessAngleR,
                        reflectivity, criticalAngleR);
            }

            @Override
            public float getRadius() {
                return radius;
            }

            @Override
            public float getLength() {
                return Utils.getTorusLength(curvRadius, curvAngleR);
            }
        };
    }
}