package com.private_void.core.plates;

import com.private_void.core.geometry.CoordinateFactory;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.surfaces.CapillarFactory;

public class CurvedPlate extends Plate {

    public CurvedPlate(final CapillarFactory capillarFactory, final CoordinateFactory coordinateFactory,
                       final Point3D center, float length, float height, float capillarsDensity) {
        super(capillarFactory, coordinateFactory, center, length, height, capillarsDensity);
    }

    @Override
    protected void createCapillars() {
        //TODO проверяю, что при заданной плотности и радиусе капилляров, они не залазят друг на друга
        //TODO генерю количесвто капилляров, равное плотности на площадь поверхности пластинки
        //TODO генерирую 3 пространственные координаты рандомно
    }

    @Override
    protected Point3D getDetectorsCoordinate() {
        return null;
    }

    @Override
    protected float getFrontSquare() {
        return 0;
    }
}
