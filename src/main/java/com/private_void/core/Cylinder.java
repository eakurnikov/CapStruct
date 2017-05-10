package com.private_void.core;

import java.util.Random;

//TODO: нужно подхватывать кожффициент для roughness из GUI
public class Cylinder extends Surface{

    Random rand;

    public Cylinder(final Point3D frontCoordinate, float radius, float length, float roughness) {

        super(frontCoordinate, radius, length, roughness);
        rand = new Random();
        //m_normal =

        detector = new Detector(new Point3D(frontCoordinate.getX() + length, frontCoordinate.getY(), frontCoordinate.getZ()),
                               2 * radius,
                               1.0f,
                               0.0f,
                                radius);
    }

    public void setNormal(float x, float y, float z) {
        normal.setX(x);
        normal.setY(y);
        normal.setZ(z);
    }

    @Override
    public Point3D getHitPoint(final Particle particle) {

        float[] solution = {particle.getCoordinate().getX() + radius,
                            particle.getCoordinate().getY() + (particle.getSpeed().getY() / Math.abs(particle.getSpeed().getY())) * radius,
                            particle.getCoordinate().getZ() + (particle.getSpeed().getZ() / Math.abs(particle.getSpeed().getZ())) * radius};
        float[] delta = {1.0f, 1.0f, 1.0f};
        float[][] W = new float[3][3];
        float[][] inversedW = new float[3][3];
        float[] F  = new float[3];

        float E = 0.05f;
        float dr = 0;

        while (Utils.getMax(delta) > E) {
            //dr = Form1.rand.NextDouble() * Form1.rough / 100;

            try {
                dr = rand.nextFloat() * roughness / 100; //100 заменить на КОЭФФИЦИЕНТ

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

                Utils.inverseMatrix(W, inversedW);
                Utils.matrixMultiplication(inversedW, F, delta);

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
        return null;
    }

    //    public Flux PassThrough(Flux flux) {
//        for (Particle particle : flux.getParticles()) {
//            if (/* частица попадает в капилляр */) {
//                while (particle.getCoordinate().getX() <= length) {
//                    particle.setCoordinate(getHitPoint(particle));
//                    setNormal(0.0f, -2 * particle.getCoordinate().getY(), -2 * particle.getCoordinate().getZ());
//                    Vector3D axis = new Vector3D(0.0f, 0.0f, 0.0f);//
//                    axis.turnAroundOY(Utils.convertDegreesToRads(rand.nextInt(360)));
//                    normal.turnAroundVector(0.01 * rand.nextInt(rough_angle) * Math.PI / 180, axis,); //TODO: что за параметр 0.01, rough_angle берется из GUI
//                    axis.TurnAroundOX(Math.PI / 2, Ray[i, j].normal, ref Ray[i, j].axis); // TODO: тут какой-то новый мистический поворот вокруг оХ, но с двумя векторами. В старом коде, оказывается их два, разбирайся
//
//                    if (Vector.Angle(Ray[i, j].speed, Ray[i, j].normal) < 28 * Math.PI / 180) {
//                        Vector.TurnAroundVector(Vector.Angle(Ray[i, j].speed, Ray[i, j].normal), Ray[i, j].axis, ref Ray[i, j].speed);
//                    }
//                    else {
//                        Ray[i, j].absorbed = true;
//                        break;
//                    }
//
//                    Ray[i, j].LooseIntensity();
//                    Ray[i, j].IncreaseTrace(q);
//                    Ray[i, j].SetCoordinate(q[0], q[1], q[2]);
//                }
//            }
//
//        }
//
//    }

}

//if (((Ray[i, j].speed[1] / Ray[i, j].speed[0]) * (x0 - Ray[i, j].coordinate[0]) + Ray[i, j].coordinate[1]) * ((Ray[i, j].speed[1] / Ray[i, j].speed[0]) * (x0 - Ray[i, j].coordinate[0]) + Ray[i, j].coordinate[1]) + ((Ray[i, j].speed[2] / Ray[i, j].speed[0]) * (x0 - Ray[i, j].coordinate[0]) + Ray[i, j].coordinate[2]) * ((Ray[i, j].speed[2] / Ray[i, j].speed[0]) * (x0 - Ray[i, j].coordinate[0]) + Ray[i, j].coordinate[2]) < r * r)
//        {
//        while (Ray[i, j].coordinate[0] <= L)
//        {
//        Approximation.Cylinder(Ray[i, j].speed, Ray[i, j].coordinate, r, q);
//        Ray[i, j].SetNormal(0, -2 * q[1], -2 * q[2]);
//        Vector.TurnAroundOY(rand.Next(360) * Math.PI / 180, ref axis);
//        Vector.TurnAroundVector(0.01 * rand.Next(rough_angle) * Math.PI / 180, axis, ref Ray[i, j].normal);
//        Vector.TurnAroundOX(Math.PI / 2, Ray[i, j].normal, ref Ray[i, j].axis);
//
//        if (Vector.Angle(Ray[i, j].speed, Ray[i, j].normal) < 28 * Math.PI / 180)
//        {
//        Vector.TurnAroundVector(Vector.Angle(Ray[i, j].speed, Ray[i, j].normal), Ray[i, j].axis, ref Ray[i, j].speed);
//        }
//        else
//        {
//        Ray[i, j].absorbed = true;
//        break;
//        }
//
//        Ray[i, j].LooseIntensity();
//        Ray[i, j].IncreaseTrace(q);
//        Ray[i, j].SetCoordinate(q[0], q[1], q[2]);
//        }
//
//        if (!Ray[i, j].absorbed && Ray[i, j].I > 0.1)
//        {
//        Ray[i, j].SetNormal(0, 2 * Ray[i, j].coordinate[1], 2 * Ray[i, j].coordinate[2]);
//        Vector.TurnAroundOX(Math.PI / 2, Ray[i, j].normal, ref Ray[i, j].axis);
//        Vector.TurnAroundVector(Vector.Angle(Ray[i, j].speed, Ray[i, j].normal), Ray[i, j].axis, ref Ray[i, j].speed);
//
//        try
//        {
//        Ray[i, j].SetCoordinate(Math.Tan(torus_angle) * (Ray[i, j].coordinate[0] * Ray[i, j].speed[2] - Ray[i, j].coordinate[2] * Ray[i, j].speed[0] - (L) * Ray[i, j].speed[2]) / (Math.Tan(torus_angle) * Ray[i, j].speed[2] - Ray[i, j].speed[0]) + (L), (Ray[i, j].speed[1] / Ray[i, j].speed[2]) * ((Ray[i, j].coordinate[0] * Ray[i, j].speed[2] - Ray[i, j].coordinate[2] * Ray[i, j].speed[0] - (L) * Ray[i, j].speed[2]) / (Math.Tan(torus_angle) * Ray[i, j].speed[2] - Ray[i, j].speed[0]) - Ray[i, j].coordinate[2]) + Ray[i, j].coordinate[1], (Ray[i, j].coordinate[0] * Ray[i, j].speed[2] - Ray[i, j].coordinate[2] * Ray[i, j].speed[0] - (L) * Ray[i, j].speed[2]) / (Math.Tan(torus_angle) * Ray[i, j].speed[2] - Ray[i, j].speed[0]));
//        detector[Convert.ToInt32(Math.Ceiling((Ray[i, j].coordinate[2] - q0[0]) / size)), Convert.ToInt32(Math.Ceiling((Ray[i, j].coordinate[1] - q0[1]) / size))] += Ray[i, j].I;
//
//        count_on_det++;
//        }
//
//        catch (Exception ex)
//        {
//        count_out_det++;
//
//        if (count_out_det > Amount * Length * 0.9)
//        {
//        MessageBox.Show(ex.Message, "Частицы не попали на детектор", MessageBoxButtons.OK, MessageBoxIcon.Error);
//        Application.Exit();
//        }
//        }
//        }
//
//        else
//        {
//        count_out_det++;
//        }
//        }
//
//        else
//        {
//        count_out_cap++;
//        }
