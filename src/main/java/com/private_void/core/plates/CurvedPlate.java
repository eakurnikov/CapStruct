package com.private_void.core.plates;

import com.private_void.core.geometry.CoordinateFactory;
import com.private_void.core.geometry.Point3D;
import com.private_void.core.surfaces.CapillarFactory;

public class CurvedPlate extends Plate {

    public CurvedPlate(final CapillarFactory capillarFactory, final Point3D center, int capillarsAmount,
                       float capillarsDensity) {
        super(capillarFactory, center, capillarsAmount, capillarsDensity);
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
    protected boolean isCapillarCoordinateValid(Point3D[] coordinates, Point3D coordinate) {
        return false;
    }
}