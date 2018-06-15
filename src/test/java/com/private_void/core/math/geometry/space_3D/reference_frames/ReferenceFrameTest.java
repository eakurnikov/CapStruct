package com.private_void.core.math.geometry.space_3D.reference_frames;

import com.private_void.core.math.geometry.space_3D.coordinates.CartesianPoint;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ReferenceFrameTest {
    private final CartesianPoint a = new CartesianPoint(0.0, 0.0, 0.0);

    private final CartesianPoint b = new CartesianPoint(10.0, 0.0, 0.0);
    private final CartesianPoint c = new CartesianPoint(0.0, 10.0, 0.0);
    private final CartesianPoint d = new CartesianPoint(0.0, 0.0, 10.0);

    private final CartesianPoint f = new CartesianPoint(10.0, 10.0, 0.0);
    private final CartesianPoint g = new CartesianPoint(10.0, 0.0, 10.0);
    private final CartesianPoint h = new CartesianPoint(0.0, 10.0, 10.0);

    private final CartesianPoint i = new CartesianPoint(10.0, 10.0, 10.0);

    @Test
    public void shiftTest() {
        ReferenceFrame.Converter converter = new ReferenceFrame.Converter(
                ReferenceFrame.builder()
                        .atPoint(new CartesianPoint(10.0, 0.0, 0.0))
                        .build());

        CartesianPoint convertedB = converter.convert(b);

        assertTrue(
                convertedB.getX() == (a.getX()) &&
                        convertedB.getY() == (a.getY()) &&
                        convertedB.getZ() == (a.getZ())
        );

        CartesianPoint convertedBackB = converter.convertBack(convertedB);

        assertTrue(
                convertedBackB.getX() == (b.getX()) &&
                        convertedBackB.getY() == (b.getY()) &&
                        convertedBackB.getZ() == (b.getZ())
        );
    }

    @Test
    public void rotationTest() {
        ReferenceFrame.Converter converter = new ReferenceFrame.Converter(
                ReferenceFrame.builder()
                        .setAngleAroundOY(30.0)
                        .build());

        CartesianPoint convertedH = converter.convert(h);

        CartesianPoint convertedBackH = converter.convertBack(convertedH);

        assertTrue(
                convertedBackH.getX() == (h.getX()) &&
                        convertedBackH.getY() == (h.getY()) &&
                        convertedBackH.getZ() == (h.getZ())
        );
    }
}