package com.private_void.core.capillars;

import com.private_void.core.detectors.Detector;
import com.private_void.core.fluxes.Flux;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.geometry.Vector3D;
import com.private_void.core.particles.Particle;
import com.private_void.utils.Utils;

import java.util.Iterator;

import static com.private_void.utils.Constants.PI;
import static com.private_void.utils.Generator.generator;

//TODO припилить Stream API или просто распараллелить через ThreadPool

public abstract class Surface {
    protected Detector detector;
    protected Point3D frontCoordinate;
    protected Vector3D normal;
    protected Vector3D axis;
    protected float radius;
    protected float roughnessSize;
    protected float roughnessAngleR;
    protected float reflectivity;
    protected float slideAngleR;
    protected float antiSlideAngleR;

    protected Surface(final Point3D frontCoordinate, float radius, float roughnessSize, float roughnessAngleD, float reflectivity, float slideAngleD) {
        this.frontCoordinate = frontCoordinate;
        this.radius = radius;
        this.roughnessSize = roughnessSize;
        this.roughnessAngleR = Utils.convertDegreesToRads(roughnessAngleD);
        this.reflectivity = reflectivity;
        this.slideAngleR = Utils.convertDegreesToRads(slideAngleD);
        this.antiSlideAngleR = PI / 2 - slideAngleR;

        normal = new Vector3D(0.0f, 1.0f, 0.0f);
        axis = new Vector3D(1.0f, 0.0f, 0.0f);
    }

    public abstract Point3D getHitPoint(final Particle particle);

    public void passThrough(Flux flux) {
        Particle particle;
        Point3D newCoordinate;
        float angleVN;

        Iterator<Particle> iterator = flux.getParticles().iterator();
        while (iterator.hasNext()) {
            particle = iterator.next();

            if (willParticleGetInside(particle)) {
                newCoordinate = getHitPoint(particle);

                while (isPointInside(newCoordinate)) {
                    axis = new Vector3D(1.0f, 0.0f, 0.0f)
                            .turnAroundOY(generator().uniformFloat(0.0f, 2.0f * PI));

                    normal = getNormal(newCoordinate)
                            .turnAroundVector(generator().uniformFloat(0.0f, roughnessAngleR), axis);

                    // TODO: НЕТ. нужно как-то иначе высчитывать ось, она не должна быть туда напарвлена всегда в случае тора
                    axis = normal.getNewByTurningAroundOX(PI / 2);

                    angleVN = particle.getSpeed().getAngle(normal.inverse());

                    if (angleVN >= antiSlideAngleR) {
                        particle.setCoordinate(newCoordinate);
                        particle.setSpeed(particle.getSpeed().getNewByTurningAroundVector(2 * Math.abs(PI / 2 - angleVN), axis));
                        particle.decreaseIntensity(reflectivity);
                        newCoordinate = getHitPoint(particle);
                    } else {
                        particle.setAbsorbed(true);
                        break;
                    }
                }
            } else {
                detector.increaseOutOfCapillarParticlesAmount();
                detector.increaseOutOfCapillarInensity(particle.getIntensity());
                iterator.remove();
            }
        }
        detector.detect(flux);
    }

    protected boolean willParticleGetInside(Particle particle) {
        float x0 = frontCoordinate.getX();

        float x = particle.getCoordinate().getX();
        float y = particle.getCoordinate().getY();
        float z = particle.getCoordinate().getZ();

        float Vx = particle.getSpeed().getX();
        float Vy = particle.getSpeed().getY();
        float Vz = particle.getSpeed().getZ();

        return ((Vy / Vx) * (x0 - x) + y) * ((Vy / Vx) * (x0 - x) + y) +
                ((Vz / Vx) * (x0 - x) + z) * ((Vz / Vx) * (x0 - x) + z) < radius * radius;
    }

    protected abstract boolean isPointInside(Point3D point);

    protected abstract Vector3D getNormal(Point3D point);

    public Detector getDetector() {
        return detector;
    }
}