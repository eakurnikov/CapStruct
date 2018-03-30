package com.private_void.core.particles;

import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.Vector;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class ParticleTest {

    @Test
    public void turningTest() {
        ParticleFactory factory = NeutralParticle.getFactory(1.0);

        Particle p1 = factory.getNewParticle(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        new Vector(0.98765, 0.1234, 0.5));

        Particle p2 = factory.getNewParticle(
                new CartesianPoint(0.0, 0.0, 0.0),
                new Vector(0.98765, 0.1234, 0.5));

        Vector axis = new Vector(0.0, 1.0, 0.0);

        for (int i = 0; i < 1000; i++) {
            p1.getSpeed().turnAroundVector(Math.toRadians(30.0), axis);
            p2.setSpeed(p2.getSpeed().getNewByTurningAroundVector(Math.toRadians(30.0), axis));
        }

        assertTrue(p1.getSpeed().getX() - p2.getSpeed().getX() == 0.0);
        assertTrue(p1.getSpeed().getY() - p2.getSpeed().getY() == 0.0);
        assertTrue(p1.getSpeed().getZ() - p2.getSpeed().getZ() == 0.0);
    }
}