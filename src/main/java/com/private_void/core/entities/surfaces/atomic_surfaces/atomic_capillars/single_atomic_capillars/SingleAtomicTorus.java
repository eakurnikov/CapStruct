package com.private_void.core.entities.surfaces.atomic_surfaces.atomic_capillars.single_atomic_capillars;

import com.private_void.core.entities.detectors.Detector;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.reference_frames.ReferenceFrame;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;
import com.private_void.core.math.utils.Utils;

public class SingleAtomicTorus extends SingleAtomicCapillar {
    private final double curvRadius;
    private final double curvAngleR;

    public SingleAtomicTorus(final CartesianPoint front, double radius, double curvRadius, double curvAngleR,
                             double period,  double chargeNumber) {
        super(front, radius, Utils.getTorusLength(curvRadius, curvAngleR), period, chargeNumber);
        this.curvRadius = curvRadius;
        this.curvAngleR = curvAngleR;
        this.detector = createDetector();
    }

    @Override
    protected Vector getAxis(final CartesianPoint point) {
        return Vector.E_X.rotateAroundOY(getPointsAngle(point));
    }

    @Override
    protected CartesianPoint getAcceleration(final CartesianPoint coordinate, double particleChargeNumber, double mass) {
        double currentCurvAngle = getPointsAngle(coordinate);

        CartesianPoint currentCrossSectionCenter = new CartesianPoint(
                front.getX() + curvRadius * Math.sin(currentCurvAngle),
                front.getY(),
                front.getZ() + curvRadius * (1 - Math.cos(currentCurvAngle)));

        ReferenceFrame.Converter converter = new ReferenceFrame.Converter(
                ReferenceFrame.builder()
//                        .atPoint(currentCrossSectionCenter)
                        .setAngleAroundOY(currentCurvAngle)
                        .build());

        CartesianPoint particleCoordinateInCurrentRefFrame = converter.convert(coordinate);

        double y = particleCoordinateInCurrentRefFrame.getY();
        double z = particleCoordinateInCurrentRefFrame.getZ();
        double r = Math.sqrt(y * y + z * z);

        double F = getForce(r, particleChargeNumber);
        double Fy = F * (y / r);
        double Fz = F * (z / r);

//        ReferenceFrame.Converter forceConverter = new ReferenceFrame.Converter(
//                ReferenceFrame.builder()
//                        .setAngleAroundOY(-currentCurvAngle)
//                        .build());
//
//        return forceConverter.convert(new CartesianPoint(0.0, Fy / mass, Fz / mass));

        return new CartesianPoint(0.0, Fy / mass, Fz / mass);

//        return new double[] {Fy / mass, Fz / mass};
    }

    @Override
    protected boolean isPointInside(final CartesianPoint point) {
        return point.getX() <= front.getX() + length;
    }

    @Override
    protected Detector createDetector() {
        return new Detector(
                new CartesianPoint(
                        front.getX() + curvRadius * Math.sin(curvAngleR),
                        front.getY(),
                        front.getZ() + curvRadius * (1 - Math.cos(curvAngleR))),
                2.0 * radius);
    }

    private double getPointsAngle(final CartesianPoint point) {
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ() - curvRadius;

        return Math.asin(x / Math.sqrt(x * x + y * y + z * z));
    }
}