package com.private_void.core.particles;

import static junit.framework.TestCase.assertTrue;

public class ParticleTest {

//    @Test
//    public void turningTest() {
//        Particle.Factory factory = NeutralParticle.getFactory(1.0);
//
//        Particle p1 = factory.getNewParticle(
//                        new CartesianPoint(0.0, 0.0, 0.0),
//                        Vector.set(0.98765, 0.1234, 0.5));
//
//        Particle p2 = factory.getNewParticle(
//                new CartesianPoint(0.0, 0.0, 0.0),
//                Vector.set(0.98765, 0.1234, 0.5));
//
//        Vector axis = Vector.E_Y;
//
//        for (int i = 0; i < 1000; i++) {
//            p1.getSpeed().rotateAroundVector(axis, Math.toRadians(30.0));
//            p2.setSpeed(p2.getSpeed().rotateAroundVector(axis, Math.toRadians(30.0)));
//        }
//
//        assertTrue(p1.getSpeed().getX() - p2.getSpeed().getX() == 0.0);
//        assertTrue(p1.getSpeed().getY() - p2.getSpeed().getY() == 0.0);
//        assertTrue(p1.getSpeed().getX() - p2.getSpeed().getX() == 0.0);
//    }
}