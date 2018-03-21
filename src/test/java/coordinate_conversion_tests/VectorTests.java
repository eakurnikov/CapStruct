package coordinate_conversion_tests;

import com.private_void.core.geometry.Vector;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class VectorTests {
    private static final double ACCURACY = 0.000000000000001;
    private static boolean equalsWithAccuracy(double a, double b) {
        return Math.abs(a - b) < ACCURACY;
    }

    @Test
    public void normalizeTest1() {
        Vector v = new Vector(1.0, 0.0, 0.0);
        assertTrue(equalsWithAccuracy(v.getX() * v.getX() + v.getY() * v.getY() + v.getZ() * v.getZ(), 1.0));
    }

    @Test
    public void normalizeTest2() {
        Vector v = new Vector(0.5, 0.5, 0.0);
        assertTrue(equalsWithAccuracy(v.getX() * v.getX() + v.getY() * v.getY() + v.getZ() * v.getZ(), 1.0));
    }

    @Test
    public void normalizeTest3() {
        Vector v = new Vector(1.0, 1.0, 1.0);
        assertTrue(equalsWithAccuracy(v.getX() * v.getX() + v.getY() * v.getY() + v.getZ() * v.getZ(), 1.0));
    }

    @Test
    public void normalizeTest4() {
        Vector v = new Vector(2.0, 1.0, 0.0);
        assertTrue(equalsWithAccuracy(v.getX() * v.getX() + v.getY() * v.getY() + v.getZ() * v.getZ(), 1.0));
    }

    @Test
    public void normalizeTest5() {
        Vector v = new Vector(10.0, 1.0, 0.0);
        assertTrue(equalsWithAccuracy(v.getX() * v.getX() + v.getY() * v.getY() + v.getZ() * v.getZ(), 1.0));
    }
}
