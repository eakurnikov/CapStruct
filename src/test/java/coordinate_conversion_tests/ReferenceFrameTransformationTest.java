//package coordinate_conversion_tests;
//
//import com.private_void.core.geometry.coordinates.CartesianPoint;
//
//import com.private_void.core.geometry.coordinates.SphericalPoint;
//import com.private_void.core.geometry.vectors.Vector;
//import com.private_void.core.particles.NeutralParticle;
//import com.private_void.core.particles.Particle;
//import com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.rotated_smooth_capillars.RotatedCapillar;
//import com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.rotated_smooth_capillars.RotatedSmoothCylinder;
//import com.private_void.utils.Utils;
//import org.junit.Test;
//
//import static junit.framework.TestCase.assertTrue;
//
//public class ReferenceFrameTransformationTest {
//
//    @Test
//    public void toInnerTestTheta2D() {
//        double angleTheta = Math.toRadians(30.0);
//
//        Particle base = NeutralParticle
//                .getFactory(1.0)
//                .getNewParticle(
//                        new CartesianPoint(0.0, 0.0, 50.0),
//                        Vector.set(Math.sqrt(3.0) / 2.0, 0.0, -0.5));
//
//        Particle particle = NeutralParticle
//                .getFactory(1.0)
//                .getNewParticle(CartesianPoint.ORIGIN, Vector.E_X);
//
//        RotatedCapillar capillar = RotatedSmoothCylinder.getFactory(
//                10.0,
//                100.0,
//                0.0,
//                0.0,
//                1.0,
//                90.0)
//                .getNewCapillar(CartesianPoint.ORIGIN, new SphericalPoint(100.0, angleTheta, 0.0));
//
//        // To inner reference frame
////        SphericalPoint position = capillar.getPosition();
//
//        capillar.toInnerRefFrame(particle);
////        particle
////                .shiftCoordinate(
////                        0.0,
////                        position.getRadius() * Math.sin(position.getTheta()),
////                        position.getRadius() * Math.sin(position.getPhi()))
////                .rotateSpeed(Vector.E_Z, -position.getTheta())
////                .rotateSpeed(Vector.E_Y, position.getPhi());
//
//        assertTrue(Utils.compareToZero(base.getCoordinate().getX() - particle.getCoordinate().getX()));
//        assertTrue(Utils.compareToZero(base.getCoordinate().getY() - particle.getCoordinate().getY()));
//        assertTrue(Utils.compareToZero(base.getCoordinate().getZ() - particle.getCoordinate().getZ()));
//
//        assertTrue(Utils.compareToZero(base.getSpeed().getX() - particle.getSpeed().getX()));
//        assertTrue(Utils.compareToZero(base.getSpeed().getY() - particle.getSpeed().getY()));
//        assertTrue(Utils.compareToZero(base.getSpeed().getZ() - particle.getSpeed().getZ()));
//    }
//
//    @Test
//    public void toInnerTestPhi2D() {
//        double anglePhi = Math.toRadians(30.0);
//
//        Particle base = NeutralParticle
//                .getFactory(1.0)
//                .getNewParticle(
//                        new CartesianPoint(0.0, 50.0, 0.0),
//                        Vector.set(Math.sqrt(3.0) / 2.0, -0.5, 0.0));
//
//        Particle particle = NeutralParticle
//                .getFactory(1.0)
//                .getNewParticle(CartesianPoint.ORIGIN, Vector.E_X);
//
//        RotatedCapillar capillar = RotatedSmoothCylinder.getFactory(
//                10.0,
//                100.0,
//                0.0,
//                0.0,
//                1.0,
//                90.0)
//                .getNewCapillar(CartesianPoint.ORIGIN, new SphericalPoint(100.0, 0.0, anglePhi));
//
//        // To inner reference frame
////        SphericalPoint position = capillar.getPosition();
//
//        capillar.toInnerRefFrame(particle);
////        particle
////                .shiftCoordinate(
////                        0.0,
////                        position.getRadius() * Math.sin(position.getTheta()),
////                        position.getRadius() * Math.sin(position.getPhi()))
////                .rotateSpeed(Vector.E_Z, -position.getTheta())
////                .rotateSpeed(Vector.E_Y, position.getPhi());
//
//        assertTrue(Utils.compareToZero(base.getCoordinate().getX() - particle.getCoordinate().getX()));
//        assertTrue(Utils.compareToZero(base.getCoordinate().getY() - particle.getCoordinate().getY()));
//        assertTrue(Utils.compareToZero(base.getCoordinate().getZ() - particle.getCoordinate().getZ()));
//
//        assertTrue(Utils.compareToZero(base.getSpeed().getX() - particle.getSpeed().getX()));
//        assertTrue(Utils.compareToZero(base.getSpeed().getY() - particle.getSpeed().getY()));
//        assertTrue(Utils.compareToZero(base.getSpeed().getZ() - particle.getSpeed().getZ()));
//    }
//
////    @Test
////    public void toGlobalTestPhi2D() {
////        double anglePhi = Math.toRadians(30.0);
////
////        Particle base = NeutralParticle
////                .getFactory(1.0)
////                .getNewParticle(CartesianPoint.ORIGIN, Vector.E_X);
////
////        Particle particle = NeutralParticle
////                .getFactory(1.0)
////                .getNewParticle(
////                        new CartesianPoint(0.0, 0.0, 50.0),
////                        Vector.set(Math.sqrt(3.0) / 2.0, 0.0, -0.5));
////
////        Capillar capillar = SmoothCylinder.getFactory(
////                10.0,
////                100.0,
////                0.0,
////                0.0,
////                1.0,
////                90.0)
////                .getNewCapillar(CartesianPoint.ORIGIN, new SphericalPoint(100.0, 0.0, anglePhi));
////
////        // To global reference frame
////        double directionCoefficient = 1.0;
////
////        SphericalPoint position = capillar.getPosition();
////
////        particle
////                .shiftCoordinate(
////                        0.0,
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getTheta()),
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getPhi()))
////                .rotateSpeed(Vector.E_Z, directionCoefficient * position.getTheta())
////                .rotateSpeed(Vector.E_Y, -directionCoefficient * position.getPhi());
////
////        assertTrue(Utils.compareToZero(base.getCoordinate().getX() - particle.getCoordinate().getX()));
////        assertTrue(Utils.compareToZero(base.getCoordinate().getY() - particle.getCoordinate().getY()));
////        assertTrue(Utils.compareToZero(base.getCoordinate().getZ() - particle.getCoordinate().getZ()));
////
////        assertTrue(Utils.compareToZero(base.getSpeed().getX() - particle.getSpeed().getX()));
////        assertTrue(Utils.compareToZero(base.getSpeed().getY() - particle.getSpeed().getY()));
////        assertTrue(Utils.compareToZero(base.getSpeed().getZ() - particle.getSpeed().getZ()));
////    }
////
////    @Test
////    public void toInnerAndBackPhi2D() {
////        double anglePhi = Math.toRadians(30.0);
////
////        Particle base = NeutralParticle
////                .getFactory(1.0)
////                .getNewParticle(CartesianPoint.ORIGIN, Vector.E_X);
////
////        Particle particle = NeutralParticle
////                .getFactory(1.0)
////                .getNewParticle(CartesianPoint.ORIGIN, Vector.E_X);
////
////        Capillar capillar = SmoothCylinder.getFactory(
////                10.0,
////                100.0,
////                0.0,
////                0.0,
////                1.0,
////                90.0)
////                .getNewCapillar(CartesianPoint.ORIGIN, new SphericalPoint(100.0, 0.0, anglePhi));
////
////        // To inner reference frame
////        double directionCoefficient = -1.0;
////
////        SphericalPoint position = capillar.getPosition();
////
////        particle
////                .shiftCoordinate(
////                        0.0,
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getTheta()),
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getPhi()))
////                .rotateSpeed(Vector.E_Z, directionCoefficient * position.getTheta())
////                .rotateSpeed(Vector.E_Y, -directionCoefficient * position.getPhi());
////        //---
////
////        // To global reference frame
////        directionCoefficient = 1.0;
////
////        position = capillar.getPosition();
////
////        particle
////                .shiftCoordinate(
////                        0.0,
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getTheta()),
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getPhi()))
////                .rotateSpeed(Vector.E_Z, directionCoefficient * position.getTheta())
////                .rotateSpeed(Vector.E_Y, -directionCoefficient * position.getPhi());
////        //---
////
////        assertTrue(Utils.compareToZero(base.getCoordinate().getX() - particle.getCoordinate().getX()));
////        assertTrue(Utils.compareToZero(base.getCoordinate().getY() - particle.getCoordinate().getY()));
////        assertTrue(Utils.compareToZero(base.getCoordinate().getZ() - particle.getCoordinate().getZ()));
////
////        assertTrue(Utils.compareToZero(base.getSpeed().getX() - particle.getSpeed().getX()));
////        assertTrue(Utils.compareToZero(base.getSpeed().getY() - particle.getSpeed().getY()));
////        assertTrue(Utils.compareToZero(base.getSpeed().getZ() - particle.getSpeed().getZ()));
////    }
////
////    @Test
////    public void toInnerTestTheta2D() {
////        double angleTheta = Math.toRadians(30.0);
////
////        Particle base = NeutralParticle
////                .getFactory(1.0)
////                .getNewParticle(
////                        new CartesianPoint(0.0, 50.0, 0.0),
////                        Vector.set(Math.sqrt(3.0) / 2.0, -0.5, 0.0));
////
////        Particle particle = NeutralParticle
////                .getFactory(1.0)
////                .getNewParticle(CartesianPoint.ORIGIN, Vector.E_X);
////
////        Capillar capillar = SmoothCylinder.getFactory(
////                10.0,
////                100.0,
////                0.0,
////                0.0,
////                1.0,
////                90.0)
////                .getNewCapillar(CartesianPoint.ORIGIN, new SphericalPoint(100.0, angleTheta, 0.0));
////
////        // To inner reference frame
////        double directionCoefficient = -1.0;
////
////        SphericalPoint position = capillar.getPosition();
////
////        particle
////                .shiftCoordinate(
////                        0.0,
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getTheta()),
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getPhi()))
////                .rotateSpeed(Vector.E_Z, directionCoefficient * position.getTheta())
////                .rotateSpeed(Vector.E_Y, -directionCoefficient * position.getPhi());
////
////        assertTrue(Utils.compareToZero(base.getCoordinate().getX() - particle.getCoordinate().getX()));
////        assertTrue(Utils.compareToZero(base.getCoordinate().getY() - particle.getCoordinate().getY()));
////        assertTrue(Utils.compareToZero(base.getCoordinate().getZ() - particle.getCoordinate().getZ()));
////
////        assertTrue(Utils.compareToZero(base.getSpeed().getX() - particle.getSpeed().getX()));
////        assertTrue(Utils.compareToZero(base.getSpeed().getY() - particle.getSpeed().getY()));
////        assertTrue(Utils.compareToZero(base.getSpeed().getZ() - particle.getSpeed().getZ()));
////    }
////
////    @Test
////    public void toGlobalTestTheta2D() {
////        double angleTheta = Math.toRadians(30.0);
////
////        Particle base = NeutralParticle
////                .getFactory(1.0)
////                .getNewParticle(CartesianPoint.ORIGIN, Vector.E_X);
////
////        Particle particle = NeutralParticle.getFactory(1.0)
////                .getNewParticle(
////                        new CartesianPoint(0.0, 50.0, 0.0),
////                        Vector.set(Math.sqrt(3.0) / 2.0, -0.5, 0.0));
////
////        Capillar capillar = SmoothCylinder.getFactory(
////                10.0,
////                100.0,
////                0.0,
////                0.0,
////                1.0,
////                90.0)
////                .getNewCapillar(CartesianPoint.ORIGIN, new SphericalPoint(100.0, angleTheta, 0.0));
////
////        // To global reference frame
////        double directionCoefficient = 1.0;
////
////        SphericalPoint position = capillar.getPosition();
////
////        particle
////                .shiftCoordinate(
////                        0.0,
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getTheta()),
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getPhi()))
////                .rotateSpeed(Vector.E_Z, directionCoefficient * position.getTheta())
////                .rotateSpeed(Vector.E_Y, -directionCoefficient * position.getPhi());
////
////        assertTrue(Utils.compareToZero(base.getCoordinate().getX() - particle.getCoordinate().getX()));
////        assertTrue(Utils.compareToZero(base.getCoordinate().getY() - particle.getCoordinate().getY()));
////        assertTrue(Utils.compareToZero(base.getCoordinate().getZ() - particle.getCoordinate().getZ()));
////
////        assertTrue(Utils.compareToZero(base.getSpeed().getX() - particle.getSpeed().getX()));
////        assertTrue(Utils.compareToZero(base.getSpeed().getY() - particle.getSpeed().getY()));
////        assertTrue(Utils.compareToZero(base.getSpeed().getZ() - particle.getSpeed().getZ()));
////    }
////
////    @Test
////    public void toInnerAndBackTestTheta2D() {
////        double angleTheta = Math.toRadians(30.0);
////
////        Particle base = NeutralParticle
////                .getFactory(1.0)
////                .getNewParticle(CartesianPoint.ORIGIN, Vector.E_X);
////
////        Particle particle = NeutralParticle
////                .getFactory(1.0)
////                .getNewParticle(CartesianPoint.ORIGIN, Vector.E_X);
////
////        Capillar capillar = SmoothCylinder.getFactory(
////                10.0,
////                100.0,
////                0.0,
////                0.0,
////                1.0,
////                90.0)
////                .getNewCapillar(CartesianPoint.ORIGIN, new SphericalPoint(100.0, angleTheta, 0.0));
////
////        // To inner reference frame
////        double directionCoefficient = -1.0;
////
////        SphericalPoint position = capillar.getPosition();
////
////        particle
////                .shiftCoordinate(
////                        0.0,
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getTheta()),
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getPhi()))
////                .rotateSpeed(Vector.E_Z, directionCoefficient * position.getTheta())
////                .rotateSpeed(Vector.E_Y, -directionCoefficient * position.getPhi());
////        //---
////
////        // To global reference frame
////        directionCoefficient = 1.0;
////
////        position = capillar.getPosition();
////
////        particle
////                .shiftCoordinate(
////                        0.0,
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getTheta()),
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getPhi()))
////                .rotateSpeed(Vector.E_Z, directionCoefficient * position.getTheta())
////                .rotateSpeed(Vector.E_Y, -directionCoefficient * position.getPhi());
////        //---
////
////        assertTrue(Utils.compareToZero(base.getCoordinate().getX() - particle.getCoordinate().getX()));
////        assertTrue(Utils.compareToZero(base.getCoordinate().getY() - particle.getCoordinate().getY()));
////        assertTrue(Utils.compareToZero(base.getCoordinate().getZ() - particle.getCoordinate().getZ()));
////
////        assertTrue(Utils.compareToZero(base.getSpeed().getX() - particle.getSpeed().getX()));
////        assertTrue(Utils.compareToZero(base.getSpeed().getY() - particle.getSpeed().getY()));
////        assertTrue(Utils.compareToZero(base.getSpeed().getZ() - particle.getSpeed().getZ()));
////    }
////
////    @Test
////    public void toInnerTest3D() {
//////        double angle = Math.toRadians(30.0);
//////
//////        Particle base = NeutralParticle
//////                .getFactory(1.0)
//////                .getNewParticle(
//////                        new CartesianPoint(0.0, 50.0, 50.0),
//////                        Vector.set(3.0 / Math.sqrt(17.0) / 2.0, -2.0 / Math.sqrt(17.0), -2.0 / Math.sqrt(17.0)));
//////
//////        Particle particle = NeutralParticle
//////                .getFactory(1.0)
//////                .getNewParticle(CartesianPoint.ORIGIN, Vector.E_X);
//////
//////        Capillar capillar = SmoothCylinder.getFactory(
//////                10.0,
//////                100.0,
//////                0.0,
//////                0.0,
//////                1.0,
//////                90.0)
//////                .getNewCapillar(CartesianPoint.ORIGIN, new SphericalPoint(100.0, angle, angle));
//////
//////        // To inner reference frame
//////        double directionCoefficient = -1.0;
//////
//////        SphericalPoint position = capillar.getPosition();
//////
//////        particle
//////                .shiftCoordinate(
//////                        0.0,
//////                        position.getRadius() * Math.sin(-directionCoefficient * position.getTheta()),
//////                        position.getRadius() * Math.sin(-directionCoefficient * position.getPhi()))
//////                .rotateSpeed(Vector.E_Y, -directionCoefficient * position.getTheta())
//////                .rotateSpeed(Vector.E_Z, -directionCoefficient * position.getPhi());
//////
//////        assertTrue(Utils.compareToZero(base.getCoordinate().getX() - particle.getCoordinate().getX()));
//////        assertTrue(Utils.compareToZero(base.getCoordinate().getY() - particle.getCoordinate().getY()));
//////        assertTrue(Utils.compareToZero(base.getCoordinate().getZ() - particle.getCoordinate().getZ()));
//////
//////        assertTrue(Utils.compareToZero(base.getSpeed().getX() - particle.getSpeed().getX()));
//////        assertTrue(Utils.compareToZero(base.getSpeed().getY() - particle.getSpeed().getY()));
//////        assertTrue(Utils.compareToZero(base.getSpeed().getZ() - particle.getSpeed().getZ()));
////    }
////
////    @Test
////    public void toGlobalTest3D() {
////        double angle = Math.toRadians(30.0);
////
////        Particle base = NeutralParticle
////                .getFactory(1.0)
////                .getNewParticle(CartesianPoint.ORIGIN, Vector.E_X);
////
////        Particle particle = NeutralParticle.getFactory(1.0)
////                .getNewParticle(
////                        new CartesianPoint(0.0, 0.0, 50.0),
////                        Vector.set(Math.sqrt(3.0) / 2.0, 0.0, -0.5));
////
////        Capillar capillar = SmoothCylinder.getFactory(
////                10.0,
////                100.0,
////                0.0,
////                0.0,
////                1.0,
////                90.0)
////                .getNewCapillar(CartesianPoint.ORIGIN, new SphericalPoint(100.0, 0.0, angle));
////
////        // To global reference frame
////        double directionCoefficient = 1.0;
////
////        SphericalPoint position = capillar.getPosition();
////
////        particle
////                .shiftCoordinate(
////                        0.0,
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getTheta()),
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getPhi()))
////                .rotateSpeed(Vector.E_Z, -directionCoefficient * position.getTheta())
////                .rotateSpeed(Vector.E_Y, -directionCoefficient * position.getPhi());
////
////        assertTrue(Utils.compareToZero(base.getCoordinate().getX() - particle.getCoordinate().getX()));
////        assertTrue(Utils.compareToZero(base.getCoordinate().getY() - particle.getCoordinate().getY()));
////        assertTrue(Utils.compareToZero(base.getCoordinate().getZ() - particle.getCoordinate().getZ()));
////
////        assertTrue(Utils.compareToZero(base.getSpeed().getX() - particle.getSpeed().getX()));
////        assertTrue(Utils.compareToZero(base.getSpeed().getY() - particle.getSpeed().getY()));
////        assertTrue(Utils.compareToZero(base.getSpeed().getZ() - particle.getSpeed().getZ()));
////    }
////
////    @Test
////    public void toInnerAndBackSameAngle3D() {
////        double angle = Math.toRadians(45.0);
////
////        Particle base = NeutralParticle
////                .getFactory(1.0)
////                .getNewParticle(CartesianPoint.ORIGIN, Vector.E_X);
////
////        Particle particle = NeutralParticle
////                .getFactory(1.0)
////                .getNewParticle(CartesianPoint.ORIGIN, Vector.E_X);
////
////        Capillar capillar = SmoothCylinder.getFactory(
////                10.0,
////                100.0,
////                0.0,
////                0.0,
////                1.0,
////                90.0)
////                .getNewCapillar(CartesianPoint.ORIGIN, new SphericalPoint(100.0, angle, angle));
////
////        // To inner reference frame
////        double directionCoefficient = -1.0;
////
////        SphericalPoint position = capillar.getPosition();
////
////        particle
////                .shiftCoordinate(
////                        0.0,
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getTheta()),
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getPhi()))
////                .rotateSpeed(Vector.E_Z, directionCoefficient * position.getTheta())
////                .rotateSpeed(Vector.E_Y, -directionCoefficient * position.getPhi());
////        //---
////
////        // To global reference frame
////        directionCoefficient = 1.0;
////
////        position = capillar.getPosition();
////
////        particle
////                .shiftCoordinate(
////                        0.0,
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getTheta()),
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getPhi()))
////                .rotateSpeed(Vector.E_Y, -directionCoefficient * position.getPhi())
////                .rotateSpeed(Vector.E_Z, directionCoefficient * position.getTheta());
////        //---
////
////        assertTrue(Utils.compareToZero(base.getCoordinate().getX() - particle.getCoordinate().getX()));
////        assertTrue(Utils.compareToZero(base.getCoordinate().getY() - particle.getCoordinate().getY()));
////        assertTrue(Utils.compareToZero(base.getCoordinate().getZ() - particle.getCoordinate().getZ()));
////
////        assertTrue(Utils.compareToZero(base.getSpeed().getX() - particle.getSpeed().getX()));
////        assertTrue(Utils.compareToZero(base.getSpeed().getY() - particle.getSpeed().getY()));
////        assertTrue(Utils.compareToZero(base.getSpeed().getZ() - particle.getSpeed().getZ()));
////    }
////
////    @Test
////    public void toInnerAndBackDifferentAngles3D() {
////        double angleTheta = Math.toRadians(45.0);
////        double anglePhi = Math.toRadians(30.0);
////
////        Particle base = NeutralParticle
////                .getFactory(1.0)
////                .getNewParticle(CartesianPoint.ORIGIN, Vector.E_X);
////
////        Particle particle = NeutralParticle
////                .getFactory(1.0)
////                .getNewParticle(CartesianPoint.ORIGIN, Vector.E_X);
////
////        Capillar capillar = SmoothCylinder.getFactory(
////                10.0,
////                100.0,
////                0.0,
////                0.0,
////                1.0,
////                90.0)
////                .getNewCapillar(CartesianPoint.ORIGIN, new SphericalPoint(100.0, angleTheta, anglePhi));
////
////        // To inner reference frame
////        double directionCoefficient = -1.0;
////
////        SphericalPoint position = capillar.getPosition();
////
////        particle
////                .shiftCoordinate(
////                        0.0,
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getTheta()),
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getPhi()))
////                .rotateSpeed(Vector.E_Z, directionCoefficient * position.getTheta())
////                .rotateSpeed(Vector.E_Y, -directionCoefficient * position.getPhi());
////        //---
////
////        // To global reference frame
////        directionCoefficient = 1.0;
////
////        position = capillar.getPosition();
////
////        particle
////                .shiftCoordinate(
////                        0.0,
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getTheta()),
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getPhi()))
////                .rotateSpeed(Vector.E_Y, -directionCoefficient * position.getPhi())
////                .rotateSpeed(Vector.E_Z, directionCoefficient * position.getTheta());
////        //---
////
////        assertTrue(Utils.compareToZero(base.getCoordinate().getX() - particle.getCoordinate().getX()));
////        assertTrue(Utils.compareToZero(base.getCoordinate().getY() - particle.getCoordinate().getY()));
////        assertTrue(Utils.compareToZero(base.getCoordinate().getZ() - particle.getCoordinate().getZ()));
////
////        assertTrue(Utils.compareToZero(base.getSpeed().getX() - particle.getSpeed().getX()));
////        assertTrue(Utils.compareToZero(base.getSpeed().getY() - particle.getSpeed().getY()));
////        assertTrue(Utils.compareToZero(base.getSpeed().getZ() - particle.getSpeed().getZ()));
////    }
////
////    @Test
////    public void toInnerAndBackOneAxis3D() {
////        double anglePhi = Math.toRadians(45.0);
////
////        Particle base = NeutralParticle
////                .getFactory(1.0)
////                .getNewParticle(CartesianPoint.ORIGIN, Vector.E_X);
////
////        Particle particle = NeutralParticle
////                .getFactory(1.0)
////                .getNewParticle(CartesianPoint.ORIGIN, Vector.E_X);
////
////        Capillar capillar = SmoothCylinder.getFactory(
////                10.0,
////                100.0,
////                0.0,
////                0.0,
////                1.0,
////                90.0)
////                .getNewCapillar(CartesianPoint.ORIGIN, new SphericalPoint(100.0, anglePhi, anglePhi));
////
////        Vector axis = Vector.E_Z;
////
////        // To inner reference frame
////        double directionCoefficient = -1.0;
////
////        SphericalPoint position = capillar.getPosition();
////
////        particle
////                .shiftCoordinate(
////                        0.0,
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getTheta()),
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getPhi()))
////                .rotateSpeed(axis, directionCoefficient * position.getTheta());
//////                .turnAroundVector(-directionCoefficient * position.getPhi(), axis);
////        //---
////
////        // To global reference frame
////        directionCoefficient = 1.0;
////
////        position = capillar.getPosition();
////
////        particle
////                .shiftCoordinate(0.0,
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getTheta()),
////                        position.getRadius() * Math.sin(-directionCoefficient * position.getPhi()))
////                .rotateSpeed(axis, directionCoefficient * position.getTheta());
//////                .turnAroundVector(-directionCoefficient * position.getPhi(), axis);
////        //---
////
////        assertTrue(Utils.compareToZero(base.getCoordinate().getX() - particle.getCoordinate().getX()));
////        assertTrue(Utils.compareToZero(base.getCoordinate().getY() - particle.getCoordinate().getY()));
////        assertTrue(Utils.compareToZero(base.getCoordinate().getZ() - particle.getCoordinate().getZ()));
////
////        assertTrue(Utils.compareToZero(base.getSpeed().getX() - particle.getSpeed().getX()));
////        assertTrue(Utils.compareToZero(base.getSpeed().getY() - particle.getSpeed().getY()));
////        assertTrue(Utils.compareToZero(base.getSpeed().getZ() - particle.getSpeed().getZ()));
////    }
//}