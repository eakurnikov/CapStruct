package com.private_void.core.entities.surfaces;

import com.private_void.core.entities.detectors.Distribution;
import com.private_void.core.entities.fluxes.Flux;

public interface CapillarSystem {
    Distribution interact(Flux flux);
}
