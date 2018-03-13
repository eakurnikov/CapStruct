package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars;

import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.SphericalPoint;
import com.private_void.core.geometry.Vector;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.CapillarFactory;
import com.private_void.utils.Utils;

import static com.private_void.utils.Constants.*;
import static com.private_void.utils.Generator.generator;

public class SmoothTorus extends SmoothCapillar {
    private float curvRadius;
    private float curvAngleR;

    public SmoothTorus(final CartesianPoint front, float radius, float curvRadius, float curvAngleR, float roughnessSize,
                       float roughnessAngleR, float reflectivity, float criticalAngleR) {
        super(front, radius, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        this.curvRadius = curvRadius;
        this.curvAngleR = curvAngleR;
        this.length = Utils.getTorusLength(curvRadius, curvAngleR);
    }

    public SmoothTorus(float length, final CartesianPoint front, float radius, float curvAngleR, float roughnessSize,
                       float roughnessAngleR, float reflectivity, float criticalAngleR) {
        super(front, radius, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        this.curvRadius = Utils.getTorusCurvRadius(length, curvAngleR);
        this.curvAngleR = curvAngleR;
        this.length = length;
    }

    public SmoothTorus(final CartesianPoint front, final SphericalPoint position, float radius, float curvRadius,
                       float curvAngleR, float roughnessSize, float roughnessAngleR, float reflectivity,
                       float criticalAngleR) {
        super(front, radius, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        this.position = position;
        this.curvRadius = curvRadius;
        this.curvAngleR = curvAngleR;
        this.length = Utils.getTorusLength(curvRadius, curvAngleR);
    }

    public SmoothTorus(float length, final CartesianPoint front, final SphericalPoint position, float radius, float curvAngleR,
                       float roughnessSize, float roughnessAngleR, float reflectivity, float criticalAngleR) {
        super(front, radius, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        this.position = position;
        this.curvRadius = Utils.getTorusCurvRadius(length, curvAngleR);
        this.curvAngleR = curvAngleR;
        this.length = length;
    }

    @Override
    protected Vector getNormal(final CartesianPoint point) {
        float x = point.getX() - front.getX();
        float y = point.getY() - front.getY();
        float z = point.getZ() - front.getZ() - curvRadius; // + curvRadius сместит влево

        return new Vector(
                (-2 * (x * x + y * y + z * z + curvRadius * curvRadius - radius * radius) * 2 * x
                        + 8 * curvRadius * curvRadius * x),

                (-2 * (x * x + y * y + z * z + curvRadius * curvRadius - radius * radius) * 2 * y),

                (-2 * (x * x + y * y + z * z + curvRadius * curvRadius - radius * radius) * 2 * z
                        + 8 * curvRadius * curvRadius * z));
    }

    @Override
    protected Vector getAxis(final CartesianPoint point) {
        return normal.getNewByTurningAroundOX(PI / 2).turnAroundOY(getPointsAngle(point));
    }

    @Override
    protected CartesianPoint getHitPoint(final NeutralParticle p) {
        if (p.getSpeed().getX() <= 0.0f) {
            p.setAbsorbed(true);
            return p.getCoordinate();
        }

        float[] solution = {p.getCoordinate().getX() + p.getSpeed().getX() * radius * p.getRecursiveIterationCount(),
                            p.getCoordinate().getY() + p.getSpeed().getY() * radius * p.getRecursiveIterationCount(),
                            p.getCoordinate().getZ() + p.getSpeed().getZ() * radius * p.getRecursiveIterationCount()};

        float[] delta = {1.0f, 1.0f, 1.0f};
        float[] F  = new float[3];
        float[][] W = new float[3][3];

        float E = 0.05f;
        int iterationsAmount = 0;

        float dr = generator().uniformFloat(0.0f, roughnessSize);
        float r = radius - dr;
        float x, y, z;

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

                x = solution[0] - front.getX();
                y = solution[1] - front.getY();
                z = solution[2] - front.getZ() - curvRadius;

                W[0][0] = 2 * (x * x + y * y + z * z + curvRadius * curvRadius - r * r) * 2 * x
                        - 8 * curvRadius * curvRadius * x;

                W[0][1] = 2 * (x * x + y * y + z * z + curvRadius * curvRadius - r * r) * 2 * y;

                W[0][2] = 2 * (x * x + y * y + z * z + curvRadius * curvRadius - r * r) * 2 * z
                        - 8 * curvRadius * curvRadius * z;

                F[0] = (x * x + y * y + z * z + curvRadius * curvRadius - r * r) *
                       (x * x + y * y + z * z + curvRadius * curvRadius - r * r)
                        - 4 * curvRadius * curvRadius * (x * x + z * z);

                if (p.getSpeed().getY() == 0.0f) {
                    W[1][0] = 0.0f;
                    W[1][1] = 1.0f;
                    W[1][2] = 0.0f;

                    F[1] = solution[1] - p.getCoordinate().getY();
                } else {
                    W[1][0] = 1.0f / p.getSpeed().getX();
                    W[1][1] = -1.0f / p.getSpeed().getY();
                    W[1][2] = 0.0f;

                    F[1] = (solution[0] - p.getCoordinate().getX()) / p.getSpeed().getX()
                            - (solution[1] - p.getCoordinate().getY()) / p.getSpeed().getY();
                }

                if (p.getSpeed().getZ() == 0.0f) {
                    W[2][0] = 0.0f;
                    W[2][1] = 0.0f;
                    W[2][2] = 1.0f;

                    F[2] = solution[2] - p.getCoordinate().getZ();
                } else {
                    W[2][0] = 0.0f;
                    W[2][1] = 1.0f / p.getSpeed().getY();
                    W[2][2] = -1.0f / p.getSpeed().getZ();

                    F[2] = (solution[1] - p.getCoordinate().getY()) / p.getSpeed().getY()
                            - (solution[2] - p.getCoordinate().getZ()) / p.getSpeed().getZ();
                }

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

        CartesianPoint newCoordinate = new CartesianPoint(solution[0], solution[1], solution[2]);
        if ((newCoordinate.isNear(p.getCoordinate()) || newCoordinate.getX() <= p.getCoordinate().getX()) && !p.isRecursiveIterationsLimitReached()) {
            p.recursiveIteration();
            return getHitPoint(p);
        } else {
            p.stopRecursiveIterations();
            return newCoordinate;
        }
    }

    @Override
    protected boolean isPointInside(final CartesianPoint point) {
        return point.getX() < front.getX() + length;
    }

    @Override
    protected void transformToReferenceFrame(Particle particle, ReferenceFrame frame) {
        // todo
    }

    private float getPointsAngle(final CartesianPoint point) {
        float x = point.getX();
        float y = point.getY();
        float z = point.getZ();

        return (float) Math.asin(x / Math.sqrt(x * x + y * y + (z - curvRadius) * (z - curvRadius)));
    }

    public static CapillarFactory getFactory(float radius, float curvRadius, float curvAngleR, float roughnessSize,
                                             float roughnessAngleR, float reflectivity, float criticalAngleR) {
        return new CapillarFactory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate, final SphericalPoint position) {
                return new SmoothTorus(coordinate, position, radius, curvRadius, curvAngleR, roughnessSize, roughnessAngleR,
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

    public static CapillarFactory getFactoryWithLength(float radius, float length, float curvAngleR, float roughnessSize,
                                             float roughnessAngleR, float reflectivity, float criticalAngleR) {
        return new CapillarFactory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate, final SphericalPoint position) {
                return new SmoothTorus(length, coordinate, position, radius, curvAngleR, roughnessSize, roughnessAngleR,
                        reflectivity, criticalAngleR);
            }

            @Override
            public float getRadius() {
                return radius;
            }

            @Override
            public float getLength() {
                return length;
            }
        };
    }
}