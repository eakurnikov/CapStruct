package com.private_void.core.entities.surfaces.capillar_factories;

import com.private_void.core.entities.surfaces.Capillar;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.reference_frames.ReferenceFrame;

public interface RotatedCylinderFactory {

    Capillar getNewCapillar(final CartesianPoint coordinate, final ReferenceFrame refFrame);

    double getRadius();

    double getLength();
}