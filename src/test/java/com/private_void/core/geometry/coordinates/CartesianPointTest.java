package com.private_void.core.geometry.coordinates;

import com.private_void.utils.Utils;
import org.junit.Test;

import static java.lang.Math.PI;
import static org.junit.Assert.*;

public class CartesianPointTest {

    @Test
    public void testOY() {
        SphericalPoint c = new CartesianPoint(0.0, 100.0, 0.0).convertToSpherical();
        SphericalPoint s = new SphericalPoint(100.0, PI / 2.0, PI / 2.0);

        assertTrue(Utils.compareToZero(s.getRadius() - c.getRadius()) ||
                Utils.compareToZero(s.getRadius() - c.getRadius() + 2.0 * PI) ||
                Utils.compareToZero(s.getRadius() - c.getRadius() - 2.0 * PI));

        assertTrue(Utils.compareToZero(s.getTheta() - c.getTheta()) ||
                Utils.compareToZero(s.getTheta() - c.getTheta() + 2.0 * PI) ||
                Utils.compareToZero(s.getTheta() - c.getTheta() - 2.0 * PI));

        assertTrue(Utils.compareToZero(s.getPhi() - c.getPhi()) ||
                Utils.compareToZero(s.getPhi() - c.getPhi() + 2.0 * PI) ||
                Utils.compareToZero(s.getPhi() - c.getPhi() - 2.0 * PI));
    }

    @Test
    public void testOX() {
        SphericalPoint c = new CartesianPoint(100.0, 0.0, 0.0).convertToSpherical();
        SphericalPoint s = new SphericalPoint(100.0, PI / 2.0, 0.0);

        assertTrue(Utils.compareToZero(s.getRadius() - c.getRadius()) ||
                Utils.compareToZero(s.getRadius() - c.getRadius() + 2.0 * PI) ||
                Utils.compareToZero(s.getRadius() - c.getRadius() - 2.0 * PI));

        assertTrue(Utils.compareToZero(s.getTheta() - c.getTheta()) ||
                Utils.compareToZero(s.getTheta() - c.getTheta() + 2.0 * PI) ||
                Utils.compareToZero(s.getTheta() - c.getTheta() - 2.0 * PI));

        assertTrue(Utils.compareToZero(s.getPhi() - c.getPhi()) ||
                Utils.compareToZero(s.getPhi() - c.getPhi() + 2.0 * PI) ||
                Utils.compareToZero(s.getPhi() - c.getPhi() - 2.0 * PI));
    }

    @Test
    public void testOZ() {
        SphericalPoint c = new CartesianPoint(0.0, 0.0, 100.0).convertToSpherical();
        SphericalPoint s = new SphericalPoint(100.0, 0.0, 0.0);

        assertTrue(Utils.compareToZero(s.getRadius() - c.getRadius()) ||
                Utils.compareToZero(s.getRadius() - c.getRadius() + 2.0 * PI) ||
                Utils.compareToZero(s.getRadius() - c.getRadius() - 2.0 * PI));

        assertTrue(Utils.compareToZero(s.getTheta() - c.getTheta()) ||
                Utils.compareToZero(s.getTheta() - c.getTheta() + 2.0 * PI) ||
                Utils.compareToZero(s.getTheta() - c.getTheta() - 2.0 * PI));

        assertTrue(Utils.compareToZero(s.getPhi() - c.getPhi()) ||
                Utils.compareToZero(s.getPhi() - c.getPhi() + 2.0 * PI) ||
                Utils.compareToZero(s.getPhi() - c.getPhi() - 2.0 * PI));
    }

    @Test
    public void testPlaneVector1() {
        SphericalPoint c = new CartesianPoint(70.71067811865474, 0.0, 70.71067811865474).convertToSpherical();
        SphericalPoint s = new SphericalPoint(100.0, PI / 4.0, 0.0);

        assertTrue(Utils.compareToZero(s.getRadius() - c.getRadius()) ||
                Utils.compareToZero(s.getRadius() - c.getRadius() + 2.0 * PI) ||
                Utils.compareToZero(s.getRadius() - c.getRadius() - 2.0 * PI));

        assertTrue(Utils.compareToZero(s.getTheta() - c.getTheta()) ||
                Utils.compareToZero(s.getTheta() - c.getTheta() + 2.0 * PI) ||
                Utils.compareToZero(s.getTheta() - c.getTheta() - 2.0 * PI));

        assertTrue(Utils.compareToZero(s.getPhi() - c.getPhi()) ||
                Utils.compareToZero(s.getPhi() - c.getPhi() + 2.0 * PI) ||
                Utils.compareToZero(s.getPhi() - c.getPhi() - 2.0 * PI));
    }

    @Test
    public void testPlaneVector2() {
        SphericalPoint c = new CartesianPoint(70.71067811865474, 0.0, -70.71067811865474).convertToSpherical();
        SphericalPoint s = new SphericalPoint(100.0, 3.0 * PI / 4.0, 0.0);

        assertTrue(Utils.compareToZero(s.getRadius() - c.getRadius()) ||
                Utils.compareToZero(s.getRadius() - c.getRadius() + 2.0 * PI) ||
                Utils.compareToZero(s.getRadius() - c.getRadius() - 2.0 * PI));

        assertTrue(Utils.compareToZero(s.getTheta() - c.getTheta()) ||
                Utils.compareToZero(s.getTheta() - c.getTheta() + 2.0 * PI) ||
                Utils.compareToZero(s.getTheta() - c.getTheta() - 2.0 * PI));

        assertTrue(Utils.compareToZero(s.getPhi() - c.getPhi()) ||
                Utils.compareToZero(s.getPhi() - c.getPhi() + 2.0 * PI) ||
                Utils.compareToZero(s.getPhi() - c.getPhi() - 2.0 * PI));
    }

    @Test
    public void testPlaneVector3() {
        SphericalPoint c = new CartesianPoint(70.71067811865474, 70.71067811865474, 0.0).convertToSpherical();
        SphericalPoint s = new SphericalPoint(100.0, PI / 2.0, PI / 4.0);

        assertTrue(Utils.compareToZero(s.getRadius() - c.getRadius()) ||
                Utils.compareToZero(s.getRadius() - c.getRadius() + 2.0 * PI) ||
                Utils.compareToZero(s.getRadius() - c.getRadius() - 2.0 * PI));

        assertTrue(Utils.compareToZero(s.getTheta() - c.getTheta()) ||
                Utils.compareToZero(s.getTheta() - c.getTheta() + 2.0 * PI) ||
                Utils.compareToZero(s.getTheta() - c.getTheta() - 2.0 * PI));

        assertTrue(Utils.compareToZero(s.getPhi() - c.getPhi()) ||
                Utils.compareToZero(s.getPhi() - c.getPhi() + 2.0 * PI) ||
                Utils.compareToZero(s.getPhi() - c.getPhi() - 2.0 * PI));
    }
}