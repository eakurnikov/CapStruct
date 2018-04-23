package com.private_void.core.particles;

import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;

public class Atom {
    private final CartesianPoint coordinate;
    private final double charge;

    private Atom(final CartesianPoint coordinate, double charge) {
        this.coordinate = coordinate;
        this.charge = charge;
    }

    public static Atom.Factory getFactory() {
        return Atom::new;
    }

    public interface Factory {
        Atom getNewAtom(final CartesianPoint coordinate, double charge);
    }
}