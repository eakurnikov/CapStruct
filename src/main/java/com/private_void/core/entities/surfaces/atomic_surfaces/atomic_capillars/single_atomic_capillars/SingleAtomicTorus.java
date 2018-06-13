package com.private_void.core.entities.surfaces.atomic_surfaces.atomic_capillars.single_atomic_capillars;

import com.private_void.core.entities.detectors.Detector;
import com.private_void.core.entities.particles.AtomicChain;
import com.private_void.core.entities.particles.ChargedParticle;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.coordinates.CylindricalPoint;
import com.private_void.core.math.geometry.space_3D.reference_frames.ReferenceFrame;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;
import com.private_void.core.math.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.private_void.core.constants.Constants.ELECTRON_CHARGE;
import static com.private_void.core.entities.particles.AtomicChain.C_SQUARE;

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
    protected void setCriticalAngle(final ChargedParticle particle) {
        criticalAngle = Math.sqrt(2.0 * particle.getChargeNumber() * chargeNumber * (ELECTRON_CHARGE * ELECTRON_CHARGE) /
                particle.getEnergy() * atomicChains.get(0).getPeriod()) * ANGLE_SCALE;
    }

    @Override
    protected Vector getAxis(final CartesianPoint point) {
        return Vector.E_X.rotateAroundOY(getPointsAngle(point));
    }

    @Override
    protected double[] getAcceleration(final CartesianPoint coordinate, double particleChargeNumber, double mass) {
        double y;
        double z;
        double r;

        double F;
        double Fy = 0.0;
        double Fz = 0.0;

        double currentCurvAngle = getPointsAngle(coordinate);
        CartesianPoint currentCrossSectionCenter = new CartesianPoint(
                front.getX() + curvRadius * Math.sin(currentCurvAngle),
                front.getY(),
                front.getZ() + curvRadius * (1 - Math.cos(currentCurvAngle)));

        CartesianPoint particleCoordinateInCurrentRefFrame = new ReferenceFrame.Converter(
                ReferenceFrame.builder()
                        .atPoint(currentCrossSectionCenter)
                        .setAngleAroundOY(currentCurvAngle)
                        .build())
                .convert(coordinate);

        for (AtomicChain chain : atomicChains) {
            y = particleCoordinateInCurrentRefFrame.getY() - chain.getCoordinate().getY();
            z = particleCoordinateInCurrentRefFrame.getZ() - chain.getCoordinate().getZ();
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
                new CartesianPoint(
                        front.getX() + curvRadius * Math.sin(curvAngleR),
                        front.getY(),
                        front.getZ() + curvRadius * (1 - Math.cos(curvAngleR))),
                2.0 * radius);
    }

    private double getPointsAngle(final CartesianPoint point) {
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ() - curvRadius;

        return Math.asin(x / Math.sqrt(x * x + y * y + z * z));
    }
}