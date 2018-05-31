package com.private_void.core.surfaces.atomic_surfaces.single_atomic_capillars;

import com.private_void.core.detection.Detector;
import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.coordinates.CylindricalPoint;
import com.private_void.core.geometry.space_3D.vectors.Vector;
import com.private_void.core.particles.AtomicChain;
import com.private_void.core.particles.ChargedParticle;
import com.private_void.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.private_void.utils.Constants.ELECTRON_CHARGE;

public class SingleAtomicTorus extends SingleAtomicCapillar {
    private final double curvRadius;
    private final double curvAngleR;

    public SingleAtomicTorus(final CartesianPoint front, final AtomicChain.Factory chainFactory, int atomicChainsAmount,
                             double chargeNumber, double radius, double curvRadius, double curvAngleR) {
        super(front, chainFactory, atomicChainsAmount, chargeNumber, radius, Utils.getTorusLength(curvRadius, curvAngleR));
        this.curvRadius = curvRadius;
        this.curvAngleR = curvAngleR;
        this.detector = createDetector();
    }

    @Override
    protected Vector getAxis(final CartesianPoint point) {
        return Vector.E_X.rotateAroundOY(getPointsAngle(point));
    }

    @Override
    protected double getCriticalAngle(final ChargedParticle particle) {
        return Math.sqrt(2.0 * particle.getChargeNumber() * chargeNumber * (ELECTRON_CHARGE * ELECTRON_CHARGE) /
                particle.getEnergy() * atomicChains.get(0).getPeriod()) * 1000;
    }

    @Override
    protected Vector rotateParticleSpeed(final ChargedParticle particle) {
        return null;
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
                new CartesianPoint(
                        front.getX() + curvRadius * Math.sin(curvAngleR),
                        front.getY(),
                        front.getZ() + curvRadius * (1 - Math.cos(curvAngleR))),
                2.0 * radius);
    }

    private double getPointsAngle(final CartesianPoint point) {
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ() + curvRadius;

        return Math.asin(x / Math.sqrt(x * x + y * y + z * z));
    }
}