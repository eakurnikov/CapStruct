package com.private_void.core;

import com.private_void.utils.Generator;
import com.private_void.utils.Utils;

public class Cylinder extends Surface{

    private float length;

    public Cylinder(final Point3D frontCoordinate, float radius, float length, float roughnessSize, int roughnessAngleD, float reflectivity, int slideAngleD) {
        super(frontCoordinate, radius, roughnessSize, roughnessAngleD, reflectivity, slideAngleD);
        this.length = length;
        this.detector = new Detector(new Point3D(frontCoordinate.getX() + length, frontCoordinate.getY(), frontCoordinate.getZ()),
                                    2 * radius,
                                    1.0f,
                                    0,
                                     radius);
    }

    @Override
    public Point3D getHitPoint(final Particle particle) {
        float[] solution = {particle.getCoordinate().getX() + radius,
                            particle.getCoordinate().getY() + (particle.getSpeed().getY() / Math.abs(particle.getSpeed().getY())) * radius,
                            particle.getCoordinate().getZ() + (particle.getSpeed().getZ() / Math.abs(particle.getSpeed().getZ())) * radius};
        float[] delta = {1.0f, 1.0f, 1.0f};
        float[] F  = new float[3];
        float[][] W = new float[3][3];

        float E = 0.05f;
        float dr = Generator.getInstance().uniformFloat() * roughnessSize;

        while (Utils.getMax(delta) > E) {
            try {

                W[0][0] = 0.0f;
                W[0][1] = 2.0f * solution[1];
                W[0][2] = 2.0f * solution[2];

                W[1][0] = 1.0f / particle.getSpeed().getX();
                W[1][1] = -1.0f / particle.getSpeed().getY();
                W[1][2] = 0.0f;

                W[2][0] = 1.0f / particle.getSpeed().getX();
                W[2][1] = 0.0f;
                W[2][2] = -1.0f / particle.getSpeed().getZ();

                F[0] = solution[1] * solution[1] + solution[2] * solution[2] - (radius - dr) * (radius - dr);
                F[1] = (solution[0] - particle.getCoordinate().getX()) / particle.getSpeed().getX() - (solution[1] - particle.getCoordinate().getY()) / particle.getSpeed().getY();
                F[2] = -(solution[2] - particle.getCoordinate().getZ()) / particle.getSpeed().getZ() + (solution[0] - particle.getCoordinate().getX()) / particle.getSpeed().getX();

                delta = Utils.matrixMultiplication(Utils.inverseMatrix(W), F);

                for (int i = 0; i < 3; i++) {
                    solution[i] -= delta[i];
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

        for (Particle particle : flux.getParticles()) {

            x = particle.getCoordinate().getX();
            y = particle.getCoordinate().getY();
            z = particle.getCoordinate().getZ();

            Vx = particle.getSpeed().getX();
            Vy = particle.getSpeed().getY();
            Vz = particle.getSpeed().getZ();

            if (((Vy / Vx) * (x0 - x) + y) * ((Vy / Vx) * (x0 - x) + y) +
                ((Vz / Vx) * (x0 - x) + z) * ((Vz / Vx) * (x0 - x) + z) < radius * radius) {

                newCoordinate = getHitPoint(particle);

                while (newCoordinate.getX() <= length) {

                    particle.increaseTrace(newCoordinate);
                    particle.setCoordinate(newCoordinate);

                    setNormal(0.0f, -2 * newCoordinate.getY(), -2 * newCoordinate.getZ());
                    setAxis(1.0f, 0.0f, 0.0f);

                    axis.turnAroundOY(Utils.convertDegreesToRads(Generator.getInstance().uniformInt(360)));
                    normal.turnAroundVector(Utils.convertDegreesToRads(Generator.getInstance().uniformInt(roughnessAngleD)), axis);
                    axis = normal.getNewVectorByTurningAroundOX(Utils.convertDegreesToRads(Generator.getInstance().uniformInt(90)));

                    angleVN = particle.getSpeed().getAngle(normal);
                    if (angleVN < Utils.convertDegreesToRads(slideAngleD)) {
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