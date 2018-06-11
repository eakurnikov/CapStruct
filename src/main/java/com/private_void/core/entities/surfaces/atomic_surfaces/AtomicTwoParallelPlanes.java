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

public class AtomicTwoParallelPlanes extends AtomicSurface implements CapillarSystem {
    private final double size;
    private final double chargePlanarDensity;
    private final double width;
    private final Detector detector;

    public AtomicTwoParallelPlanes(final CartesianPoint front, double period, double width, double chargeNumber, double size) {
        super(front, period, chargeNumber);
        this.size = size;
        this.chargePlanarDensity = 1 / (period * period);
        this.width = width;
        this.detector = new Detector(new CartesianPoint(front.getX() + size, front.getY(), front.getZ()), size);
    }

    @Override
    public Distribution interact(Flux flux) {
        Logger.info(MessagePool.interactionStart());

        ChargedParticle particle;
        CartesianPoint newCoordinate;
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

        Logger.info(MessagePool.interactionFinish());

        return detector.detect(flux);
    }

    @Override
    protected Vector getAxis(CartesianPoint point) {
        return Vector.E_X;
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
    protected Vector rotateParticleSpeed(final ChargedParticle particle) {
        double y1 = particle.getCoordinate().getY() - front.getY();
        double y2 = front.getY() + width - particle.getCoordinate().getY();

        double Fy1 = 2.0 * PI *  particle.getChargeNumber() * chargeNumber * (ELECTRON_CHARGE * ELECTRON_CHARGE)
                * chargePlanarDensity * (1.0 - y1 / Math.sqrt((y1 / shieldingDistance) * (y1 / shieldingDistance) + C_SQUARE));

        double Fy2 = 2.0 * PI *  particle.getChargeNumber() * chargeNumber * (ELECTRON_CHARGE * ELECTRON_CHARGE)
                * chargePlanarDensity * (1.0 - y2 / Math.sqrt((y2 / shieldingDistance) * (y2 / shieldingDistance) + C_SQUARE));

        double dVy = ((Fy1 - Fy2) / particle.getMass()) * TIME_STEP;

        return Vector.set(particle.getSpeed().getX(), particle.getSpeed().getY() + dVy, particle.getSpeed().getZ());
    }

    private boolean isPointInside(final CartesianPoint point) {
        return point.getX() <= front.getX() + size;
    }
}