package com.private_void.core.particles;

import com.private_void.core.geometry.CartesianPoint;

public class Atom {
    private CartesianPoint coordinate;
    private double charge;

    private Atom(final CartesianPoint coordinate, double charge) {
        this.coordinate = coordinate;
        this.charge = charge;
    }

    public static AtomFactory getFactory() {
        return Atom::new;
    }
}