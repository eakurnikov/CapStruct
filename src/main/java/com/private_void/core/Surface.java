package com.private_void.core;

public abstract class Surface {

    protected Detector detector;
    protected Point3D frontCoordinate;
    protected Vector3D normal;
    protected float radius;
    protected float length;
    protected float roughness;

    protected Surface(final Point3D frontCoordinate, float radius, float length, float roughness) {
        this.frontCoordinate = frontCoordinate;
        this.radius = radius;
        this.length = length;
        this.roughness = roughness;
    }

    public abstract Point3D getHitPoint(final Particle particle);

    public abstract Flux passThrough(Flux flux);

}
