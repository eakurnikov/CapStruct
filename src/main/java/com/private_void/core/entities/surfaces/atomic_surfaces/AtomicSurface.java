package com.private_void.core.entities.surfaces.atomic_surfaces;

import com.private_void.core.entities.particles.ChargedParticle;
import com.private_void.core.entities.particles.Particle;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;

import static com.private_void.core.constants.Constants.BOHR_RADIUS;
import static com.private_void.core.constants.Constants.TIME_STEP;

public abstract class AtomicSurface {
    protected static final double ACCELERATION_SCALE = 450.0;
    protected static final double ANGLE_SCALE = 200.0;

    protected final CartesianPoint front;
    protected final double period;
    protected final double chargeNumber;
    protected double criticalAngle;
    protected double shieldingDistance;

    public AtomicSurface(final CartesianPoint front, double period, double chargeNumber) {
        this.front = front;
        this.period = period;
        this.chargeNumber = chargeNumber;
    }

    protected void setShieldingDistance(double particleChargeNumber) {
        shieldingDistance = 0.885 * BOHR_RADIUS * Math.pow(Math.sqrt(particleChargeNumber) +
                Math.sqrt(chargeNumber),  2.0 / 3.0);
    }

    protected abstract void setCriticalAngle(final ChargedParticle particle);

    protected abstract Vector getAxis(final CartesianPoint point);

    protected abstract double[] getAcceleration(final CartesianPoint coordinate, double particleChargeNumber, double mass);

//  realization of Euler's method
    protected Particle.State getParticlesNewState(final Particle.State prevState, double particleChargeNumber, double mass) {
        double[] acceleration = getAcceleration(prevState.getCoordinate(), particleChargeNumber, mass);
        Vector nextSpeed = Vector.set(
                prevState.getSpeed().getX(),
                prevState.getSpeed().getY() + acceleration[0] * TIME_STEP,
                prevState.getSpeed().getZ() + acceleration[1] * TIME_STEP);

        return new Particle.State(prevState.getCoordinate().shift(nextSpeed), nextSpeed);
    }

//    realization of modified Euler's method
//    protected Particle.State getParticlesNewState(final Particle.State prevState, double particleChargeNumber,
//                                                  double mass) {
//        double[] currentAcceleration = getAcceleration(prevState.getCoordinate(), particleChargeNumber, mass);
//
//        CartesianPoint tempCoordinate = prevState.getCoordinate().shift(
//                prevState.getSpeed().getX() * STEP / 2.0,
//                prevState.getSpeed().getY() * STEP / 2.0,
//                prevState.getSpeed().getZ() * STEP / 2.0);
//
//        double[] tempAcceleration = getAcceleration(tempCoordinate, particleChargeNumber, mass);
//
//        CartesianPoint nextCoordinate = prevState.getCoordinate().shift(
//                prevState.getSpeed().getX() * STEP,
//                prevState.getSpeed().getY() * STEP + (currentAcceleration[0] * STEP * STEP) / 2.0,
//                prevState.getSpeed().getZ() * STEP + (currentAcceleration[1] * STEP * STEP) / 2.0);
//
//        Vector nextSpeed = Vector.set(
//                prevState.getSpeed().getX(),
//                prevState.getSpeed().getY() + (tempAcceleration[0] * TIME_STEP),
//                prevState.getSpeed().getZ() + (tempAcceleration[1] * TIME_STEP));
//
//        return new Particle.State(nextCoordinate, nextSpeed);
//    }

//    realization of Runge-Kutta's method
//    protected Particle.State getParticlesNewState(final Particle.State prevState, double particleChargeNumber,
//                                                  double mass) {
//        CartesianPoint coordinate = prevState.getCoordinate();
//        Vector speed = prevState.getSpeed();
//
//        double[] a1 = getAcceleration(
//                coordinate,
//                chargeNumber,
//                mass);
//
//        double[] a2 = getAcceleration(
//                coordinate.shift(
//                        speed.getX() * STEP / 2.0,
//                        speed.getY() * STEP / 2.0,
//                        speed.getZ() * STEP / 2.0),
//                chargeNumber,
//                mass);
//
//        double[] a3 = getAcceleration(
//                coordinate.shift(
//                        speed.getX() * STEP / 2.0,
//                        (speed.getY() + a1[0] * STEP / 2.0) * STEP / 2.0,
//                        (speed.getZ() + a1[1] * STEP / 2.0) * STEP / 2.0),
//                chargeNumber,
//                mass);
//
//        double[] a4 = getAcceleration(
//                coordinate.shift(
//                        speed.getX() * STEP,
//                        (speed.getY() + a2[0] / 2.0) * STEP,
//                        (speed.getZ() + a2[1] / 2.0) * STEP),
//                chargeNumber,
//                mass);
//
//        return new Particle.State(
//                coordinate.shift(
//                        speed.getX() * STEP,
//                        speed.getY() * STEP + (a1[0] + a2[0] + a3[0]) * STEP * STEP / 6.0,
//                        speed.getZ() * STEP + (a1[1] + a2[1] + a3[1]) * STEP * STEP / 6.0),
//                Vector.set(
//                        speed.getX(),
//                        speed.getY() + (a1[0] + 2.0 * a2[0] + 2.0 * a3[0] + a4[0]) * STEP / 6.0,
//                        speed.getZ() + (a1[1] + 2.0 * a2[1] + 2.0 * a3[1] + a4[1]) * STEP / 6.0));
//    }
}