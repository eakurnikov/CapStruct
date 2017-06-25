package com.private_void.core;

import com.private_void.utils.Utils;

public abstract class Surface {
    protected Detector detector;
    protected Point3D frontCoordinate;
    protected Vector3D normal;
    protected Vector3D axis;
    protected float radius;
    protected float roughnessSize;
    protected float roughnessAngleR;
    protected float reflectivity;
    protected float slideAngleR;

    protected float x;
    protected float y;
    protected float z;

    protected float Vx;
    protected float Vy;
    protected float Vz;

    protected Surface(final Point3D frontCoordinate, float radius, float roughnessSize, float roughnessAngleD, float reflectivity, float slideAngleD) {
        this.frontCoordinate = frontCoordinate;
        this.radius = radius;
        this.roughnessSize = roughnessSize;
        this.roughnessAngleR = Utils.convertDegreesToRads(roughnessAngleD);
        this.reflectivity = reflectivity;
        this.slideAngleR = Utils.convertDegreesToRads(slideAngleD);

        normal = new Vector3D(0.0f, 1.0f, 0.0f);
        axis = new Vector3D(1.0f, 0.0f, 0.0f);
    }

    public abstract Point3D getHitPoint(final Particle particle);

    public abstract Flux passThrough(Flux flux);

    public void setNormal(float x, float y, float z) {
        normal.setX(x);
        normal.setY(y);
        normal.setZ(z);
    }

    public void setAxis(float x, float y, float z) {
        axis.setX(x);
        axis.setY(y);
        axis.setZ(z);
    }

    public int getDetectedParticlesAmount() {
        return detector.detectedParticlesAmount;
    }

    public int getNotDetectedParticlesAmount() {
        return detector.notDetectedParticlesAmount;
    }

    public int getOutOfCapillarParticlesAmount() {
        return detector.outOfCapillarParticlesAmount;
    }

    public float getDetectedIntensity() {
        return detector.detectedIntensity;
    }

    public float getNotDetectedIntensity() {
        return detector.notDetectedIntensity;
    }

    public float getOutOfCapillarIntensity() {
        return detector.outOfCapillarIntensity;
    }
}