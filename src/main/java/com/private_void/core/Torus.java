package com.private_void.core;

import com.private_void.utils.Utils;

import static com.private_void.core.Constants.CELL_SIZE;
import static com.private_void.core.Constants.PI;
import static com.private_void.utils.Generator.generator;

public class Torus extends Surface {
    private float torusRadius;
    private float curvAngleR;
    
    //TODO возможно заюзать мой метод square
    //TODO припилить Stream API или просто распараллелить через ThreadPool

    public Torus(final Point3D frontCoordinate, float radius, float torusRadius, float curvAngleD, float roughnessSize,
                 float roughnessAngleD, float reflectivity, float slideAngleD) {

        super(frontCoordinate, radius, roughnessSize, roughnessAngleD, reflectivity, slideAngleD);
        this.torusRadius = torusRadius;
        this.curvAngleR = Utils.convertDegreesToRads(curvAngleD);
        this.detector = new RotatedDetector(
                new Point3D((float) (frontCoordinate.getX() + torusRadius * Math.sin(curvAngleD)), frontCoordinate.getY(),
                            (float) (frontCoordinate.getZ() - torusRadius * (1 - Math.cos(curvAngleR)))),
                2 * radius, CELL_SIZE, curvAngleD
        );
    }

    @Override
    public Point3D getHitPoint(final Particle particle) {
        float[] solution = {particle.getCoordinate().getX() + particle.getSpeed().getX() * radius,
                            particle.getCoordinate().getY() + particle.getSpeed().getY() * radius,
                            particle.getCoordinate().getZ() + particle.getSpeed().getZ() * radius};
        float[] delta = {1.0f, 1.0f, 1.0f};
        float[] F  = new float[3];
        float[][] W = new float[3][3];

        float E = 0.05f;
        float dr = generator().uniformFloat(0.0f, roughnessSize);

        int iterationsAmount = 0;
        int iterationsAmountMax = 200;

        while (Utils.getMax(delta) > E) {
            try {
                // Костыль для уничтожения частиц, вычисление координат которых зациклилось
                iterationsAmount++;
                if (iterationsAmount > iterationsAmountMax) {
                    particle.setAbsorbed(true);
                    break;
                }

                W[0][0] = 2 * (solution[0] * solution[0] + solution[1] * solution[1] + (solution[2] + torusRadius) * (solution[2] + torusRadius) + torusRadius * torusRadius - (radius - dr) * (radius - dr)) * 2 * solution[0] - 8 * torusRadius * torusRadius * solution[0];
                W[0][1] = 2 * (solution[0] * solution[0] + solution[1] * solution[1] + (solution[2] + torusRadius) * (solution[2] + torusRadius) + torusRadius * torusRadius - (radius - dr) * (radius - dr)) * 2 * solution[1];
                W[0][2] = 2 * (solution[0] * solution[0] + solution[1] * solution[1] + (solution[2] + torusRadius) * (solution[2] + torusRadius) + torusRadius * torusRadius - (radius - dr) * (radius - dr)) * 2 * solution[0] - 8 * torusRadius * torusRadius * (solution[2] + torusRadius);

                W[1][0] =  1.0f / particle.getSpeed().getX();
                W[1][1] = -1.0f / particle.getSpeed().getY();
                W[1][2] =  0.0f;

                W[2][0] =  0.0f;
                W[2][1] =  1.0f / particle.getSpeed().getY();
                W[2][2] = -1.0f / particle.getSpeed().getZ();

                F[0] = (solution[0] * solution[0] + solution[1] * solution[1] + (solution[2] + torusRadius) * (solution[2] + torusRadius) + torusRadius * torusRadius - (radius - dr) * (torusRadius - dr)) * (solution[0] * solution[0] + solution[1] * solution[1] + (solution[2] + torusRadius) * (solution[2] + torusRadius) + torusRadius * torusRadius - (radius - dr) * (radius - dr)) - 4 * torusRadius * torusRadius * (solution[0] * solution[0] + (solution[2] + torusRadius) * (solution[2] + torusRadius));
                F[1] = (solution[0] - particle.getCoordinate().getX()) / particle.getSpeed().getX() - (solution[1] - particle.getCoordinate().getY()) / particle.getSpeed().getY();
                F[2] = (solution[1] - particle.getCoordinate().getY()) / particle.getSpeed().getY() - (solution[2] - particle.getCoordinate().getZ()) / particle.getSpeed().getZ();

                delta = Utils.matrixMultiplication(Utils.inverseMatrix(W), F);

                for (int i = 0; i < 3; i++) {
                    solution[i] -= delta[i]; //возможно, нужно разыменовывать дельту
                }
            } catch (ArithmeticException e) {
                System.out.println("Division by zero.");
                System.out.println(e.getMessage());
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return new Point3D(solution[0], solution[1], solution[2]);
    }

    @Override
    public Flux passThrough(Flux flux) {
        Point3D newCoordinate;
        float angleVN;
        float x0 = frontCoordinate.getX();
        int reboundsCountMax = 300;

        for (Particle particle : flux.getParticles()) {
            x = particle.getCoordinate().getX();
            y = particle.getCoordinate().getY();
            z = particle.getCoordinate().getZ();

            Vx = particle.getSpeed().getX();
            Vy = particle.getSpeed().getY();
            Vz = particle.getSpeed().getZ();

            if (((Vy / Vx) * (x0 - x) + y) * ((Vy / Vx) * (x0 - x) + y) +
                ((Vz / Vx) * (x0 - x) + z) * ((Vz / Vx) * (x0 - x) + z) < radius * radius) {
                // Костыль для уничтожения частиц, у которых произошло слишком много отражений внутри каплляра. В принципе он не нужен
                // так как, если будет много отражений, интенсивность просто убьется. Но нужно протестировать
                int reboundsCount = 0;

                newCoordinate = getHitPoint(particle);

                x = newCoordinate.getX();
                y = newCoordinate.getY();
                z = newCoordinate.getZ();

                while (Math.asin(x / Math.sqrt(x * x + y * y + (z + torusRadius) * (z + torusRadius))) <= curvAngleR) {
                    particle.increaseTrace(newCoordinate);
                    particle.setCoordinate(newCoordinate);
                    reboundsCount++;

                    setNormal((-2 * (x * x + y * y + (z + torusRadius) * (z + torusRadius) + torusRadius * torusRadius - radius * radius) * 2 * x + 8 * torusRadius * torusRadius * x),
                              (-2 * (x * x + y * y + (z + torusRadius) * (z + torusRadius) + torusRadius * torusRadius - radius * radius) * 2 * y),
                              (-2 * (x * x + y * y + (z + torusRadius) * (z + torusRadius) + torusRadius * torusRadius - radius * radius) * 2 * (z + torusRadius) + 8 * torusRadius * torusRadius * (z + torusRadius)));
                    setAxis(1.0f, 0.0f, 0.0f);

                    axis.turnAroundOY(generator().uniformFloat(0.0f, 2.0f * PI));
                    normal.turnAroundVector(generator().uniformFloat(0.0f, roughnessAngleR), axis);
                    axis = normal.getNewVectorByTurningAroundOX(generator().uniformFloat(0.0f, PI));

                    angleVN = particle.getSpeed().getAngle(normal);
                    if (angleVN < slideAngleR && reboundsCount  < reboundsCountMax) {
                        particle.getSpeed().turnAroundVector(angleVN, axis);
                        particle.decreaseIntensity(reflectivity);
                    }
                    else {
                        particle.setAbsorbed(true);
                        break;
                    }
                }
            }
            else {
                detector.increaseOutOfCapillarParticlesAmount();
                detector.increaseOutOfCapillarInensity(particle.getIntensity());
            }
        }
        return detector.detect(flux);
    }
}