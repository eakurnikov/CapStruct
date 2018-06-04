package com.private_void.core.entities.surfaces.atomic_surfaces.atomic_capillars;

import com.private_void.core.entities.particles.AtomicChain;
import com.private_void.core.entities.particles.ChargedParticle;
import com.private_void.core.entities.surfaces.Capillar;
import com.private_void.core.entities.surfaces.capillar_factories.StraightCapillarFactory;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.reference_frames.ReferenceFrame;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;
import com.private_void.core.math.utils.Utils;

import java.util.List;

public class AtomicTorus extends AtomicCapillar {
    private final double curvRadius;
    private final double curvAngleR;

    public AtomicTorus(final CartesianPoint front, final ReferenceFrame refFrame,
                       final AtomicChain.Factory chainFactory, int atomicChainsAmount, double chargeNumber,
                       double radius, double curvRadius, double curvAngleR) {
        super(front, refFrame, chainFactory, atomicChainsAmount, chargeNumber, radius,
                Utils.getTorusLength(curvRadius, curvAngleR));
        this.curvRadius = curvRadius;
        this.curvAngleR = curvAngleR;
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
    protected List<AtomicChain> createAtomicChains(final AtomicChain.Factory chainFactory) {
        return null;
    }

    @Override
    protected boolean isPointInside(final CartesianPoint point) {
        return false;
    }

    public static StraightCapillarFactory getFactory(final AtomicChain.Factory chainFactory, int atomicChainsAmount,
                                                     double chargeNumber, double radius, double curvRadius, double curvAngleR) {
        return new StraightCapillarFactory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate) {
                return new AtomicTorus(coordinate, ReferenceFrame.builder().atPoint(coordinate).build(), chainFactory,
                        atomicChainsAmount, chargeNumber, radius, curvRadius, curvAngleR);
            }

            @Override
            public double getRadius() {
                return radius;
            }

            @Override
            public double getLength() {
                return Utils.getTorusLength(curvRadius, curvAngleR);
            }
        };
    }

    private double getPointsAngle(final CartesianPoint point) {
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ() + curvRadius;

        return Math.asin(x / Math.sqrt(x * x + y * y + z * z));
    }
}