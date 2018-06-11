package com.private_void.core.entities.surfaces.atomic_surfaces.atomic_capillars;

import com.private_void.core.entities.particles.AtomicChain;
import com.private_void.core.entities.particles.ChargedParticle;
import com.private_void.core.entities.surfaces.Capillar;
import com.private_void.core.entities.surfaces.capillar_factories.RotatedTorusFactory;
import com.private_void.core.entities.surfaces.capillar_factories.StraightCapillarFactory;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.coordinates.CylindricalPoint;
import com.private_void.core.math.geometry.space_3D.reference_frames.ReferenceFrame;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;
import com.private_void.core.math.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.private_void.core.constants.Constants.ELECTRON_CHARGE;
import static com.private_void.core.constants.Constants.TIME_STEP;
import static com.private_void.core.entities.particles.AtomicChain.C_SQUARE;

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

    public AtomicTorus(double length, final CartesianPoint front, final ReferenceFrame refFrame,
                       final AtomicChain.Factory chainFactory, int atomicChainsAmount, double chargeNumber,
                       double radius, double curvAngleR) {
        super(front, refFrame, chainFactory, atomicChainsAmount, chargeNumber, radius, length);
        this.curvRadius = Utils.getTorusCurvRadius(length, curvAngleR);
        this.curvAngleR = curvAngleR;
    }

    @Override
    protected Vector getAxis(final CartesianPoint point) {
        return Vector.E_X.rotateAroundOY(getPointsAngle(point));
    }

    @Override
    protected void setCriticalAngle(final ChargedParticle particle) {
        criticalAngle = Math.sqrt(2.0 * particle.getChargeNumber() * chargeNumber * (ELECTRON_CHARGE * ELECTRON_CHARGE) /
                particle.getEnergy() * atomicChains.get(0).getPeriod()) * 200;
    }

    @Override
    protected Vector rotateParticleSpeed(final ChargedParticle particle) {
        double y;
        double z;
        double r;
        double F;

        double dVy = 0.0;
        double dVz = 0.0;

        double currentCurvAngle = getPointsAngle(particle.getCoordinate());

        CartesianPoint currentCrossSectionCenter = new CartesianPoint(
                curvRadius * Math.sin(currentCurvAngle),
                0.0,
                -curvRadius * (1.0 - Math.cos(currentCurvAngle)));

        CartesianPoint particleCoordinateInCurrentRefFrame = new ReferenceFrame.Converter(
                ReferenceFrame.builder()
                        .atPoint(currentCrossSectionCenter)
                        .setAngleAroundOY(-currentCurvAngle)
                        .build())
                .convert(particle.getCoordinate());

        for (AtomicChain chain : atomicChains) {
            y = particleCoordinateInCurrentRefFrame.getY() - chain.getCoordinate().getY();
            z = particleCoordinateInCurrentRefFrame.getZ() - chain.getCoordinate().getZ();
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
        return point.getX() <= length;
    }

    public static StraightCapillarFactory getCapillarFactory(final AtomicChain.Factory chainFactory,
                                                             int chainsAmount, double chargeNumber, double radius,
                                                             double curvRadius, double curvAngleR) {
        return new StraightCapillarFactory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate) {
                return new AtomicTorus(coordinate, ReferenceFrame.builder().atPoint(coordinate).build(), chainFactory,
                        chainsAmount, chargeNumber, radius, curvRadius, curvAngleR);
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

    public static RotatedTorusFactory getRotatedCapillarFactory(final AtomicChain.Factory chainFactory,
                                                                int chainsAmount, double chargeNumber,
                                                                double radius, double length) {
        return new RotatedTorusFactory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate, double curvAngleR,
                                           final ReferenceFrame refFrame) {
                return new AtomicTorus(length, coordinate, refFrame, chainFactory, chainsAmount, chargeNumber, radius,
                        curvAngleR);
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

    private double getPointsAngle(final CartesianPoint point) {
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ() + curvRadius;

        return Math.asin(x / Math.sqrt(x * x + y * y + z * z));
    }
}