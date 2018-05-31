//package com.private_void.utils.newthons_method;
//
//import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
//import com.private_void.core.geometry.space_3D.vectors.Vector;
//import com.private_void.core.particles.NeutralParticle;
//import com.private_void.core.particles.Particle;
//
//import static com.private_void.utils.Generator.generator;
//
////В этом классе будет храниться количество рекурсивных итераций
//public class NewthonsMethod {
//    private static final double ACCURACY = 0.05;
//
//    private double[] solution;
//    private double[] delta = {1.0, 1.0, 1.0};
//    private double[] F  = new double[3];
//    private double[][] W = new double[3][3];
//
//    private int iterationsAmount = 0;
//
//    private double dr = generator().uniformDouble(0.0, roughnessSize);
//    private double r = radius - dr;
//    private double y, z;
//
//    private NewthonsMethod(double[] initialApproximation) {
//        this.solution = initialApproximation;
//    }
//
//    private static class CylinderEquation extends NewthonsMethod {
//        private final int recursiveIterationCounter;
//        private final CartesianPoint initialPoint;
//        final Vector speed;
//        private final CartesianPoint surfaceCenter;
//        private final double radius;
//        private final double roughnessSize;
//
//        private CylinderEquation(final CartesianPoint initialPoint, final Vector speed,
//                                 final CartesianPoint surfaceCenter, double radius, double roughnessSize,
//                                 int recursiveIterationCounter) {
//            super(new double[]{
//                    initialPoint.getX() + speed.getX() * radius * recursiveIterationCounter,
//                    initialPoint.getY() + speed.getY() * radius * recursiveIterationCounter,
//                    initialPoint.getZ() + speed.getZ() * radius * recursiveIterationCounter
//            });
//
//            this.initialPoint = initialPoint;
//            this.speed = speed;
//            this.surfaceCenter = surfaceCenter;
//            this.radius = radius;
//            this.roughnessSize = roughnessSize;
//            this.recursiveIterationCounter = recursiveIterationCounter;
//        }
//
//        public CartesianPoint getSolution() {
//            return getSolution(1);
//        }
//
//        private CartesianPoint getSolution(int recursiveIterationCounter) {
//            return null;
//        }
//    }
//
//    public static CartesianPoint cylinder(final CartesianPoint initialPoint, final Vector speed,
//                                          final CartesianPoint surfaceCenter, double radius, double roughnessSize) {
//        return new CylinderEquation(initialPoint, speed, surfaceCenter, radius, roughnessSize, 1)
//                .getSolution();
//    }
//
//    public static CartesianPoint cylinder(final Particle particle, final CartesianPoint center, double radius,
//                                          double roughnessSize) {
//        return new NewthonsMethod(capillar, particle);
//    }
//
//    public static CartesianPoint cone(final Particle particle, final CartesianPoint center, double radius,
//                                      double roughnessSize) {
//        return new NewthonsMethod(capillar, particle);
//    }
//
//    public static CartesianPoint torus(final Particle particle, final CartesianPoint center, double radius,
//                                       double curvRadius) {
//        return new NewthonsMethod(capillar, particle);
//    }
//
//    public static CartesianPoint torus(final Particle particle, final CartesianPoint center, double radius,
//                                       double curvRadius, double roughnessSize) {
//        return new NewthonsMethod(capillar, particle);
//    }
//}
