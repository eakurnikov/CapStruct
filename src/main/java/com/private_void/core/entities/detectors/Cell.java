package com.private_void.core.entities.detectors;

import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;

public class Cell {
    private final double x;
    private final double y;
    private int particlesAmount;

    Cell(final CartesianPoint coordinate) {
        this.x = coordinate.getZ();
        this.y = coordinate.getY();
        this.particlesAmount = 0;
    }

    public void register() {
        particlesAmount++;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getParticlesAmount() {
        return particlesAmount;
    }
}