package com.private_void.core.surfaces.atomic_surfaces.single_atomic_capillars;

import com.private_void.core.detection.Detector;
import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.coordinates.CylindricalPoint;
import com.private_void.core.geometry.space_3D.vectors.Vector;
import com.private_void.core.particles.AtomicChain;
import com.private_void.core.particles.ChargedParticle;

import java.util.ArrayList;
import java.util.List;

import static com.private_void.core.particles.AtomicChain.C_SQUARE;
import static com.private_void.utils.Constants.ELECTRON_CHARGE;
import static com.private_void.utils.Constants.TIME_STEP;

public class SingleAtomicCylinder extends SingleAtomicCapillar {

    public SingleAtomicCylinder(final CartesianPoint front, final AtomicChain.Factory chainFactory,
                                int atomicChainsAmount, double chargeNumber, double radius, double length) {
        super(front, chainFactory, atomicChainsAmount, chargeNumber, radius, length);
        this.detector = createDetector();
    }

    @Override
    protected Vector getAxis(final CartesianPoint point) {
        return Vector.E_X;
    }

    @Override
    protected double getCriticalAngle(final ChargedParticle particle) {
        return Math.sqrt(2.0 * particle.getChargeNumber() * chargeNumber * (ELECTRON_CHARGE * ELECTRON_CHARGE) /
                particle.getEnergy() * atomicChains.get(0).getPeriod()) * 1000;
    }

    @Override
    protected Vector rotateParticleSpeed(final ChargedParticle particle) {
        double y;
        double z;
        double r;
        double F;

        double dVy = 0.0;
        double dVz = 0.0;

        for (AtomicChain chain : atomicChains) {
            y = particle.getCoordinate().getY() - chain.getCoordinate().getY();
            z = particle.getCoordinate().getZ() - chain.getCoordinate().getZ();
            r = Math.sqrt(y * y + z * z);

            F = 2.0 * particle.getChargeNumber() * chargeNumber * (ELECTRON_CHARGE * ELECTRON_CHARGE) /
                    (chain.getPeriod() * (r + (r * r * r) / C_SQUARE));

            dVy += (F * (y / r) / particle.getMass()) * TIME_STEP;
            dVz += (F * (z / r) / particle.getMass()) * TIME_STEP;
        }

        return Vector.set(
                particle.getSpeed().getX(),
                particle.getSpeed().getY() + dVy / 800,
                particle.getSpeed().getZ() + dVz / 800);
    }

    @Override
    protected List<AtomicChain> createAtomicChains(final AtomicChain.Factory chainFactory) {
        List<AtomicChain> atomicChains = new ArrayList<>();

        double phi = 0.0;
        for (int i = 0; i < atomicChainsAmount; i++) {
            atomicChains.add(
                    chainFactory.getNewAtomicChain(
                            new CylindricalPoint(radius, phi += period, 0.0).convertToCartesian()));
        }

        return atomicChains;
    }

    @Override
    protected boolean isPointInside(final CartesianPoint point) {
        return point.getX() <= front.getX() + length;
    }

    @Override
    protected Detector createDetector() {
        return new Detector(
                new CartesianPoint(front.getX() + length, front.getY(), front.getZ()),
                2.0 * radius);
    }
}