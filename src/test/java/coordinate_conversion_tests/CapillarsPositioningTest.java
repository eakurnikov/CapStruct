package coordinate_conversion_tests;

import com.private_void.core.geometry.coordinates.CartesianPoint;
import com.private_void.core.plates.CurvedPlate;
import com.private_void.core.surfaces.Capillar;
import com.private_void.core.surfaces.CapillarFactory;
import com.private_void.core.surfaces.smooth_surfaces.smooth_capillars.SmoothCylinder;
import com.private_void.utils.Utils;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class CapillarsPositioningTest {

    @Test
    public void willParticleGetInsideTest() {
        // ...
    }

    @Test
    public void isCapillarCoordinateValidTest() {
        // ...
    }

    @Test
    public void sphericalPositioningTest() {

        CapillarFactory smoothCylinderFactory = SmoothCylinder.getFactory(
                7.0,
                500.0,
                0.0,
                0.0,
                1.0,
                90.0);

        CurvedPlate curvedPlate = new CurvedPlate(
                smoothCylinderFactory,
                CartesianPoint.ORIGIN,
                0.0034,
                Math.toRadians(3.65),
                2000.0);

        List<Capillar> capillars = curvedPlate.getCapillars();

        for (Capillar capillar : capillars) {
            SmoothCylinder cylinder = (SmoothCylinder) capillar;

            CartesianPoint c = cylinder.getPosition().convertToCartesian();
            assertTrue(Utils.compareToZero(
                    c.getX() * c.getX() +
                            c.getY() * c.getY() +
                            c.getZ() * c.getZ() -
                            2000.0 * 2000.0));

            assertTrue(Utils.compareToZero(
                    (cylinder.getFront().getX() - 2000.0) * (cylinder.getFront().getX() - 2000.0) +
                            cylinder.getFront().getY() * cylinder.getFront().getY() +
                            cylinder.getFront().getZ() * cylinder.getFront().getZ() -
                            2000.0 * 2000.0));
        }
    }
}
