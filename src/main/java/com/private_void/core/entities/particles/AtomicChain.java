package com.private_void.core.entities.particles;

import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;

public class AtomicChain {
    public static final double C_SQUARE = 3.0;

    private final CartesianPoint coordinate;
    private final double period;

    private AtomicChain(final CartesianPoint coordinate, double period) {
        this.coordinate = coordinate;
        this.period = period;
    }

    public CartesianPoint getCoordinate() {
        return coordinate;
    }

    public double getPeriod() {
        return period;
    }

    public static AtomicChain.Factory getFactory(double period) {

        return new AtomicChain.Factory() {

            @Override
            public AtomicChain getNewAtomicChain(CartesianPoint coordinate) {
                return new AtomicChain(coordinate, period);
            }

            @Override
            public double getPeriod() {
                return period;
            }
        };
    }

    public interface Factory {

        AtomicChain getNewAtomicChain(final CartesianPoint coordinate);

        double getPeriod();
    }
}