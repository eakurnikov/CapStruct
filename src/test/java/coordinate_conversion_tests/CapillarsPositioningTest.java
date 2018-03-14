package coordinate_conversion_tests;

import com.private_void.core.geometry.CartesianPoint;
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
                7.0f,
                500.0f,
                0.0f,
                0.0f,
                1.0f,
                90.0f);

        CurvedPlate curvedPlate = new CurvedPlate(
                smoothCylinderFactory,
                new CartesianPoint(0.0f, 0.0f, 0.0f),
                0.0034f,
                Utils.convertDegreesToRadians(3.65f),
                2000.0f);

        List<Capillar> capillars = curvedPlate.getCapillars();

        for (Capillar capillar : capillars) {
            SmoothCylinder cylinder = (SmoothCylinder) capillar;

            CartesianPoint c = cylinder.getPosition().convertToCartesian();
            assertTrue(Utils.compareToZero(
                    c.getX() * c.getX() +
                            c.getY() * c.getY() +
                            c.getZ() * c.getZ() -
                            2000.0f * 2000.0f));

            assertTrue(Utils.compareToZero(
                    (cylinder.getFront().getX() - 2000.0f) * (cylinder.getFront().getX() - 2000.0f) +
                            cylinder.getFront().getY() * cylinder.getFront().getY() +
                            cylinder.getFront().getZ() * cylinder.getFront().getZ() -
                            2000.0f * 2000.0f));
        }
    }
}
