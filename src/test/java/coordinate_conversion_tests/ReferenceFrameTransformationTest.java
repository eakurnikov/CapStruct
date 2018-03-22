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
    public void toInnerTest() {
        double anglePhi = Math.toRadians(30.0);

        Particle base = NeutralParticle.getFactory(1.0)
                .getNewParticle(
                        new CartesianPoint(0.0, 0.0, 50.0),
                        new Vector(Math.sqrt(3.0) / 2.0, 0.0, -0.5));

        Particle particle = NeutralParticle.getFactory(1.0)
                .getNewParticle(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        new Vector(1.0, 0.0, 0.0));

        Capillar capillar = SmoothCylinder.getFactory(
                10.0,
                100.0,
                0.0,
                0.0,
                1.0,
                90.0)
                .getNewCapillar(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        new SphericalPoint(100.0, 0.0, anglePhi));

        // To inner reference frame
        double directionCoefficient = -1.0;

        SphericalPoint position = capillar.getPosition();

        particle.getCoordinate()
                .shift(0.0,
                        position.getRadius() * Math.sin(-directionCoefficient * position.getTheta()),
                        position.getRadius() * Math.sin(-directionCoefficient * position.getPhi()));

        particle.getSpeed()
                .turnAroundVector(-directionCoefficient * position.getTheta(), new Vector(0.0, 0.0, 1.0))
                .turnAroundVector(-directionCoefficient * position.getPhi(), new Vector(0.0, 1.0, 0.0));

        assertTrue(Utils.compareToZero(base.getCoordinate().getX() - particle.getCoordinate().getX()));
        assertTrue(Utils.compareToZero(base.getCoordinate().getY() - particle.getCoordinate().getY()));
        assertTrue(Utils.compareToZero(base.getCoordinate().getZ() - particle.getCoordinate().getZ()));

        assertTrue(Utils.compareToZero(base.getSpeed().getX() - particle.getSpeed().getX()));
        assertTrue(Utils.compareToZero(base.getSpeed().getY() - particle.getSpeed().getY()));
        assertTrue(Utils.compareToZero(base.getSpeed().getZ() - particle.getSpeed().getZ()));
    }

    @Test
    public void toGlobalTest() {
        double anglePhi = Math.toRadians(30.0);

        Particle base = NeutralParticle.getFactory(1.0)
                .getNewParticle(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        new Vector(1.0, 0.0, 0.0));

        Particle particle = NeutralParticle.getFactory(1.0)
                .getNewParticle(
                        new CartesianPoint(0.0, 0.0, 50.0),
                        new Vector(Math.sqrt(3.0) / 2.0, 0.0, -0.5));

        Capillar capillar = SmoothCylinder.getFactory(
                10.0,
                100.0,
                0.0,
                0.0,
                1.0,
                90.0)
                .getNewCapillar(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        new SphericalPoint(100.0, 0.0, anglePhi));

        // To global reference frame
        double directionCoefficient = 1.0;

        SphericalPoint position = capillar.getPosition();

        particle.getCoordinate()
                .shift(0.0,
                        position.getRadius() * Math.sin(-directionCoefficient * position.getTheta()),
                        position.getRadius() * Math.sin(-directionCoefficient * position.getPhi()));

        particle.getSpeed()
                .turnAroundVector(-directionCoefficient * position.getTheta(), new Vector(0.0, 0.0, 1.0))
                .turnAroundVector(-directionCoefficient * position.getPhi(), new Vector(0.0, 1.0, 0.0));

        assertTrue(Utils.compareToZero(base.getCoordinate().getX() - particle.getCoordinate().getX()));
        assertTrue(Utils.compareToZero(base.getCoordinate().getY() - particle.getCoordinate().getY()));
        assertTrue(Utils.compareToZero(base.getCoordinate().getZ() - particle.getCoordinate().getZ()));

        assertTrue(Utils.compareToZero(base.getSpeed().getX() - particle.getSpeed().getX()));
        assertTrue(Utils.compareToZero(base.getSpeed().getY() - particle.getSpeed().getY()));
        assertTrue(Utils.compareToZero(base.getSpeed().getZ() - particle.getSpeed().getZ()));
    }

    @Test
    public void toInnerAndBack() {
        double anglePhi = Math.toRadians(30.0);

        Particle base = NeutralParticle.getFactory(1.0)
                .getNewParticle(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        new Vector(1.0, 0.0, 0.0));

        Particle particle = NeutralParticle.getFactory(1.0)
                .getNewParticle(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        new Vector(1.0, 0.0, 0.0));

        Capillar capillar = SmoothCylinder.getFactory(
                10.0,
                100.0,
                0.0,
                0.0,
                1.0,
                90.0)
                .getNewCapillar(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        new SphericalPoint(100.0, 0.0, anglePhi));

        // To inner reference frame
        double directionCoefficient = -1.0;

        SphericalPoint position = capillar.getPosition();

        particle.getCoordinate()
                .shift(0.0,
                        position.getRadius() * Math.sin(-directionCoefficient * position.getTheta()),
                        position.getRadius() * Math.sin(-directionCoefficient * position.getPhi()));

        particle.getSpeed()
                .turnAroundVector(-directionCoefficient * position.getTheta(), new Vector(0.0, 0.0, 1.0))
                .turnAroundVector(-directionCoefficient * position.getPhi(), new Vector(0.0, 1.0, 0.0));
        //---

        // To global reference frame
        directionCoefficient = 1.0;

        position = capillar.getPosition();

        particle.getCoordinate()
                .shift(0.0,
                        position.getRadius() * Math.sin(-directionCoefficient * position.getTheta()),
                        position.getRadius() * Math.sin(-directionCoefficient * position.getPhi()));

        particle.getSpeed()
                .turnAroundVector(-directionCoefficient * position.getTheta(), new Vector(0.0, 0.0, 1.0))
                .turnAroundVector(-directionCoefficient * position.getPhi(), new Vector(0.0, 1.0, 0.0));
        //---

        assertTrue(Utils.compareToZero(base.getCoordinate().getX() - particle.getCoordinate().getX()));
        assertTrue(Utils.compareToZero(base.getCoordinate().getY() - particle.getCoordinate().getY()));
        assertTrue(Utils.compareToZero(base.getCoordinate().getZ() - particle.getCoordinate().getZ()));

        assertTrue(Utils.compareToZero(base.getSpeed().getX() - particle.getSpeed().getX()));
        assertTrue(Utils.compareToZero(base.getSpeed().getY() - particle.getSpeed().getY()));
        assertTrue(Utils.compareToZero(base.getSpeed().getZ() - particle.getSpeed().getZ()));
    }
}