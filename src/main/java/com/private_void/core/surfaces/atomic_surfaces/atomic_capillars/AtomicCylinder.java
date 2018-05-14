package com.private_void.core.surfaces.atomic_surfaces.atomic_capillars;

import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.coordinates.CylindricalPoint;
import com.private_void.core.geometry.space_3D.vectors.Vector;
import com.private_void.core.particles.AtomicChain;
import com.private_void.core.particles.ChargedParticle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.capillar_factories.CapillarFactory;

import java.util.ArrayList;
import java.util.List;

import static com.private_void.core.particles.AtomicChain.C_SQUARE;
import static com.private_void.utils.Constants.ELECTRON_CHARGE;
import static com.private_void.utils.Constants.TIME_STEP;

public class AtomicCylinder extends AtomicCapillar {

    public AtomicCylinder(final AtomicChain.Factory factory, final CartesianPoint front, int atomicChainsAmount,
                          double chargeNumber, double radius, double length) {
        super(factory, front, atomicChainsAmount, chargeNumber, radius, length);
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

            dVy += (Fy * Math.signum(y) / particle.getMass()) * TIME_STEP;
            dVz += (Fz * Math.signum(z) / particle.getMass()) * TIME_STEP;
        }

        return Vector.set(
                particle.getSpeed().getX(),
                particle.getSpeed().getY() + dVy / 300_000,
                particle.getSpeed().getZ() + dVz / 300_000);
    }

    @Override
    protected List<AtomicChain> createAtomicChains(AtomicChain.Factory factory) {
        List<AtomicChain> atomicChains = new ArrayList<>();

//      double phi = Math.PI;
        double phi = 0.0;
        for (int i = 0; i < atomicChainsAmount; i++) {
            atomicChains.add(factory.getNewAtomicChain(new CylindricalPoint(radius, phi += period, 0.0).convertToCartesian()));
        }

        return atomicChains;
    }

    @Override
    protected boolean isPointInside(CartesianPoint point) {
        return point.getX() <= front.getX() + length;
    }

    public static CapillarFactory getCapillarFactory(final AtomicChain.Factory factory, int atomicChainsAmount,
                                                     double chargeNumber, double radius, double length) {
        return new CapillarFactory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate) {
                return new AtomicCylinder(factory, coordinate, atomicChainsAmount, chargeNumber, radius, length);
            }

            @Override
            public double getRadius() {
                return radius;
            }

            @Override
            public double getLength() {
                return length;
            }
        };
    }
}

//    @Override
//    protected void createAtoms() {
//        atoms = new ArrayList<>();
//
//        double x = front.getX();
//        //double y = front.getY();
//        //double z = front.getX() - size / 2;
//
//        while (x <= front.getX() + length) {
//            x += period;
//        }
//    }