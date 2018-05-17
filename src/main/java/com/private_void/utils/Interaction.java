package com.private_void.utils;

import com.private_void.core.particles.Particle;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public final class Interaction extends RecursiveAction {
    private static final int THREADS_AMOUNT = 100;

    private List<? extends Particle> particles;
    private int startIndex;
    private int length;
    private Processable processable;

    public Interaction(List<? extends Particle> particles, int startIndex, int length, Processable processable) {
        this.particles = particles;
        this.startIndex = startIndex;
        this.length = length;
        this.processable = processable;
    }

    public void start() {
        new ForkJoinPool().invoke(this);
    }

    @Override
    protected void compute() {
        if (length < particles.size() / THREADS_AMOUNT) {
            processable.process(particles, startIndex, length);
        } else {
            int newLength = length / 2;
            invokeAll(
                    new Interaction(particles, startIndex, newLength, processable),
                    new Interaction(particles,startIndex + newLength, length - newLength, processable));
        }
    }

    public interface Processable {
        void process(List<? extends Particle> particles, int startIndex, int length);
    }
}