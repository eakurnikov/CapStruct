package com.private_void.core.particles;

import java.util.List;
import java.util.concurrent.RecursiveAction;

public abstract class Interaction extends RecursiveAction {
    private static final int THREADS_AMOUNT = 100;

    protected List<? extends Particle> particles;
    protected int startIndex;
    protected int length;

    protected Interaction(List<? extends Particle> particles, int startIndex, int length) {
        this.particles = particles;
        this.startIndex = startIndex;
        this.length = length;
    }

    @Override
    protected void compute() {
        if (length < particles.size() / THREADS_AMOUNT) {
            start();
        } else {
            int newLength = length / 2;
            invokeAll(
                    newInteraction(particles, startIndex, newLength),
                    newInteraction(particles,startIndex + newLength, length - newLength));
        }
    }

    protected abstract void start();

    protected abstract Interaction newInteraction(List<? extends Particle> particles, int startIndex, int length);
}
