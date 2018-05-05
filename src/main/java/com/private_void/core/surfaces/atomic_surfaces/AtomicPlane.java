package com.private_void.core.surfaces.atomic_surfaces;

import com.private_void.app.Logger;
import com.private_void.core.detectors.Detector;
import com.private_void.core.detectors.Distribution;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.vectors.Vector;
import com.private_void.core.particles.ChargedParticle;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.CapillarSystem;

import java.util.Iterator;

import static com.private_void.core.particles.AtomicChain.C_SQUARE;
import static com.private_void.utils.Constants.*;

public class AtomicPlane extends AtomicSurface implements CapillarSystem {
    private final double size;
    private final double chargePlanarDensity;
    private final Detector detector;

    public AtomicPlane(final CartesianPoint front, double period, double chargeNumber, double size) {
        super(front, period, chargeNumber);
        this.size = size;
        this.chargePlanarDensity = 1 / (period * period);
        this.detector = new Detector(new CartesianPoint(front.getX() + size, front.getY(), front.getZ()), size);
    }

    @Override
    public Distribution interact(Flux flux) {
        Logger.interactionStart();

        ChargedParticle particle;
        CartesianPoint newCoordinate;
        double angleWithSurface;
        Vector normal = Vector.E_Y;

        int particlesCounter = 0;
        int tenPercentOfParticlesAmount = flux.getParticles().size() / 10;

        setShieldingDistance(((ChargedParticle) flux.getParticles().get(0)).getChargeNumber());

        for (Iterator<? extends Particle> iterator = flux.getParticles().iterator(); iterator.hasNext(); particlesCounter++) {
            if (particlesCounter % tenPercentOfParticlesAmount == 0.0) {
                Logger.processedParticlesPercent(particlesCounter * 10 / tenPercentOfParticlesAmount);
            }

            particle = (ChargedParticle) iterator.next();
            angleWithSurface = particle.getSpeed().getAngle(normal) - PI / 2.0;

            if (angleWithSurface <= getCriticalAngle(particle)) {
                newCoordinate = particle.getCoordinate();

                while (isPointInside(newCoordinate)) {
                    particle
                            .setCoordinate(newCoordinate)
                            .setSpeed(rotateParticleSpeed(particle));
                    newCoordinate = newCoordinate.shift(particle.getSpeed());
                }
            } else {
                particle.absorb();
                break;
            }

            particle.setChanneled();
        }

        Logger.interactionFinish();

        return detector.detect(flux);
    }

    @Override
    protected Vector getAxis(CartesianPoint point) {
        return Vector.E_X;
    }

    @Override
    protected double getCriticalAngle(final ChargedParticle particle) {
        // CriticalAngle = ( (2PI N d` Z1 Z2 e^2 a) / E ) ^ (1/2)
        // Nd` - среднее число атомов на единицу площади
        // d` - расстояние между соседними плоскостями
        // n - концентрация на плоскости = Nd`
        // N - среднее число атомов в единице объема
        // Z1, Z2 - заряды частиц (1, 26)
        // e - заряд электрона, 1.60217662 × 10 ^ -19 Кулона
        // a - расстояние экранировки (0.885  * а боровский ((Z1)^(1/2) + (Z2)^(1/2)) ^ -(2/3)), а боровский = 0.529 ангстрем
        // E - энергия налетающей частицы (10 КэВ - 1 МэВ)

//        double temp = Math.toRadians(1.0);
        return Math.sqrt((2 * PI * particle.getChargeNumber() * chargeNumber *
                (ELECTRON_CHARGE * ELECTRON_CHARGE) * shieldingDistance * chargePlanarDensity) / particle.getEnergy());
    }

    @Override
    protected Vector rotateParticleSpeed(final ChargedParticle particle) {
        double y = particle.getCoordinate().getY() - front.getY();

        double Fy = 2.0 * PI *  particle.getChargeNumber() * chargeNumber * (ELECTRON_CHARGE * ELECTRON_CHARGE)
                * chargePlanarDensity * (1.0 - y / Math.sqrt((y / shieldingDistance) * (y / shieldingDistance) + C_SQUARE));
        double dVy = (Fy / particle.getMass()) * TIME_STEP;

//        Vector temp = Vector.set(particle.getSpeed().getX(), particle.getSpeed().getY() + 0.009, particle.getSpeed().getZ());
        return Vector.set(particle.getSpeed().getX(), particle.getSpeed().getY() + dVy, particle.getSpeed().getZ());
    }

    private boolean isPointInside(final CartesianPoint point) {
        return point.getX() <= front.getX() + size;
    }
}

//    @Override
//    protected void createAtoms() {
//        atoms = new ArrayList<>();
//
//        double x = front.getX();
//        double y = front.getY();
//        double z = front.getZ() - size / 2.0;
//
//        while (x <= front.getX() + size) {
//            while (z <= front.getZ() + size / 2.0) {
//                atoms.add(atomFactory.getNewAtom(new CartesianPoint(x, y, z), chargeNumber));
//                z += period;
//            }
//            x += period;
//            z = front.getZ() - size / 2;
//        }
//    }

//    @Override
//    protected double getPotential(final ChargedParticle particle) {
//        double y = (particle.getCoordinate().getY() - front.getY()) / shieldingDistance;
//        double C2 = 3.0;
//
//        return 2.0 * PI * particle.getChargeNumber() * chargeNumber *
//                (ELECTRON_CHARGE * ELECTRON_CHARGE) * shieldingDistance * chargePlanarDensity *
//                Math.sqrt(y * y + C2) - y);
//    }