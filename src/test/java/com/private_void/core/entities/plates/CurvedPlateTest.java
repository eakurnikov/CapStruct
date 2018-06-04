package com.private_void.core.entities.plates;

import com.private_void.core.math.geometry.space_3D.coordinates.Point3D;
import com.private_void.core.math.geometry.space_3D.coordinates.SphericalPoint;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CurvedPlateTest {

    @Test
    public void isCapillarCoordinateValid() {
        double capillarRadius = 7.0;
        double curvRadius = 1_000.0;
        double pointRadius = Math.atan(capillarRadius / curvRadius);

        Point3D[] coordinates = new Point3D[5];
        coordinates[0] = new SphericalPoint(curvRadius, 0.0, 0.0); //false
        coordinates[1] = new SphericalPoint(curvRadius, pointRadius, 0.0); //false
        coordinates[2] = new SphericalPoint(curvRadius, 2.0 * pointRadius, 0.0); //true
        coordinates[3] = new SphericalPoint(curvRadius, 2.0 * pointRadius, 2.0 * pointRadius); //true
        coordinates[4] = new SphericalPoint(curvRadius, Math.PI, Math.PI); //true

        Point3D coordinate = new SphericalPoint(curvRadius, 0.0, 0.0);

        boolean[] result = {false, false, true, true, true};

        int i = 0;
        while (i < coordinates.length && coordinates[i] != null) {
            boolean isPointValid = true;
            if ((coordinate.getQ2() - coordinates[i].getQ2()) * (coordinate.getQ2() - coordinates[i].getQ2())
                    + (coordinate.getQ3() - coordinates[i].getQ3()) * (coordinate.getQ3() - coordinates[i].getQ3())
                    < 4.0 * pointRadius * pointRadius) {
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