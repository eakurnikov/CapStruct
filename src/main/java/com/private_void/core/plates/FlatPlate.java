package com.private_void.core.plates;

import com.private_void.core.geometry.Point3D;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.CapillarFactory;

import java.util.LinkedList;
import java.util.List;

import static com.private_void.utils.Generator.generator;

public class FlatPlate extends Plate {
    public FlatPlate(final CapillarFactory capillarFactory, final Point3D center, float length, float width, float height,
                     float capillarsDensity, float capillarRadius) {
        super(capillarFactory, center, length, width, height, capillarsDensity, capillarRadius);
    }

    @Override
    protected void createCapillars() {
        List<Capillar> newCapillars = new LinkedList<>();
        float frontSqure = getFrontSquare();
        float capillarsAmount = capillarsDensity * frontSqure;

        for (int i = 0; i < capillarsAmount; i++) {
            Point3D coordinate = new Point3D(center.getX(),
                    generator().uniformFloat(center.getY() - height / 2, center.getY() + height / 2),
                    generator().uniformFloat(center.getZ() - length / 2, center.getZ() + length / 2));
            newCapillars.add(capillarFactory.getNewCapillar(coordinate));
        }

        capillars = newCapillars;
    }

    @Override
    protected Point3D getDetectorsCoordinate() {
        return new Point3D(center.getX() + width, center.getY(), center.getZ());
    }

    @Override
    protected float getFrontSquare() {
        return length * height;
    }
}
