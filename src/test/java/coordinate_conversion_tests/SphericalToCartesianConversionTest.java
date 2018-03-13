package coordinate_conversion_tests;

import static com.private_void.utils.Constants.PI;
import static org.junit.Assert.assertTrue;

import com.private_void.core.geometry.CartesianPoint;
import com.private_void.core.geometry.SphericalPoint;
import com.private_void.utils.Utils;
import org.junit.Test;

public class SphericalToCartesianConversionTest {

    // Theta = 0, varing Phi
    @Test
    public void testTheta0Phi0() {
        CartesianPoint s = new SphericalPoint(100.0f, 0.0f, 0.0f).convertToCartesian();
        CartesianPoint c = new CartesianPoint(0.0f, 100.0f, 0.0f);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));
    }

    @Test
    public void testTheta0PhiPI2() {
        CartesianPoint s = new SphericalPoint(100.0f, 0.0f, PI / 2.0f).convertToCartesian();
        CartesianPoint c = new CartesianPoint(0.0f, 100.0f, 0.0f);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));
    }

    @Test
    public void testTheta0PhiPI() {
        CartesianPoint s = new SphericalPoint(100.0f, 0.0f, PI).convertToCartesian();
        CartesianPoint c = new CartesianPoint(0.0f, 100.0f, 0.0f);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));
    }

    @Test
    public void testTheta0Phi3PI2() {
        CartesianPoint s = new SphericalPoint(100.0f, 0.0f, 3.0f * PI / 2.0f).convertToCartesian();
        CartesianPoint c = new CartesianPoint(0.0f, 100.0f, 0.0f);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));
    }

    @Test
    public void testTheta0Phi2PI() {
        CartesianPoint s = new SphericalPoint(100.0f, 0.0f, 2.0f * PI).convertToCartesian();
        CartesianPoint c = new CartesianPoint(0.0f, 100.0f, 0.0f);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));
    }
    //---

    // Theta = PI / 2, variing Phi
    @Test
    public void testThetaPI2Phi0() {
        CartesianPoint s = new SphericalPoint(100.0f, PI / 2.0f, 0.0f).convertToCartesian();
        CartesianPoint c = new CartesianPoint(100.0f, 0.0f, 0.0f);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));
    }

    @Test
    public void testThetaPI2PhiPI2() {
        CartesianPoint s = new SphericalPoint(100.0f, PI / 2.0f, PI / 2.0f).convertToCartesian();
        CartesianPoint c = new CartesianPoint(0.0f, 0.0f, 100.0f);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));
    }

    @Test
    public void testThetaPI2PhiPI() {
        CartesianPoint s = new SphericalPoint(100.0f, PI / 2.0f, PI).convertToCartesian();
        CartesianPoint c = new CartesianPoint(-100.0f, 0.0f, 0.0f);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));
    }

    @Test
    public void testThetaPI2Phi3PI2() {
        CartesianPoint s = new SphericalPoint(100.0f, PI / 2.0f, 3.0f * PI / 2.0f).convertToCartesian();
        CartesianPoint c = new CartesianPoint(0.0f, 0.0f, -100.0f);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));
    }

    @Test
    public void testThetaPI2Phi2PI() {
        CartesianPoint s = new SphericalPoint(100.0f, PI / 2.0f, 2.0f * PI).convertToCartesian();
        CartesianPoint c = new CartesianPoint(100.0f, 0.0f, 0.0f);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));
    }
    //---

    // Theta = PI, varing Phi
    @Test
    public void testThetaPIPhi0() {
        CartesianPoint s = new SphericalPoint(100.0f, PI, 0.0f).convertToCartesian();
        CartesianPoint c = new CartesianPoint(0.0f, -100.0f, 0.0f);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));
    }

    @Test
    public void testThetaPIPhiPI2() {
        CartesianPoint s = new SphericalPoint(100.0f, PI, PI / 2.0f).convertToCartesian();
        CartesianPoint c = new CartesianPoint(0.0f, -100.0f, 0.0f);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));
    }

    @Test
    public void testThetaPIPhiPI() {
        CartesianPoint s = new SphericalPoint(100.0f, PI, PI).convertToCartesian();
        CartesianPoint c = new CartesianPoint(0.0f, -100.0f, 0.0f);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));
    }

    @Test
    public void testThetaPIPhi3PI2() {
        CartesianPoint s = new SphericalPoint(100.0f, PI, 3.0f * PI / 2.0f).convertToCartesian();
        CartesianPoint c = new CartesianPoint(0.0f, -100.0f, 0.0f);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));
    }

    @Test
    public void testThetaPIPhi2PI() {
        CartesianPoint s = new SphericalPoint(100.0f, PI, 2.0f * PI).convertToCartesian();
        CartesianPoint c = new CartesianPoint(0.0f, -100.0f, 0.0f);

        assertTrue(Utils.compareToZero(s.getX() - c.getX()));
        assertTrue(Utils.compareToZero(s.getY() - c.getY()));
        assertTrue(Utils.compareToZero(s.getZ() - c.getZ()));
    }
    //---
}
