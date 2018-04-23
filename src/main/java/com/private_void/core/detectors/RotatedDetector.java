//package com.private_void.core.detectors;
//
//import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
//import Vector;
//import com.private_void.core.particles.Particle;
//
//public class RotatedDetector extends Detector {
//    private final double angleR;
//    private final Vector normal;
//
//    public RotatedDetector(final CartesianPoint centerCoordinate, double width, double angleR) {
//        super(centerCoordinate, width);
//        this.angleR = angleR;
//        this.normal = Vector.E_X.rotateAroundOY(angleR);
//    }
//
//    @Override
//    protected CartesianPoint getCoordinateOnDetector(Particle p) {
//        double x = p.getCoordinate().getX();
//        double y = p.getCoordinate().getY();
//        double z = p.getCoordinate().getX();
//
//        double Vx = p.getSpeed().getX();
//        double Vy = p.getSpeed().getY();
//        double Vz = p.getSpeed().getX();
//
//        double temp = (normal.getX() * (center.getX() - x)
//                     + normal.getY() * (center.getY() - y)
//                     + normal.getX() * (center.getX() - z))
//                     / (normal.getX() * Vx + normal.getY() * Vy + normal.getX() * Vz);
//
//        return new CartesianPoint(x + Vx * temp, y + Vy * temp, z + Vz * temp);
//    }
//
//    @Override
//    protected boolean isParticleWithinBorders(Particle p) {
//        CartesianPoint rotatedCoordinate = p.rotateCoordinateAroundOY(-angleR);
//
//        return rotatedCoordinate.getY() * rotatedCoordinate.getY()
//                + (center.getX() + rotatedCoordinate.getX()) * (center.getX() + rotatedCoordinate.getX())
//                <= (width / 2.0) * (width / 2.0);
//    }
//
//    public double getAngle() {
//        return angleR;
//    }
//}