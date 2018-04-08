package com.private_void.core.plates;

import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.coordinates.Point3D;
import com.private_void.core.geometry.coordinates.SphericalPoint;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class FlatPlateTest {

    @Test
    public void isCapillarCoordinateValid() {
        double capillarRadius = 7.0;

        Point3D[] coordinates = new Point3D[5];
        coordinates[0] = new CartesianPoint(0.0, 0.0, 0.0); //false
        coordinates[1] = new CartesianPoint(0.0, capillarRadius, 0.0); //false
        coordinates[2] = new CartesianPoint(0.0, 2.0 * capillarRadius, 0.0); //true
        coordinates[3] = new CartesianPoint(0.0, 2.0 * capillarRadius, 2.0 * capillarRadius); //true
        coordinates[4] = new CartesianPoint(0.0, capillarRadius + 3.0, capillarRadius + 5.0); //true

        Point3D coordinate = new SphericalPoint(0.0, 0.0, 0.0);

        boolean[] result = {false, false, true, true, true};

        int i = 0;
        while (i < coordinates.length && coordinates[i] != null) {
            boolean isPointValid = true;
            if ((coordinate.getQ2() - coordinates[i].getQ2()) * (coordinate.getQ2() - coordinates[i].getQ2())
                    + (coordinate.getQ3() - coordinates[i].getQ3()) * (coordinate.getQ3() - coordinates[i].getQ3())
                    < 4.0 * capillarRadius * capillarRadius) {
                isPointValid = false;
            }
            result[i] = isPointValid;
            i++;
        }

        assertTrue(!result[0]);
        assertTrue(!result[1]);
        assertTrue(result[2]);
        assertTrue(result[3]);
        assertTrue(result[4]);
    }
}