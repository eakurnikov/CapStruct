package com.private_void.core;

import com.private_void.utils.Utils;

import java.util.Iterator;

import static com.private_void.core.Constants.CELL_SIZE;
import static com.private_void.core.Constants.PI;
import static com.private_void.utils.Generator.generator;

public class Cone extends Surface {
    private float length;
    private float divergentAngleR;

    public Cone(final Point3D frontCoordinate, float radius, int divergentAngleD, float coneCoefficient, float roughnessSize,
                float roughnessAngleD, float reflectivity, float slideAngleD) throws IllegalArgumentException {

        super(frontCoordinate, radius, roughnessSize, roughnessAngleD, reflectivity, slideAngleD);
        if (coneCoefficient >= 1 || coneCoefficient <= 0) {
            throw new IllegalArgumentException();
        }
        this.divergentAngleR = Utils.convertDegreesToRads(divergentAngleD);
        this.length = (float) (radius * (1 / Math.tan(divergentAngleR)) * coneCoefficient);
        this.detector = new Detector(new Point3D(frontCoordinate.getX() + length, frontCoordinate.getY(), frontCoordinate.getZ()),
                                    2 * radius, CELL_SIZE);
    }

    public Cone(final Point3D frontCoordinate, float radius, float length, float coneCoefficient, float roughnessSize,
                float roughnessAngleD, float reflectivity, float slideAngleD) throws IllegalArgumentException {

        super(frontCoordinate, radius, roughnessSize, roughnessAngleD, reflectivity, slideAngleD);
        if (coneCoefficient >= 1 || coneCoefficient <= 0) {
            throw new IllegalArgumentException();
        }
        this.length = length;
        this.divergentAngleR = (float) Math.atan((radius / length) * coneCoefficient);
        this.detector = new Detector(new Point3D(frontCoordinate.getX() + length, frontCoordinate.getY(), frontCoordinate.getZ()),
                2 * radius, CELL_SIZE);
    }

    @Override
    public Point3D getHitPoint(Particle particle) {
        float[] solution = {particle.getCoordinate().getX() + radius,
                            particle.getCoordinate().getY() + (particle.getSpeed().getY() / Math.abs(particle.getSpeed().getY())) * radius,
                            particle.getCoordinate().getZ() + (particle.getSpeed().getZ() / Math.abs(particle.getSpeed().getZ())) * radius};
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

                W[0][0] = 1.0f;
                W[0][1] = (float) (solution[1] * (1.0f / Math.tan(divergentAngleR)) / (Math.sqrt(solution[1] * solution[1] + solution[2] * solution[2])));
                W[0][2] = (float) (solution[2] * (1.0f / Math.tan(divergentAngleR)) / (Math.sqrt(solution[1] * solution[1] + solution[2] * solution[2])));

                W[1][0] = 1.0f / particle.getSpeed().getX();
                W[1][1] = -1.0f / particle.getSpeed().getY();
                W[1][2] = 0.0f;

                W[2][0] = 1.0f / particle.getSpeed().getX();
                W[2][1] = 0.0f;
                W[2][2] = -1.0f / particle.getSpeed().getZ();

                F[0] = (float) (solution[1] + Math.sqrt(solution[1] * solution[1] + solution[2] * solution[2]) * (1.0f / Math.tan(divergentAngleR)) - (radius - dr) * (1.0f / Math.tan(divergentAngleR)));
                F[1] = (solution[0] - particle.getCoordinate().getX()) / particle.getSpeed().getX() - (solution[1] - particle.getCoordinate().getY()) / particle.getSpeed().getY();
                F[2] = (solution[0] - particle.getCoordinate().getX()) / particle.getSpeed().getX() - (solution[2] - particle.getCoordinate().getZ()) / particle.getSpeed().getZ();

                delta = Utils.matrixMultiplication(Utils.inverseMatrix(W), F);

                for (int i = 0; i < 3; i++) {
                    solution[i] -= delta[i];
                }
            } catch (ArithmeticException e) {
                System.out.println("Division by zero.");
                System.out.println(e.getMessage());
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return new Point3D(solution[0], solution[1], solution[2]);
    }

    @Override
    public void passThrough(Flux flux) {
        Point3D newCoordinate;
        float angleVN;
        float x0 = frontCoordinate.getX();
        int reboundsCountMax = 300;

        Iterator<Particle> iterator = flux.getParticles().iterator();
        Particle particle;

        while (iterator.hasNext()) {
            particle = iterator.next();

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

                while (newCoordinate.getX() <= length) {
                    particle.setCoordinate(newCoordinate);
                    reboundsCount++;

                    setNormal(-1.0f, (float) (-y * (1.0f / Math.tan(divergentAngleR)) / (Math.sqrt(y * y + z * z))), (float) (-z * (1.0f / Math.tan(divergentAngleR)) / (Math.sqrt(y * y + z * z))));
                    setAxis(1.0f, 0.0f, 0.0f);

                    axis.turnAroundOY(generator().uniformFloat(0.0f, 2.0f * PI));
                    normal.turnAroundVector(generator().uniformFloat(0.0f, roughnessAngleR), axis);
                    axis = normal.getNewVectorByTurningAroundOX(PI / 2);

                    angleVN = particle.getSpeed().getAngle(normal);
                    if (angleVN <= slideAngleR && reboundsCount  < reboundsCountMax) {
                        particle.getSpeed().turnAroundVector(2 * angleVN, axis);
                        particle.decreaseIntensity(reflectivity);
                    } else {
                        particle.setAbsorbed(true);
                        break;
                    }
                }
            }
            else {
                detector.increaseOutOfCapillarParticlesAmount();
                detector.increaseOutOfCapillarInensity(particle.getIntensity());
                iterator.remove();
            }
        }
        detector.detect(flux);
    }
}