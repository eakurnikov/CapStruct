package com.private_void.core.geometry;

import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.geometry.coordinates.SphericalPoint;
import com.private_void.utils.Utils;
import org.junit.Test;

import static com.private_void.utils.Constants.PI;
import static org.junit.Assert.*;

public class SphericalPointTest {

    @Test
    public void convertToCartesianTest() {
        // Theta = 0, Phi = 0
        CartesianPoint s = new SphericalPoint(100.0, 0.0, 0.0).convertToCartesian();
        CartesianPoint c = new CartesianPoint(0.0, 100.0, 0.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));

        // Theta = 0, Phi = PI/2
        s = new SphericalPoint(100.0, 0.0, PI / 2.0).convertToCartesian();
        c = new CartesianPoint(0.0, 100.0, 0.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));

        // Theta = 0, Phi = PI
        s = new SphericalPoint(100.0, 0.0, PI).convertToCartesian();
        c = new CartesianPoint(0.0, 100.0, 0.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));

        // Theta = 0, Phi = 3PI/2
        s = new SphericalPoint(100.0, 0.0, 3.0 * PI / 2.0).convertToCartesian();
        c = new CartesianPoint(0.0, 100.0, 0.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));

        /// Theta = 0, Phi = 2PI
            s = new SphericalPoint(100.0, 0.0, 2.0 * PI).convertToCartesian();
            c = new CartesianPoint(0.0, 100.0, 0.0);

            assertTrue(Utils.compareToZero(s.getX() - c.getX()));
            assertTrue(Utils.compareToZero(s.getY() - c.getY()));
            assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));
        //--------------------------------------------------------------------------------------------------------------

        // Theta = PI/2, Phi = 0
        s = new SphericalPoint(100.0, PI / 2.0, 0.0).convertToCartesian();
        c = new CartesianPoint(100.0, 0.0, 0.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));

        // Theta = PI/2, Phi = PI/2
        s = new SphericalPoint(100.0, PI / 2.0, PI / 2.0).convertToCartesian();
        c = new CartesianPoint(0.0, 0.0, 100.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));

        // Theta = PI/2, Phi = PI
        s = new SphericalPoint(100.0, PI / 2.0, PI).convertToCartesian();
        c = new CartesianPoint(-100.0, 0.0, 0.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));

        // Theta = PI/2, Phi = 3PI/2
        s = new SphericalPoint(100.0, PI / 2.0, 3.0 * PI / 2.0).convertToCartesian();
        c = new CartesianPoint(0.0, 0.0, -100.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));

        // Theta = PI/2, Phi = 2PI
        s = new SphericalPoint(100.0, PI / 2.0, 2.0 * PI).convertToCartesian();
        c = new CartesianPoint(100.0, 0.0, 0.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));
        //--------------------------------------------------------------------------------------------------------------

        // Theta = PI, Phi = 0
        s = new SphericalPoint(100.0, PI, 0.0).convertToCartesian();
        c = new CartesianPoint(0.0, -100.0, 0.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));

        // Theta = PI, Phi = PI/2
        s = new SphericalPoint(100.0, PI, PI / 2.0).convertToCartesian();
        c = new CartesianPoint(0.0, -100.0, 0.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));

        // Theta = PI, Phi = PI
        s = new SphericalPoint(100.0, PI, PI).convertToCartesian();
        c = new CartesianPoint(0.0, -100.0, 0.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));

        // Theta = PI, Phi = 3PI/2
        s = new SphericalPoint(100.0, PI, 3.0 * PI / 2.0).convertToCartesian();
        c = new CartesianPoint(0.0, -100.0, 0.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));


        // Theta = PI, Phi = 2PI
        s = new SphericalPoint(100.0, PI, 2.0 * PI).convertToCartesian();
        c = new CartesianPoint(0.0, -100.0, 0.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));
        //--------------------------------------------------------------------------------------------------------------
    }

    @Test
    public void shiftTest() {
        // Shift to R = 200, Theta = 0, Phi = 0
        SphericalPoint b = new SphericalPoint(200.0, 0.0, 0.0);
        SphericalPoint s = new SphericalPoint(100.0, 0.0, 0.0);
        s = s.shift(100.0, 0.0, 0.0);

        assertTrue(Utils.compareToZero(s.getRadius() - b.getRadius()));
        assertTrue(Utils.compareToZero(s.getTheta() - b.getTheta()));
        assertTrue(Utils.compareToZero(s.getPhi() - b.getPhi()));

        //Shift to R = 100, Theta = PI, Phi = 0
        b = new SphericalPoint(100.0, PI, 0.0);
        s = new SphericalPoint(100.0, 0.0, 0.0);
        s = s.shift(0.0, PI, 0.0);

        assertTrue(Utils.compareToZero(s.getRadius() - b.getRadius()));
        assertTrue(Utils.compareToZero(s.getTheta() - b.getTheta()));
        assertTrue(Utils.compareToZero(s.getPhi() - b.getPhi()));

        //Shift to R = 100, Theta = 0, Phi = PI
        b = new SphericalPoint(100.0, 0.0, PI);
        s = new SphericalPoint(100.0, 0.0, 0.0)
                .shift(0.0, 0.0, PI);

        assertTrue(Utils.compareToZero(s.getRadius() - b.getRadius()));
        assertTrue(Utils.compareToZero(s.getTheta() - b.getTheta()));
        assertTrue(Utils.compareToZero(s.getPhi() - b.getPhi()));

        //Shift to R = 200, Theta = PI, Phi = PI
        b = new SphericalPoint(200.0, PI, PI);
        s = new SphericalPoint(100.0, 0.0, 0.0)
                .shift(100.0, PI, PI);

        assertTrue(Utils.compareToZero(s.getRadius() - b.getRadius()));
        assertTrue(Utils.compareToZero(s.getTheta() - b.getTheta()));
        assertTrue(Utils.compareToZero(s.getPhi() - b.getPhi()));
    }
}