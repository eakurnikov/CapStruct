package com.private_void.core.entities.surfaces.atomic_surfaces.atomic_capillars.single_atomic_capillars;

import com.private_void.core.entities.detectors.Detector;
import com.private_void.core.entities.particles.AtomicChain;
import com.private_void.core.entities.particles.ChargedParticle;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.coordinates.CylindricalPoint;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;

import java.util.ArrayList;
import java.util.List;

import static com.private_void.core.constants.Constants.ELECTRON_CHARGE;
import static com.private_void.core.entities.particles.AtomicChain.C_SQUARE;

public class SingleAtomicCylinder extends SingleAtomicCapillar {

    public SingleAtomicCylinder(final CartesianPoint front, final AtomicChain.Factory chainFactory,
                                int atomicChainsAmount, double chargeNumber, double radius, double length) {
        super(front, chainFactory, atomicChainsAmount, chargeNumber, radius, length);
        this.detector = createDetector();
    }

    @Override
    protected void setCriticalAngle(final ChargedParticle particle) {
        criticalAngle = Math.sqrt(2.0 * particle.getChargeNumber() * chargeNumber * (ELECTRON_CHARGE * ELECTRON_CHARGE) /
                particle.getEnergy() * atomicChains.get(0).getPeriod()) * ANGLE_SCALE;
    }

    @Override
    protected Vector getAxis(final CartesianPoint point) {
        return Vector.E_X;
    }

    @Override
    protected double[] getAcceleration(final CartesianPoint coordinate, double particleChargeNumber, double mass) {
        double y;
        double z;
        double r;

        double F;
        double Fy = 0.0;
        double Fz = 0.0;

        for (AtomicChain chain : atomicChains) {
            y = coordinate.getY() - chain.getCoordinate().getY();
            z = coordinate.getZ() - chain.getCoordinate().getZ();
            r = Math.sqrt(y * y + z * z);

            F = 2.0 * particleChargeNumber * chargeNumber * (ELECTRON_CHARGE * ELECTRON_CHARGE) /
                    (chain.getPeriod() * (r + (r * r * r) / (C_SQUARE * shieldingDistance * shieldingDistance)));

            Fy += F * (y / r);
            Fz += F * (z / r);
        }

        return new double[] {Fy / (mass * ACCELERATION_SCALE), Fz / (mass * ACCELERATION_SCALE)};
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