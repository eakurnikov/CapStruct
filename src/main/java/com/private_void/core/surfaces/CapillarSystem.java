package com.private_void.core.surfaces;

import com.private_void.core.detectors.Detector;
import com.private_void.core.fluxes.Flux;

public interface CapillarSystem {

    Flux interact(Flux flux);

    Detector getDetector();
}
