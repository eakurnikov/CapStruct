package com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.single_smooth_capillars;

import com.private_void.core.detectors.Detector;
import com.private_void.core.detectors.RotatedDetector;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.utils.Utils;

import static com.private_void.utils.Constants.ITERATIONS_MAX;
import static com.private_void.utils.Constants.PI;
import static com.private_void.utils.Generator.generator;

public class SingleSmoothTorus extends SingleSmoothCapillar {
    private float curvRadius;
    private float curvAngleR;

    public SingleSmoothTorus(final Point3D frontCoordinate, float radius, float curvRadius, float curvAngleR,
                       float roughnessSize, float roughnessAngleR, float reflectivity, float criticalAngleR) {
        super(frontCoordinate, radius, roughnessSize, roughnessAngleR, reflectivity, criticalAngleR);
        this.curvRadius = curvRadius;
        this.curvAngleR = curvAngleR;
        this.length = Utils.getTorusLength(curvRadius, curvAngleR);
//        this.detector = new RotatedDetector(getDetectorsCoordinate(), 2 * radius, curvAngleR);
        this.detector = new Detector(getDetectorsCoordinate(), 2 * radius);
    }

    @Override
    protected Vector3D getNormal(final Point3D point) {
        float x = point.getX() - front.getX();
        float y = point.getY() - front.getY();
        float z = point.getZ() - front.getZ() - curvRadius; // + curvRadius сместит влево

        return new Vector3D(
                (-2 * (x * x + y * y + z * z + curvRadius * curvRadius - radius * radius) * 2 * x
                        + 8 * curvRadius * curvRadius * x),

                (-2 * (x * x + y * y + z * z + curvRadius * curvRadius - radius * radius) * 2 * y),

                (-2 * (x * x + y * y + z * z + curvRadius * curvRadius - radius * radius) * 2 * z
                        + 8 * curvRadius * curvRadius * z));
    }

    @Override
    protected Vector3D getAxis(final Point3D point) {
        return normal.getNewByTurningAroundOX(PI / 2).turnAroundOY(getPointsAngle(point));
    }

    @Override
    protected Point3D getHitPoint(final NeutralParticle p) {
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
        float x, y ,z;

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

        Point3D newCoordinate = new Point3D(solution[0], solution[1], solution[2]);
        if ((newCoordinate.isNear(p.getCoordinate()) || newCoordinate.getX() <= p.getCoordinate().getX()) && !p.isRecursiveIterationsLimitReached()) {
            p.recursiveIteration();
            return getHitPoint(p);
        } else {
            p.stopRecursiveIterations();
            return newCoordinate;
        }
    }

    @Override
    protected boolean isPointInside(final Point3D point) {
//        float angle = getPointsAngle(point);
//        return angle >= 0 && angle <= curvAngleR;

        return point.getX() < front.getX() + length;
    }

    @Override
    protected Point3D getDetectorsCoordinate() {
        return new Point3D(
                (float) (front.getX() + curvRadius * Math.sin(curvAngleR)),
                front.getY(),
                (float) (front.getZ() + curvRadius * (1 - Math.cos(curvAngleR))));
    }

    private float getPointsAngle(final Point3D point) {
        float x = point.getX();
        float y = point.getY();
        float z = point.getZ();

        return (float) Math.asin(x / Math.sqrt(x * x + y * y + (z - curvRadius) * (z - curvRadius)));
    }
}