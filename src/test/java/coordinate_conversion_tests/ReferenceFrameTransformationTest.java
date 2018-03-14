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
        Particle particleExample = NeutralParticle.getFactory(1.0f)
                .getNewParticle(
                        new CartesianPoint(0.0f, -123.0f, 20.0f),
                        new Vector(0.94f, -0.1f, -0.5f));

        Particle particle = NeutralParticle.getFactory(1.0f)
                .getNewParticle(
                        new CartesianPoint(0.0f, -123.0f, 20.0f),
                        new Vector(0.94f, -0.1f, -0.5f));

        Capillar capillar = SmoothCylinder.getFactory(
                        10.0f,
                        100.0f,
                        0.0f,
                        0.0f,
                        1.0f,
                        90.0f)
                .getNewCapillar(
                        new CartesianPoint(0.0f, 0.0f, 0.0f),
                        new SphericalPoint(500.0f, (float) Math.toRadians(1.0f), (float) Math.toRadians(1.0f)));

        // To inner reference frame
        float directionCoefficient = -1.0f;

        particle.getCoordinate()
                .shift(0.0f,
                        (float) Math.sin(-directionCoefficient * capillar.getPosition().getTheta()),
                        (float) Math.sin(-directionCoefficient * capillar.getPosition().getPhi()));

        particle.getSpeed()
                .turnAroundVector(directionCoefficient * capillar.getPosition().getTheta(), new Vector(0.0f, 0.0f, 1.0f))
                .turnAroundVector(directionCoefficient * capillar.getPosition().getPhi(), new Vector(0.0f, 1.0f, 0.0f));
        //---

        // To global reference frame
        directionCoefficient = 1.0f;

        particle.getCoordinate()
                .shift(0.0f,
                        (float) Math.sin(-directionCoefficient * capillar.getPosition().getTheta()),
                        (float) Math.sin(-directionCoefficient * capillar.getPosition().getPhi()));

        particle.getSpeed()
                .turnAroundVector(directionCoefficient * capillar.getPosition().getTheta(), new Vector(0.0f, 0.0f, 1.0f))
                .turnAroundVector(directionCoefficient * capillar.getPosition().getPhi(), new Vector(0.0f, 1.0f, 0.0f));
        //---

        assertTrue(Utils.compareToZero(particleExample.getCoordinate().getX() - particle.getCoordinate().getX()));
        assertTrue(Utils.compareToZero(particleExample.getCoordinate().getY() - particle.getCoordinate().getY()));
        assertTrue(Utils.compareToZero(particleExample.getCoordinate().getZ() - particle.getCoordinate().getZ()));

        assertTrue(Utils.compareToZero(particleExample.getSpeed().getX() - particle.getSpeed().getX()));
        assertTrue(Utils.compareToZero(particleExample.getSpeed().getY() - particle.getSpeed().getY()));
        assertTrue(Utils.compareToZero(particleExample.getSpeed().getZ() - particle.getSpeed().getZ()));
    }
}