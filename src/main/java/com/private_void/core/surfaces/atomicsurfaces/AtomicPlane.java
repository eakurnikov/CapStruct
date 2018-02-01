package com.private_void.core.surfaces.atomicsurfaces;

import com.private_void.core.detectors.Detector;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.particles.AtomFactory;
import com.private_void.core.particles.ChargedParticle;
import com.private_void.core.particles.Particle;

import java.util.ArrayList;
import java.util.Iterator;

public class AtomicPlane extends AtomicSurface {
    private float size;

    public AtomicPlane(final Point3D frontCoordinate, final AtomFactory factory, float period, float chargeNumber,
                       float size) {
        super(frontCoordinate, factory, period, chargeNumber);
        this.size = size;
        this.detector = new Detector(new Point3D(frontCoordinate.getX() + size, frontCoordinate.getY(), frontCoordinate.getZ()), size);
        createAtoms();
    }

    @Override
    protected void createAtoms() {
        atoms = new ArrayList<>();

        float x = frontCoordinate.getX();
        float y = frontCoordinate.getY();
        float z = frontCoordinate.getZ() - size / 2;

        while (x <= frontCoordinate.getX() + size) {
            while (z <= frontCoordinate.getZ() + size / 2) {
                atoms.add(atomFactory.getNewAtom(new Point3D(x, y, z), chargeNumber));
                z += period;
            }
            x += period;
            z = frontCoordinate.getZ() - size / 2;
        }
    }

    @Override
    protected float getCriticalAngle(ChargedParticle particle) {
        return 0;
    }

    @Override
    protected float getPotential(ChargedParticle particle) {
        return 0;
    }

    @Override
    public void interact(Flux flux) {
        ChargedParticle p;
        Point3D newCoordinate;
        Iterator<? extends Particle> iterator = flux.getParticles().iterator();

        while (iterator.hasNext()) {

        }

        detector.detect(flux);
    }
}
