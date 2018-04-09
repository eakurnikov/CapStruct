package com.private_void.core.surfaces.capillar_factories;

import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.coordinates.ReferenceFrame;
import com.private_void.core.surfaces.Capillar;

public interface RotatedCapillarFactory {

    Capillar getNewCapillar(final CartesianPoint coordinate, final ReferenceFrame refFrame);

    double getRadius();

    double getLength();
}