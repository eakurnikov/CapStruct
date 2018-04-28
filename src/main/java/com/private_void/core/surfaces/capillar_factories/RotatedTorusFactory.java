package com.private_void.core.surfaces.capillar_factories;

import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.reference_frames.ReferenceFrame;
import com.private_void.core.surfaces.Capillar;

public interface RotatedTorusFactory  {

    Capillar getNewCapillar(final CartesianPoint coordinate, double curvAngleR, final ReferenceFrame refFrame);

    double getRadius();

    double getLength();
}