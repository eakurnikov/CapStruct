package com.private_void.core;

public abstract class Surface {

    protected Detector detector;
    protected Point3D frontCoordinate;
    protected Vector3D normal;
    protected Vector3D axis;
    protected float radius;
    protected float length;
    protected float roughnessSize;
    protected int roughnessAngleD;
    protected float reflectivity;
    protected int slideAngleD;

    protected float x;
    protected float y;
    protected float z;

    protected float Vx;
    protected float Vy;
    protected float Vz;

    protected Surface(final Point3D frontCoordinate, float radius, float length, float roughnessSize, int roughnessAngleD, float reflectivity, int slideAngleD) {
        this.frontCoordinate = frontCoordinate;
        this.radius = radius;
        this.length = length;
        this.roughnessSize = roughnessSize;
        this.roughnessAngleD = roughnessAngleD;
        this.reflectivity = reflectivity;
        this.slideAngleD = slideAngleD;
    }

    public abstract Point3D getHitPoint(final Particle particle);

    public abstract Flux passThrough(Flux flux);

}
