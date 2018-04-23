package com.private_void.core.geometry.coordinates;

import com.private_void.utils.Utils;
import org.junit.Test;


import static java.lang.Math.PI;
import static org.junit.Assert.*;

public class SphericalPointTest {

    @Test
    public void convertToCartesianTest2() {
        // Theta = 0, Phi = 0
        CartesianPoint s = new SphericalPoint(100.0, 0.0, 0.0).convertToCartesian();
        CartesianPoint c = new CartesianPoint(0.0, 0.0, 100.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));

        // Theta = 0, Phi = PI/2
        s = new SphericalPoint(100.0, 0.0, PI / 2.0).convertToCartesian();
        c = new CartesianPoint(0.0, 0.0, 100.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));

        // Theta = 0, Phi = PI
        s = new SphericalPoint(100.0, 0.0, PI).convertToCartesian();
        c = new CartesianPoint(0.0, 0.0, 100.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));

        // Theta = 0, Phi = 3PI/2
        s = new SphericalPoint(100.0, 0.0, 3.0 * PI / 2.0).convertToCartesian();
        c = new CartesianPoint(0.0, 0.0, 100.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));

        /// Theta = 0, Phi = 2PI
        s = new SphericalPoint(100.0, 0.0, 2.0 * PI).convertToCartesian();
        c = new CartesianPoint(0.0, 0.0, 100.0);

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
        c = new CartesianPoint(0.0, 100.0, 0.0);

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
        c = new CartesianPoint(0.0, -100.0, 0.0);

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
        c = new CartesianPoint(0.0, 0.0, -100.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));

        // Theta = PI, Phi = PI/2
        s = new SphericalPoint(100.0, PI, PI / 2.0).convertToCartesian();
        c = new CartesianPoint(0.0, 0.0, -100.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));

        // Theta = PI, Phi = PI
        s = new SphericalPoint(100.0, PI, PI).convertToCartesian();
        c = new CartesianPoint(0.0, 0.0, -100.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));

        // Theta = PI, Phi = 3PI/2
        s = new SphericalPoint(100.0, PI, 3.0 * PI / 2.0).convertToCartesian();
        c = new CartesianPoint(0.0, 0.0, -100.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));


        // Theta = PI, Phi = 2PI
        s = new SphericalPoint(100.0, PI, 2.0 * PI).convertToCartesian();
        c = new CartesianPoint(0.0, 0.0, -100.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));
        //--------------------------------------------------------------------------------------------------------------

        // Theta = PI / 4, Phi = 0
        s = new SphericalPoint(100.0, PI / 4.0, 0.0).convertToCartesian();
        c = new CartesianPoint(70.71067811865474, 0.0, 70.71067811865474);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));


        // Theta = PI / 4, Phi = 0
        s = new SphericalPoint(100.0, 3.0 * PI / 4.0, 0.0).convertToCartesian();
        c = new CartesianPoint(70.71067811865474, 0.0, -70.71067811865474);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));

        // Theta = - PI / 4, Phi = 0
        s = new SphericalPoint(100.0, -PI / 4.0, 0.0).convertToCartesian();
        c = new CartesianPoint(-70.71067811865474, 0.0, 70.71067811865474);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));

        // Theta = PI / 2, Phi = PI
        s = new SphericalPoint(100.0, PI / 2.0, PI).convertToCartesian();
        c = new CartesianPoint(-100.0, 0.0, 0.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));

        // Theta = - PI / 2, Phi = 0
        s = new SphericalPoint(100.0, -PI / 2.0, 0.0).convertToCartesian();
        c = new CartesianPoint(-100.0, 0.0, 0.0);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));

//TODO хз че делать с последовательными поворотами. тут все 3 компоненты должны быть равны
//        // Theta = PI / 4, Phi = PI / 4
//        s = new SphericalPoint(100.0, PI / 4.0, PI / 4.0).convertToCartesian();
//        c = new CartesianPoint(-70.71067811865474, 0.0, 70.71067811865474);
//
//        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
//        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
//        assertTrue(Utils.compareToZero(s.getX() - c.getX()));

    }

    @Test
    public void collisionsTest() {
        //Base
        CartesianPoint c = new CartesianPoint(0.0, 0.0, 100.0);

        // Theta = 0, Phi = 0
        CartesianPoint s1 = new SphericalPoint(100.0, 0.0, 0.0).convertToCartesian();

        assertTrue(Utils.compareToZero(s1.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s1.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s1.getZ() - c.getZ()));

        // Theta = 0, Phi = 2PI
        CartesianPoint s2 = new SphericalPoint(100.0, 0.0, 2.0 * PI).convertToCartesian();

        assertTrue(Utils.compareToZero(s2.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s2.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s2.getZ() - c.getZ()));

        // Theta = 0, Phi = 4PI
        CartesianPoint s3 = new SphericalPoint(100.0, 0.0, 4.0 * PI).convertToCartesian();

        assertTrue(Utils.compareToZero(s3.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s3.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s3.getZ() - c.getZ()));

        // Theta = 0, Phi = -2PI
        CartesianPoint s4 = new SphericalPoint(100.0, 0.0, -2.0 * PI).convertToCartesian();

        assertTrue(Utils.compareToZero(s4.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s4.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s4.getZ() - c.getZ()));

        // Theta = 0, Phi = -4PI
        CartesianPoint s5 = new SphericalPoint(100.0, 0.0, -4.0 * PI).convertToCartesian();

        assertTrue(Utils.compareToZero(s5.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s5.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s5.getZ() - c.getZ()));
        //--------------------------------------------------------------------------------------------------------------

        //Base
        c = new CartesianPoint(0.0, 0.0, 100.0);

        // Theta = 0, Phi = 0
        s1 = new SphericalPoint(100.0, 0.0, 0.0).convertToCartesian();

        assertTrue(Utils.compareToZero(s1.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s1.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s1.getZ() - c.getZ()));

        // Theta = 2PI, Phi = 0
        s2 = new SphericalPoint(100.0, 2.0 * PI, 0.0).convertToCartesian();

        assertTrue(Utils.compareToZero(s2.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s2.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s2.getZ() - c.getZ()));

        // Theta = 4PI, Phi = 0
        s3 = new SphericalPoint(100.0, 4.0 * PI, 0.0).convertToCartesian();

        assertTrue(Utils.compareToZero(s3.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s3.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s3.getZ() - c.getZ()));

        // Theta = -2PI, Phi = 0
        s4 = new SphericalPoint(100.0, -2.0 * PI, 0.0).convertToCartesian();

        assertTrue(Utils.compareToZero(s4.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s4.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s4.getZ() - c.getZ()));

        // Theta = -4PI, Phi = 0
        s5 = new SphericalPoint(100.0, -4.0 * PI, 0.0).convertToCartesian();

        assertTrue(Utils.compareToZero(s5.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s5.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s5.getZ() - c.getZ()));
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