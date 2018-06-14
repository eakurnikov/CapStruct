package com.private_void.core.entities.surfaces.atomic_surfaces.atomic_capillars;

import com.private_void.core.entities.surfaces.Capillar;
import com.private_void.core.entities.surfaces.capillar_factories.RotatedTorusFactory;
import com.private_void.core.entities.surfaces.capillar_factories.StraightCapillarFactory;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.reference_frames.ReferenceFrame;
import com.private_void.core.math.geometry.space_3D.vectors.Vector;
import com.private_void.core.math.utils.Utils;

public class AtomicTorus extends AtomicCapillar {
    private final double curvRadius;
    private final double curvAngleR;

    public AtomicTorus(final CartesianPoint front, final ReferenceFrame refFrame, double radius, double curvRadius,
                       double curvAngleR, double period, double chargeNumber) {
        super(front, refFrame, radius, Utils.getTorusLength(curvRadius, curvAngleR), period, chargeNumber);
        this.curvRadius = curvRadius;
        this.curvAngleR = curvAngleR;
    }

    public AtomicTorus(double length, final CartesianPoint front, final ReferenceFrame refFrame, double radius,
                       double curvAngleR, double period, double chargeNumber) {
        super(front, refFrame, radius, length, period, chargeNumber);
        this.curvRadius = Utils.getTorusCurvRadius(length, curvAngleR);
        this.curvAngleR = curvAngleR;
    }

    @Override
    protected Vector getAxis(final CartesianPoint point) {
        return Vector.E_X.rotateAroundOY(getPointsAngle(point));
    }

    @Override
    protected CartesianPoint getAcceleration(final CartesianPoint coordinate, double particleChargeNumber, double mass) {
        double currentCurvAngle = getPointsAngle(coordinate);

        CartesianPoint currentCrossSectionCenter = new CartesianPoint(
                curvRadius * Math.sin(currentCurvAngle),
                0.0,
                -curvRadius * (1.0 - Math.cos(currentCurvAngle)));

        ReferenceFrame.Converter coordinateConverter = new ReferenceFrame.Converter(
                ReferenceFrame.builder()
//                        .atPoint(currentCrossSectionCenter)
                        .setAngleAroundOY(currentCurvAngle)
                        .build());

        CartesianPoint particleCoordinateInCurrentRefFrame = coordinateConverter.convert(coordinate);

        double y = particleCoordinateInCurrentRefFrame.getY();
        double z = particleCoordinateInCurrentRefFrame.getZ();
        double r = Math.sqrt(y * y + z * z);

        double F = getForce(r, particleChargeNumber);
        double Fy = F * (y / r);
        double Fz = F * (z / r);

//        ReferenceFrame.Converter forceConverter = new ReferenceFrame.Converter(
//                ReferenceFrame.builder()
//                        .setAngleAroundOY(currentCurvAngle)
//                        .build());
//
//        return forceConverter.convertBack(new CartesianPoint(0.0, Fy / mass, Fz / mass));

        return new CartesianPoint(0.0, Fy / mass, Fz / mass);

//        return new double[] {Fy / mass, Fz / mass};
    }

    @Override
    protected boolean isPointInside(final CartesianPoint point) {
        return point.getX() <= length;
    }

    public static StraightCapillarFactory getCapillarFactory(double radius, double curvRadius, double curvAngleR,
                                                             double period, double chargeNumber) {
        return new StraightCapillarFactory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate) {
                return new AtomicTorus(coordinate, ReferenceFrame.builder().atPoint(coordinate).build(), radius,
                        curvRadius, curvAngleR, period, chargeNumber);
            }

            @Override
            public double getRadius() {
                return radius;
            }

            @Override
            public double getLength() {
                return Utils.getTorusLength(curvRadius, curvAngleR);
            }
        };
    }

    public static RotatedTorusFactory getRotatedCapillarFactory(double radius, double length, double period,
                                                                double chargeNumber) {
        return new RotatedTorusFactory() {

            @Override
            public Capillar getNewCapillar(final CartesianPoint coordinate, double curvAngleR,
                                           final ReferenceFrame refFrame) {
                return new AtomicTorus(length, coordinate, refFrame, radius, curvAngleR, period, chargeNumber);
            }

            @Override
            public double getRadius() {
                return radius;
            }

            @Override
            public double getLength() {
                return length;
            }
        };
    }

    private double getPointsAngle(final CartesianPoint point) {
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ() + curvRadius;

        return Math.asin(x / Math.sqrt(x * x + y * y + z * z));
    }
}