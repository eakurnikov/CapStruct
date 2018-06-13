package com.private_void.core.entities.surfaces.atomic_surfaces;

import com.private_void.app.notifiers.Logger;
import com.private_void.app.notifiers.MessagePool;
import com.private_void.app.notifiers.ProgressProvider;
import com.private_void.core.entities.detectors.Detector;
import com.private_void.core.entities.detectors.Distribution;
import com.private_void.core.entities.fluxes.Flux;
import com.private_void.core.entities.particles.ChargedParticle;
import com.private_void.core.entities.particles.Particle;
import com.private_void.core.entities.surfaces.CapillarSystem;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;

import java.util.Iterator;

import static com.private_void.core.constants.Constants.*;
import static com.private_void.core.entities.particles.AtomicChain.C_SQUARE;

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
        Logger.info(MessagePool.interactionStart());

        ChargedParticle particle;
        Particle.State state;
        double angleWithSurface;
        Vector normal = Vector.E_Y;

        int particlesCounter = 0;
        int tenPercentOfParticlesAmount = flux.getParticles().size() / 10;

        setShieldingDistance(((ChargedParticle) flux.getParticles().get(0)).getChargeNumber());
        setCriticalAngle((ChargedParticle) flux.getParticles().get(0));

        for (Iterator<? extends Particle> iterator = flux.getParticles().iterator(); iterator.hasNext(); particlesCounter++) {
            if (particlesCounter % tenPercentOfParticlesAmount == 0.0) {
                ProgressProvider.getInstance().setProgress(particlesCounter * 10 / tenPercentOfParticlesAmount);
            }

            particle = (ChargedParticle) iterator.next();
            angleWithSurface = particle.getSpeed().getAngle(normal) - PI / 2.0;

            if (angleWithSurface <= criticalAngle) {
                state = new Particle.State(particle.getCoordinate(), particle.getSpeed());

                while (isPointInside(state.getCoordinate())) {
                    particle.setState(state);
                    state = getParticlesNewState(state, particle.getChargeNumber(), particle.getMass());
                }
            } else {
                particle.absorb();
                break;
            }

            particle.setChanneled();
        }

        Logger.info(MessagePool.interactionFinish());

        return detector.detect(flux);
    }

    @Override
    protected void setCriticalAngle(final ChargedParticle particle) {
        // CriticalAngle = ( (2PI N d` Z1 Z2 e^2 a) / E ) ^ (1/2)
        // Nd` - среднее число атомов на единицу площади
        // d` - расстояние между соседними плоскостями
        // n - концентрация на плоскости = Nd`
        // N - среднее число атомов в единице объема
        // Z1, Z2 - заряды частиц (1, 26)
        // e - заряд электрона, 1.60217662 × 10 ^ -19 Кулона
        // a - расстояние экранировки (0.885  * а боровский ((Z1)^(1/2) + (Z2)^(1/2)) ^ -(2/3)), а боровский = 0.529 ангстрем
        // E - энергия налетающей частицы (10 КэВ - 1 МэВ)

        criticalAngle = Math.sqrt((2 * PI * particle.getChargeNumber() * chargeNumber *
                (ELECTRON_CHARGE * ELECTRON_CHARGE) * shieldingDistance * chargePlanarDensity) / particle.getEnergy());
    }

    @Override
    protected Vector getAxis(CartesianPoint point) {
        return Vector.E_X;
    }

    @Override
    protected double[] getAcceleration(final CartesianPoint coordinate, double chargeNumber, double mass) {
        double y = coordinate.getY() - front.getY();

        double Fy = 2.0 * PI *  chargeNumber * chargeNumber * (ELECTRON_CHARGE * ELECTRON_CHARGE)
                * chargePlanarDensity
                * (1.0 - y / Math.sqrt((y / shieldingDistance) * (y / shieldingDistance) + C_SQUARE));

        return new double[] {(Fy / mass) * TIME_STEP};
    }

    @Override
    protected Particle.State getParticlesNewState(final Particle.State prevState, double chargeNumber, double mass) {
        double[] acceleration = getAcceleration(prevState.getCoordinate(), chargeNumber, mass);

        Vector nextSpeed = Vector.set(
                prevState.getSpeed().getX(),
                prevState.getSpeed().getY() + acceleration[0],
                prevState.getSpeed().getZ());

        return new Particle.State(prevState.getCoordinate().shift(nextSpeed), nextSpeed);
    }

    private boolean isPointInside(final CartesianPoint point) {
        return point.getX() <= front.getX() + size;
    }
}