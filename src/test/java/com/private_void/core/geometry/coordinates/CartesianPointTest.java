package com.private_void.core.geometry.coordinates;

import com.private_void.core.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.geometry.space_3D.coordinates.CylindricalPoint;
import com.private_void.core.geometry.space_3D.coordinates.SphericalPoint;
import com.private_void.utils.Utils;
import org.junit.Test;

import static java.lang.Math.PI;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void conversionToCylindricalTest() {
        CylindricalPoint base = new CylindricalPoint(100.0, 0.0, 0.0);
        CylindricalPoint point = new CartesianPoint(0.0, 0.0, 100.0).convertToCylindrical();

        assertTrue(Utils.compareToZero(base.getR() - point.getR()) ||
                Utils.compareToZero(base.getPhi() - point.getPhi()) ||
                Utils.compareToZero(base.getZ() - point.getZ()));
        //---

        base = new CylindricalPoint(100.0, 0.0, 10.0);
        point = new CartesianPoint(10.0, 0.0, 100.0).convertToCylindrical();

        assertTrue(Utils.compareToZero(base.getR() - point.getR()) ||
                Utils.compareToZero(base.getPhi() - point.getPhi()) ||
                Utils.compareToZero(base.getZ() - point.getZ()));
        //---

        CartesianPoint base1 = new CylindricalPoint(100.0, PI / 4.0, 0.0).convertToCartesian();
        CartesianPoint point1 = new CartesianPoint(0.0, 70.71067811865474, 70.71067811865474);

        assertTrue(Utils.compareToZero(base1.getX() - point1.getX()) ||
                Utils.compareToZero(base1.getY() - point1.getY()) ||
                Utils.compareToZero(base1.getZ() - point1.getZ()));

        base = new CylindricalPoint(100.0, PI / 4.0, 0.0);
        point = new CartesianPoint(0.0, 70.71067811865474, 70.71067811865474).convertToCylindrical();

        assertTrue(Utils.compareToZero(base.getR() - point.getR()) ||
                Utils.compareToZero(base.getPhi() - point.getPhi()) ||
                Utils.compareToZero(base.getZ() - point.getZ()));
    }
}