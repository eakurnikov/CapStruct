package com.private_void.utils.newtons_method;

import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.utils.Utils;
import com.private_void.utils.exceptions.BadParticleException;

public class NewtonsMethod {
    private static final int RECURSIVE_ITERATIONS_MAX = 300;
    private static final int ITERATIONS_MAX = 200;
    private static final double ACCURACY = 0.05;

    private final Equation equation;

    public NewtonsMethod(final Equation equation) {
        this.equation = equation;
    }

    public CartesianPoint getSolution() throws BadParticleException {
        return getSolution(1);
    }

    private CartesianPoint getSolution(int recursiveIterationsCounter) throws BadParticleException {
        if (recursiveIterationsCounter == RECURSIVE_ITERATIONS_MAX) {
            throw new BadParticleException();
        }

        double[] approximation = equation.getInitialApproximation(recursiveIterationsCounter);
        double[] delta = new double[]{1.0, 1.0, 1.0};

        int iterationsCounter = 0;

        while (Utils.getMax(delta) > ACCURACY) {
            if (iterationsCounter == ITERATIONS_MAX) {
                return getSolution(recursiveIterationsCounter + 1);
            }

            delta = equation.solve(approximation);

            for (int i = 0; i < 3; i++) {
                approximation[i] -= delta[i];
            }

            iterationsCounter++;
        }

        CartesianPoint solution = new CartesianPoint(approximation);
        if (equation.isSolutionValid(solution)) {
            return solution;
        } else {
            return getSolution(recursiveIterationsCounter + 1);
        }
    }

    public interface Equation {

        double[] getInitialApproximation(int recursiveIterationCounter);

        double[] solve(double[] currentApproximation);

        boolean isSolutionValid(final CartesianPoint solution);
    }
}