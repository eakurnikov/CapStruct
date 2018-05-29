package com.private_void.core.surfaces.atomic_surfaces.single_atomic_capillars;

import com.private_void.core.detection.Detector;
import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.vectors.Vector;
import com.private_void.core.particles.AtomicChain;
import com.private_void.core.particles.ChargedParticle;
import com.private_void.utils.Utils;

import java.util.List;

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
        return 0;
    }

    @Override
    protected Vector rotateParticleSpeed(final ChargedParticle particle) {
        return null;
    }

    @Override
    protected List<AtomicChain> createAtomicChains(AtomicChain.Factory factory) {
        return null;
    }

    @Override
    protected boolean isPointInside(CartesianPoint point) {
        return false;
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