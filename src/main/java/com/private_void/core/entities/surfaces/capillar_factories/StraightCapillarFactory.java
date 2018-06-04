package com.private_void.core.entities.surfaces.capillar_factories;

import com.private_void.core.entities.surfaces.Capillar;
import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;

public interface StraightCapillarFactory {

    Capillar getNewCapillar(final CartesianPoint coordinate);

    double getRadius();

    double getLength();
}