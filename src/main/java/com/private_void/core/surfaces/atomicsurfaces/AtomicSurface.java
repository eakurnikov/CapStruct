package com.private_void.core.surfaces.atomicsurfaces;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.surfaces.Surface;

public abstract class AtomicSurface extends Surface {

    public AtomicSurface(Point3D frontCoordinate) {
        super(frontCoordinate);
    }
}
