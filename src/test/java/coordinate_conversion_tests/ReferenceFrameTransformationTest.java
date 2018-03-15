package coordinate_conversion_tests;

import com.private_void.core.geometry.CartesianPoint;

import com.private_void.core.geometry.SphericalPoint;
import com.private_void.core.geometry.Vector;
import com.private_void.core.particles.NeutralParticle;
import com.private_void.core.particles.Particle;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.SmoothCylinder;
import com.private_void.utils.Utils;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class ReferenceFrameTransformationTest {

    @Test
    public void toInnerAndBack() {
        Particle particleExample = NeutralParticle.getFactory(1.0)
                .getNewParticle(
                        new CartesianPoint(0.0, -123.0, 20.0),
                        new Vector(0.94, -0.1, -0.5));

        Particle particle = NeutralParticle.getFactory(1.0)
                .getNewParticle(
                        new CartesianPoint(0.0, -123.0, 20.0),
                        new Vector(0.94, -0.1, -0.5));

        Capillar capillar = SmoothCylinder.getFactory(
                        10.0,
                        100.0,
                        0.0,
                        0.0,
                        1.0,
                        90.0)
                .getNewCapillar(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        new SphericalPoint(500.0, Math.toRadians(1.0), Math.toRadians(1.0)));

        // To inner reference frame
        double directionCoefficient = -1.0;

        particle.getCoordinate()
                .shift(0.0,
                        Math.sin(-directionCoefficient * capillar.getPosition().getTheta()),
                        Math.sin(-directionCoefficient * capillar.getPosition().getPhi()));

        particle.getSpeed()
                .turnAroundVector(directionCoefficient * capillar.getPosition().getTheta(), new Vector(0.0, 0.0, 1.0))
                .turnAroundVector(directionCoefficient * capillar.getPosition().getPhi(), new Vector(0.0, 1.0, 0.0));
        //---

        // To global reference frame
        directionCoefficient = 1.0;

        particle.getCoordinate()
                .shift(0.0,
                        Math.sin(-directionCoefficient * capillar.getPosition().getTheta()),
                        Math.sin(-directionCoefficient * capillar.getPosition().getPhi()));

        particle.getSpeed()
                .turnAroundVector(directionCoefficient * capillar.getPosition().getTheta(), new Vector(0.0, 0.0, 1.0))
                .turnAroundVector(directionCoefficient * capillar.getPosition().getPhi(), new Vector(0.0, 1.0, 0.0));
        //---

        assertTrue(Utils.compareToZero(particleExample.getCoordinate().getX() - particle.getCoordinate().getX()));
        assertTrue(Utils.compareToZero(particleExample.getCoordinate().getY() - particle.getCoordinate().getY()));
        assertTrue(Utils.compareToZero(particleExample.getCoordinate().getZ() - particle.getCoordinate().getZ()));

        assertTrue(Utils.compareToZero(particleExample.getSpeed().getX() - particle.getSpeed().getX()));
        assertTrue(Utils.compareToZero(particleExample.getSpeed().getY() - particle.getSpeed().getY()));
        assertTrue(Utils.compareToZero(particleExample.getSpeed().getZ() - particle.getSpeed().getZ()));
    }
}