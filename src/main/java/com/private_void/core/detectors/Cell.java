package com.private_void.core.detectors;

import com.private_void.core.geometry.coordinates.CartesianPoint;

public class Cell {
    private final double z;
    private final double y;
    private int particlesAmount;

    public Cell(final CartesianPoint coordinate) {
        this.z = coordinate.getZ();
        this.y = coordinate.getY();
        this.particlesAmount = 0;
    }

    public void register() {
        particlesAmount++;
    }

    public double getZ() {
        return z;
    }

    public double getY() {
        return y;
    }

    public int getParticlesAmount() {
        return particlesAmount;
    }
}