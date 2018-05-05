package com.private_void.core.surfaces.atomic_surfaces.single_atomic_capillars;

import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.vectors.Vector;
import com.private_void.core.particles.AtomicChain;
import com.private_void.core.particles.ChargedParticle;

import java.util.ArrayList;
import java.util.List;

import static com.private_void.core.particles.AtomicChain.C_SQUARE;
import static com.private_void.utils.Constants.ELECTRON_CHARGE;
import static com.private_void.utils.Constants.TIME_STEP;

public class SingleAtomicCylinder extends SingleAtomicCapillar {

    public SingleAtomicCylinder(final AtomicChain.Factory factory, final CartesianPoint front, double period,
                                double chargeNumber, double radius, double length) {
        super(factory, front, period, chargeNumber, radius, length);
        this.detector = createDetector();
    }

    @Override
    protected Vector getAxis(final CartesianPoint point) {
        return Vector.E_X;
    }

    @Override
    protected double getCriticalAngle(final ChargedParticle particle) {
        return Math.sqrt(2.0 * particle.getChargeNumber() * chargeNumber * (ELECTRON_CHARGE * ELECTRON_CHARGE) /
                particle.getEnergy() * atomicChains.get(0).getPeriod()) * 100;
    }

    @Override
    protected Vector rotateParticleSpeed(final ChargedParticle particle) {
        double y;
        double z;

        double Fy;
        double Fz;

        double dVy = 0.0;
        double dVz = 0.0;

//        particle.setCoordinate(new CartesianPoint(0.0, 0.0, 0.0));
//        particle.setSpeed(Vector.E_X);

        for (AtomicChain chain : atomicChains) {
            y = particle.getCoordinate().getY() - chain.getCoordinate().getY();
            z = particle.getCoordinate().getZ() - chain.getCoordinate().getZ();

            Fy = - particle.getChargeNumber() * chargeNumber * (ELECTRON_CHARGE * ELECTRON_CHARGE) /
                    (chain.getPeriod() * (C_SQUARE * (shieldingDistance / y) * (shieldingDistance / y) + 1.0));

            Fz = - particle.getChargeNumber() * chargeNumber * (ELECTRON_CHARGE * ELECTRON_CHARGE) /
                    (chain.getPeriod() * (C_SQUARE * (shieldingDistance / z) * (shieldingDistance / z) + 1.0));

            dVy += (Fy * Math.signum(chain.getCoordinate().getY()) / particle.getMass()) * TIME_STEP;
            dVz += (Fz * Math.signum(chain.getCoordinate().getZ()) / particle.getMass()) * TIME_STEP;
        }

        return Vector.set(
                particle.getSpeed().getX(),
                particle.getSpeed().getY() + dVy,
                particle.getSpeed().getZ() + dVz);
    }

    @Override
    protected List<AtomicChain> createAtomicChains(final AtomicChain.Factory factory) {
        List<AtomicChain> atomicChains = new ArrayList<>();

        atomicChains.add(factory.getNewAtomicChain(new CartesianPoint(0.0, radius, 0.0)));
        atomicChains.add(factory.getNewAtomicChain(new CartesianPoint(0.0, 0.0, radius)));
        atomicChains.add(factory.getNewAtomicChain(new CartesianPoint(0.0, 0.0, -radius)));
        atomicChains.add(factory.getNewAtomicChain(new CartesianPoint(0.0, -radius, 0.0)));

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