package com.private_void.core.surfaces.atomicsurfaces;

import com.private_void.core.detectors.Detector;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.AtomFactory;
import com.private_void.core.particles.ChargedParticle;
import com.private_void.core.particles.Particle;
import com.private_void.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;

import static com.private_void.utils.Constants.ELECTRON_CHARGE;
import static com.private_void.utils.Constants.PI;

public class AtomicPlane extends AtomicSurface {
    private float size;
    private float chargePlanarDensity;

    public AtomicPlane(final AtomFactory atomFactory, final Point3D frontCoordinate, float period, float chargeNumber,
                       float size) {
        super(atomFactory, frontCoordinate, period, chargeNumber);
        this.size = size;
        this.chargePlanarDensity = 1 / (period * period);
        this.detector = new Detector(new Point3D(frontCoordinate.getX() + size, frontCoordinate.getY(), frontCoordinate.getZ()), size);
        createAtoms();
    }

    @Override
    public void interact(Flux flux) {
        ChargedParticle p;
        Point3D newCoordinate;
        float angleWithSurface;
        Iterator<? extends Particle> iterator = flux.getParticles().iterator();

        setShieldingDistance(((ChargedParticle) flux.getParticles().get(0)).getChargeNumber());

        while (iterator.hasNext()) {
            try {
                p = (ChargedParticle) iterator.next();
                angleWithSurface = p.getSpeed().getAngle(getNormal(p.getCoordinate())) - PI / 2;

                if (angleWithSurface <= getCriticalAngle(p)) {
                    newCoordinate = p.getCoordinate();

                    while (newCoordinate.getX() <= frontCoordinate.getX() + size) {
                        p.setCoordinate(newCoordinate);
                        p.setSpeed(getNewSpeed(p));
                        newCoordinate = getNewCoordinate(p);
                    }
                } else {
                    iterator.remove();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        detector.detect(flux);
    }

    @Override
    protected Vector3D getNormal(final Point3D point) {
        return new Vector3D(0.0f, 1.0f, 0.0f);
    }

    @Override
    protected Vector3D getAxis(final Point3D point) {
        return new Vector3D(0.0f, 0.0f, 1.0f);
    }

    @Override
    protected void createAtoms() {
        atoms = new ArrayList<>();

        float x = frontCoordinate.getX();
        float y = frontCoordinate.getY();
        float z = frontCoordinate.getZ() - size / 2;

        while (x <= frontCoordinate.getX() + size) {
            while (z <= frontCoordinate.getZ() + size / 2) {
                atoms.add(atomFactory.getNewAtom(new Point3D(x, y, z), chargeNumber));
                z += period;
            }
            x += period;
            z = frontCoordinate.getZ() - size / 2;
        }
    }

    @Override
    protected float getCriticalAngle(final ChargedParticle particle) {
        // CriticalAngle = ( (2PI N d` Z1 Z2 e^2 a) / E ) ^ (1/2)
        // Nd` - среднее число атомов на единицу площади
        // d` - расстояние между соседними плоскостями
        // n - концентрация на плоскости = Nd`
        // N - среднее число атомов в единице объема
        // Z1, Z2 - заряды частиц (1, 26)
        // e - заряд электрона, 1.60217662 × 10 ^ -19 Кулона
        // a - расстояние экранировки (0.885  * а боровский ((Z1)^(1/2) + (Z2)^(1/2)) ^ -(2/3)), а боровский = 0.529 ангстрем
        // E - энергия налетающей частицы (10 КэВ - 1 МэВ)

        return Utils.convertDegreesToRadians(45);
//        return (float) Math.sqrt((2 * PI * particle.getChargeNumber() * chargeNumber *
//                (ELECTRON_CHARGE * ELECTRON_CHARGE) * shieldingDistance * chargePlanarDensity) / particle.getEnergy());
    }

    @Override
    protected Vector3D getNewSpeed(final ChargedParticle particle) {
        float y = particle.getCoordinate().getY() - frontCoordinate.getY();
        float C2 = 3.0f;
        float timeInterval = 1.0f;

        float Fy = 2.0f * PI *  particle.getChargeNumber() * chargeNumber * (ELECTRON_CHARGE * ELECTRON_CHARGE)
                * chargePlanarDensity * (float) (1.0f - y / Math.sqrt((y / shieldingDistance) * (y / shieldingDistance) + C2));
        float dVy = (Fy / particle.getMass()) * timeInterval;

        return new Vector3D(particle.getSpeed().getX(), particle.getSpeed().getY() + 0.009f, particle.getSpeed().getZ());
//        return new Vector3D(particle.getSpeed().getX(), particle.getSpeed().getY() + dVy, particle.getSpeed().getZ());
    }

    @Override
    protected Point3D getNewCoordinate(final ChargedParticle p) {
        float x = p.getCoordinate().getX();
        float y = p.getCoordinate().getY();
        float z = p.getCoordinate().getZ();

        float Vx = p.getSpeed().getX();
        float Vy = p.getSpeed().getY();
        float Vz = p.getSpeed().getZ();

        return new Point3D(x + Vx, y + Vy, z + Vz);
    }

//    @Override
//    protected float getPotential(final ChargedParticle particle) {
//        float y = (particle.getCoordinate().getY() - frontCoordinate.getY()) / shieldingDistance;
//        float C2 = 3.0f;
//
//        return 2.0f * PI * particle.getChargeNumber() * chargeNumber *
//                (ELECTRON_CHARGE * ELECTRON_CHARGE) * shieldingDistance * chargePlanarDensity *
//                ((float) Math.sqrt(y * y + C2) - y);
//    }
}