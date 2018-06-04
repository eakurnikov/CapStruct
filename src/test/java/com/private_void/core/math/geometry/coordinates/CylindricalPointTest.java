package com.private_void.core.math.geometry.coordinates;

import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import com.private_void.core.math.geometry.space_3D.coordinates.CylindricalPoint;
import com.private_void.core.math.utils.Utils;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CylindricalPointTest {

    @Test
    public void convertToCartesian() {
        CartesianPoint point = new CylindricalPoint(100.0, 0.0, 0.0).convertToCartesian();
        CartesianPoint base = new CartesianPoint(0.0, 0.0, 100.0);

        assertTrue(Utils.compareToZero(point.getX() - base.getX()));
        assertTrue(Utils.compareToZero(point.getY() - base.getY()));
        assertTrue(Utils.compareToZero(point.getZ() - base.getZ()));
        //---

        point = new CylindricalPoint(100.0, Math.PI, 0.0).convertToCartesian();
        base = new CartesianPoint(0.0, 0.0, -100.0);

        assertTrue(Utils.compareToZero(point.getX() - base.getX()));
        assertTrue(Utils.compareToZero(point.getY() - base.getY()));
        assertTrue(Utils.compareToZero(point.getZ() - base.getZ()));
        //---

        point = new CylindricalPoint(100.0, 0.0, 10.0).convertToCartesian();
        base = new CartesianPoint(10.0, 0.0, 100.0);

        assertTrue(Utils.compareToZero(point.getX() - base.getX()));
        assertTrue(Utils.compareToZero(point.getY() - base.getY()));
        assertTrue(Utils.compareToZero(point.getZ() - base.getZ()));
        //---

        point = new CylindricalPoint(100.0, Math.PI, 10.0).convertToCartesian();
        base = new CartesianPoint(10.0, 0.0, -100.0);

        assertTrue(Utils.compareToZero(point.getX() - base.getX()));
        assertTrue(Utils.compareToZero(point.getY() - base.getY()));
        assertTrue(Utils.compareToZero(point.getZ() - base.getZ()));
        //---

        point = new CylindricalPoint(100.0, Math.PI / 2, 10.0).convertToCartesian();
        base = new CartesianPoint(10.0, 100.0, 0.0);

        assertTrue(Utils.compareToZero(point.getX() - base.getX()));
        assertTrue(Utils.compareToZero(point.getY() - base.getY()));
        assertTrue(Utils.compareToZero(point.getZ() - base.getZ()));
        //---
    }
}