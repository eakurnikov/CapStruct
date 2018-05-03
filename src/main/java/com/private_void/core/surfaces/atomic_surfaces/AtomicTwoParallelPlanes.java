package com.private_void.core.surfaces.atomic_surfaces;

import com.private_void.app.Logger;
import com.private_void.core.detectors.Detector;
import com.private_void.core.detectors.Distribution;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.vectors.Vector;
import com.private_void.core.particles.Atom;
import com.private_void.core.particles.ChargedParticle;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.CapillarSystem;

import java.util.ArrayList;
import java.util.Iterator;

import static com.private_void.utils.Constants.ELECTRON_CHARGE;
import static com.private_void.utils.Constants.PI;

public class AtomicTwoParallelPlanes extends AtomicSurface implements CapillarSystem {
    private final double size;
    private final double chargePlanarDensity;
    private final double width;
    private ArrayList<Atom> secondPlaneAtoms;
    private final Detector detector;

    public AtomicTwoParallelPlanes(final Atom.Factory atomFactory, final CartesianPoint front,
                                   double period, double width, double chargeNumber, double size) {
        super(atomFactory, front, period, chargeNumber);
        this.size = size;
        this.chargePlanarDensity = 1 / (period * period);
        this.width = width;
        this.detector = new Detector(new CartesianPoint(front.getX() + size, front.getY(), front.getZ()), size);
        createAtoms();
    }

    @Override
    public Distribution interact(Flux flux) {
        Logger.interactionStart();

        ChargedParticle particle;
        CartesianPoint newCoordinate;
        double angleWithSurface;

        int particlesCounter = 0;
        int tenPercentOfParticlesAmount = flux.getParticles().size() / 10;

        setShieldingDistance(((ChargedParticle) flux.getParticles().get(0)).getChargeNumber());

        for (Iterator<? extends Particle> iterator = flux.getParticles().iterator(); iterator.hasNext(); particlesCounter++) {
            if (particlesCounter % tenPercentOfParticlesAmount == 0.0) {
                Logger.processedParticlesPercent(particlesCounter * 10 / tenPercentOfParticlesAmount);
            }

            particle = (ChargedParticle) iterator.next();
            angleWithSurface = particle.getSpeed().getAngle(getNormal(particle.getCoordinate())) - PI / 2.0;

            if (angleWithSurface <= getCriticalAngle(particle)) {
                newCoordinate = particle.getCoordinate();

                while (newCoordinate.getX() <= front.getX() + size) {
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
    protected Vector getNormal(final CartesianPoint point) {
        return Vector.E_Y;
    }

    @Override
    protected Vector getParticleSpeedRotationAxis(final CartesianPoint point, final Vector normal) {
        return Vector.E_Z;
    }

    @Override
    protected void createAtoms() {
        atoms = new ArrayList<>();
        secondPlaneAtoms = new ArrayList<>();

        double x = front.getX();
        double y = front.getY();
        double z = front.getZ() - size / 2.0;

        while (x <= front.getX() + size) {
            while (z <= front.getZ() + size / 2.0) {
                atoms.add(atomFactory.getNewAtom(new CartesianPoint(x, y, z), chargeNumber));
                secondPlaneAtoms.add(atomFactory.getNewAtom(new CartesianPoint(x, y + width, z), chargeNumber));
                z += period;
            }
            x += period;
            z = front.getZ() - size / 2;
        }
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
        double C2 = 3.0;
        double timeInterval = 1.0;

        double y1 = particle.getCoordinate().getY() - front.getY();
        double Fy1 = 2.0 * PI *  particle.getChargeNumber() * chargeNumber * (ELECTRON_CHARGE * ELECTRON_CHARGE)
                * chargePlanarDensity * (1.0 - y1 / Math.sqrt((y1 / shieldingDistance) * (y1 / shieldingDistance) + C2));

        double y2 = front.getY() + width - particle.getCoordinate().getY();
        double Fy2 = 2.0 * PI *  particle.getChargeNumber() * chargeNumber * (ELECTRON_CHARGE * ELECTRON_CHARGE)
                * chargePlanarDensity * (1.0 - y2 / Math.sqrt((y2 / shieldingDistance) * (y2 / shieldingDistance) + C2));

        double dVy = ((Fy1 - Fy2) / particle.getMass()) * timeInterval;

//        Vector temp = Vector.set(particle.getSpeed().getX(), particle.getSpeed().getY() + 0.009, particle.getSpeed().getZ());
        return Vector.set(particle.getSpeed().getX(), particle.getSpeed().getY() + dVy, particle.getSpeed().getZ());
    }
}