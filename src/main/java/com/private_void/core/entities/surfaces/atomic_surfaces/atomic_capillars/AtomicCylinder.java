package com.private_void.core.entities.surfaces.atomic_surfaces.atomic_capillars;

import com.private_void.core.entities.particles.AtomicChain;
import com.private_void.core.entities.particles.ChargedParticle;
import com.private_void.core.entities.surfaces.Capillar;
import com.private_void.core.entities.surfaces.capillar_factories.RotatedCylinderFactory;
import com.private_void.core.entities.surfaces.capillar_factories.StraightCapillarFactory;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.coordinates.CylindricalPoint;
import com.private_void.core.math.geometry.space_3D.reference_frames.ReferenceFrame;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;

import java.util.ArrayList;
import java.util.List;

import static com.private_void.core.constants.Constants.ELECTRON_CHARGE;
import static com.private_void.core.entities.particles.AtomicChain.C_SQUARE;

public class AtomicCylinder extends AtomicCapillar {

    public AtomicCylinder(final CartesianPoint front, final ReferenceFrame refFrame,
                          final AtomicChain.Factory chainFactory, int atomicChainsAmount, double chargeNumber,
                          double radius, double length) {
        super(front, refFrame, chainFactory, atomicChainsAmount, chargeNumber, radius, length);
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

    public static StraightCapillarFactory getCapillarFactory(final AtomicChain.Factory chainFactory, int chainsAmount,
                                                             double chargeNumber, double radius, double length) {
        return new StraightCapillarFactory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate) {
                return new AtomicCylinder(coordinate, ReferenceFrame.builder().atPoint(coordinate).build(),
                        chainFactory, chainsAmount, chargeNumber, radius, length);
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

    public static RotatedCylinderFactory getRotatedCapillarFactory(final AtomicChain.Factory chainFactory,
                                                                   int chainsAmount, double chargeNumber, double radius,
                                                                   double length) {
        return new RotatedCylinderFactory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate, final ReferenceFrame refFrame) {
                return new AtomicCylinder(coordinate, refFrame, chainFactory, chainsAmount, chargeNumber, radius,
                        length);
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