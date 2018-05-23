package com.private_void.core.surfaces;

import com.private_void.core.detection.Distribution;
import com.private_void.core.fluxes.Flux;

public interface CapillarSystem {
    Distribution interact(Flux flux);
}
