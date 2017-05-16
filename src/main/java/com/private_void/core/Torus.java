package com.private_void.core;

public class Torus extends Surface {

    private float torusRadius;
    private float curvAngleD;
    //private float curvAngleR;

    public Torus(final Point3D frontCoordinate, float radius, float torusRadius, float curvAngleD, float roughnessSize,
                 int roughnessAngleD, float reflectivity, int slideAngleD) {

        super(frontCoordinate, radius, roughnessSize, roughnessAngleD, reflectivity, slideAngleD);

        this.torusRadius = torusRadius;
        this.curvAngleD = curvAngleD;
        //this.curvAngleR = Utils.convertDegreesToRads(curvAngleD);
        this.detector = new Detector(new Point3D(frontCoordinate.getX() /* + length*/, //TODO разобраться с заданием координаты Х детектора. Он должен находиться на конце тора, для этого скорее всего надо вычислять длину тора по его углу
                                                 frontCoordinate.getY(),
                                                 (float) (frontCoordinate.getZ() - radius - (torusRadius + radius) * (1 - Math.cos(Utils.convertDegreesToRads(curvAngleD))))),
                                    2 * radius,
                                    1.0f,
                                     curvAngleD,
                                     radius);

    }

    @Override
    public Point3D getHitPoint(Particle particle) {

        float[] solution = {particle.getCoordinate().getX() + particle.getSpeed().getX() * radius,
                            particle.getCoordinate().getY() + particle.getSpeed().getY() * radius,
                            particle.getCoordinate().getZ() + particle.getSpeed().getZ() * radius};
        float[] delta = {1.0f, 1.0f, 1.0f};
        float[] F  = new float[3];
        float[][] W = new float[3][3];

        float E = 0.05f;
        float dr = rand.nextFloat() * roughnessSize;

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

                Utils.matrixMultiplication(Utils.inverseMatrix(W), F, delta);

                for (int i = 0; i < 3; i++) {
                    solution[i] -= delta[i]; //возможно, нужно разыменовывать дельту
                }

            }
            catch (ArithmeticException e) {
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

                // Костыль для уничтожения частиц, у которых произошло слишком много отражений внутри каплляра
                int reboundsCount = 0;

                while (Math.asin(x / Math.sqrt(x * x + y * y + (z + torusRadius) * (z + torusRadius))) <= Utils.convertDegreesToRads(curvAngleD)) {

                    newCoordinate = getHitPoint(particle);
                    particle.increaseTrace(newCoordinate);
                    particle.setCoordinate(newCoordinate);
                    reboundsCount++;

                    x = newCoordinate.getX();
                    y = newCoordinate.getY();
                    z = newCoordinate.getZ();

                    setNormal((-2 * (x * x + y * y + (z + torusRadius) * (z + torusRadius) + torusRadius * torusRadius - radius * radius) * 2 * x + 8 * torusRadius * torusRadius * x),
                              (-2 * (x * x + y * y + (z + torusRadius) * (z + torusRadius) + torusRadius * torusRadius - radius * radius) * 2 * y),
                              (-2 * (x * x + y * y + (z + torusRadius) * (z + torusRadius) + torusRadius * torusRadius - radius * radius) * 2 * (z + torusRadius) + 8 * torusRadius * torusRadius * (z + torusRadius)));
                    setAxis(1.0f, 0.0f, 0.0f);

                    axis.turnAroundOY(Utils.convertDegreesToRads(rand.nextInt(360)));
                    normal.turnAroundVector(Utils.convertDegreesToRads(rand.nextInt(roughnessAngleD)), axis);
                    axis = normal.getNewVectorByTurningAroundOX(Utils.convertDegreesToRads(rand.nextInt(90)));

                    angleVN = particle.getSpeed().getAngle(normal);
                    if (angleVN < Utils.convertDegreesToRads(slideAngleD) && reboundsCount  < reboundsCountMax) {
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
            }

        }

        return flux;
    }
}