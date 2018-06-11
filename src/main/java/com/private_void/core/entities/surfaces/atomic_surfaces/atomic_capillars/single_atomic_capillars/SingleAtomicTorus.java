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

import static com.private_void.core.constants.Constants.*;
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
                front.getX() + curvRadius * Math.sin(currentCurvAngle),
                front.getY(),
                front.getZ() + curvRadius * (1 - Math.cos(currentCurvAngle)));

        CartesianPoint particleCoordinateInCurrentRefFrame = new ReferenceFrame.Converter(
                ReferenceFrame.builder()
                        .atPoint(currentCrossSectionCenter)
                        .setAngleAroundOY(currentCurvAngle)
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
    protected Vector getNextParticleSpeed(final ChargedParticle particle) {
        CartesianPoint nextCoordinate = particle.getCoordinate().shift(
                particle.getSpeed().getX() * STEP / 2.0,
                particle.getSpeed().getY() * STEP / 2.0,
                particle.getSpeed().getZ() * STEP / 2.0
        );
        CartesianPoint nextForce = getForce(nextCoordinate, particle.getChargeNumber());
        return Vector.set(
                particle.getSpeed().getX() + (nextForce.getX() * TIME_STEP) / (450.0 * particle.getMass()),
                particle.getSpeed().getY() + (nextForce.getY() * TIME_STEP) / (450.0 * particle.getMass()),
                particle.getSpeed().getZ() + (nextForce.getZ() * TIME_STEP) / (450.0 * particle.getMass())
        );
    }

    @Override
    protected CartesianPoint getNextParticleCoordinate(final ChargedParticle particle) {
        CartesianPoint currentForce = getForce(particle.getCoordinate(), particle.getChargeNumber());
        return particle.getCoordinate().shift(
                particle.getSpeed().getX() * STEP + (currentForce.getX() * STEP * STEP) / (2.0 * particle.getMass()),
                particle.getSpeed().getY() * STEP + (currentForce.getY() * STEP * STEP) / (2.0 * particle.getMass()),
                particle.getSpeed().getZ() * STEP + (currentForce.getZ() * STEP * STEP) / (2.0 * particle.getMass())
        );
    }

    private CartesianPoint getForce(final CartesianPoint point, double particleChargeNumber) {
        double y;
        double z;
        double r;

        double F;
        double Fy = 0.0;
        double Fz = 0.0;

        double currentCurvAngle = getPointsAngle(point);
        CartesianPoint currentCrossSectionCenter = new CartesianPoint(
                front.getX() + curvRadius * Math.sin(currentCurvAngle),
                front.getY(),
                front.getZ() + curvRadius * (1 - Math.cos(currentCurvAngle)));

        CartesianPoint particleCoordinateInCurrentRefFrame = new ReferenceFrame.Converter(
                ReferenceFrame.builder()
                        .atPoint(currentCrossSectionCenter)
                        .setAngleAroundOY(currentCurvAngle)
                        .build())
                .convert(point);

        for (AtomicChain chain : atomicChains) {
            y = particleCoordinateInCurrentRefFrame.getY() - chain.getCoordinate().getY();
            z = particleCoordinateInCurrentRefFrame.getZ() - chain.getCoordinate().getZ();
            r = Math.sqrt(y * y + z * z);

            F = 2.0 * particleChargeNumber * chargeNumber * (ELECTRON_CHARGE * ELECTRON_CHARGE) /
                    (chain.getPeriod() * (r + (r * r * r) / C_SQUARE));

            Fy += F * (y / r);
            Fz += F * (z / r);
        }

        return new CartesianPoint(0.0, Fy, Fz);
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