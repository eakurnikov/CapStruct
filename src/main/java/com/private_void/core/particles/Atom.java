package com.private_void.core.particles;

import com.private_void.core.geometry.Point3D;

public class Atom {
    private Point3D coordinate;
    private float charge;

    private Atom(final Point3D coordinate, float charge) {
        this.coordinate = coordinate;
        this.charge = charge;
    }

    public static AtomFactory getFactory() {
        return Atom::new;
    }
}