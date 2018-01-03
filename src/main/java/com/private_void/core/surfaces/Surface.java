package com.private_void.core.surfaces;

import com.private_void.core.detectors.Detector;
import com.private_void.core.geometry.Point3D;

public abstract class Surface {
    protected Detector detector;
    protected Point3D frontCoordinate;
    protected float criticalAngle;

    protected Surface(final Point3D frontCoordinate) {
        this.frontCoordinate = frontCoordinate;
    }

    public Detector getDetector() {
        return detector;
    }

    protected float getCriticalAngle() {
        return criticalAngle;
    }
}
