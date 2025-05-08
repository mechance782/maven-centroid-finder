package io.github.mechance782.mavenCentroidFinder;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
public class DistanceImageBinarizerTest {

    @Test
    public void testToBinaryArray_nullImage(){
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        
        // target = black
        int targetColor = 0x000000;
        int threshold = 100;

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);

        BufferedImage image = null;

        assertThrows(NullPointerException.class, () -> {
            binarizer.toBinaryArray(image);
        });
    }

    @Test
    public void testToBinaryArray_oneExactMatch(){
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        
        // target = black
        int targetColor = 0x000000;
        int threshold = 100;

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);

        // Create 2x1 pixel image
        BufferedImage image = new BufferedImage(2, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x000000);
        image.setRGB(1, 0, 0xffffff);

        int[][] binary = binarizer.toBinaryArray(image);

        assertEquals(1, binary[0][0]);
        assertEquals(0, binary[0][1]);
    }

    @Test
    public void testToBinaryArray_noMatches(){
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        
        // target = black
        int targetColor = 0x000000;
        int threshold = 100;

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);

        // Create 3x1 pixel image
        BufferedImage image = new BufferedImage(3, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0xff0000);
        image.setRGB(1, 0, 0xffffff);
        image.setRGB(2, 0, 0x00ffff);

        int[][] binary = binarizer.toBinaryArray(image);

        assertEquals(0, binary[0][0]);
        assertEquals(0, binary[0][1]);
        assertEquals(0, binary[0][2]);
    }

    @Test
    public void testToBinaryArray_smallThreshold(){
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        
        int targetColor = 0xb69a7b;
        int threshold = 30;

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);

        // Create 3x3 pixel image
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0xb69a7a); // 1
        image.setRGB(1, 0, 0xb69a98); // 1
        image.setRGB(2, 0, 0xb69a9a); // 0
        image.setRGB(0, 1, 0xb69a9f); // 0
        image.setRGB(1, 1, 0xb69a7f); // 1
        image.setRGB(2, 1, 0xbffa7b); // 0
        image.setRGB(0, 2, 0xf6fa7b); // 0
        image.setRGB(1, 2, 0xb69a76); // 1
        image.setRGB(2, 2, 0x00ffff); // 0

        int[][] binary = binarizer.toBinaryArray(image);

        assertEquals(1, binary[0][0]);
        assertEquals(1, binary[0][1]);
        assertEquals(0, binary[0][2]);
        assertEquals(0, binary[1][0]);
        assertEquals(1, binary[1][1]);
        assertEquals(0, binary[1][2]);
        assertEquals(0, binary[2][0]);
        assertEquals(1, binary[2][1]);
        assertEquals(0, binary[2][2]);
    }

    @Test
    public void testToBinaryArray_largeThreshold(){
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        
        // target = black
        int targetColor = 0x000000;
        int threshold = 200;

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);

        // Create 3x1 pixel image
        BufferedImage image = new BufferedImage(3, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0xff0000); // diff = 255 
        image.setRGB(1, 0, 0x1f1f1f);
        image.setRGB(2, 0, 0x0000c1); // diff = 192

        int[][] binary = binarizer.toBinaryArray(image);

        assertEquals(0, binary[0][0]);
        assertEquals(1, binary[0][1]);
        assertEquals(1, binary[0][2]);
    }

    @Test
    public void testToBinaryArray_changeGreen() {
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();

        int targetColor = 0x000000; // pure black
        int threshold = 100;

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);

        // Create a 3x1 image where only the GREEN channel varies
        // Format: 0x00GG00 (R=0, G=variable, B=0)
        BufferedImage image = new BufferedImage(3, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x002000); // G = 32 → distance = 32
        image.setRGB(1, 0, 0x008000); // G = 128 → distance = 128
        image.setRGB(2, 0, 0x006400); // G = 100 → distance = 100

        int[][] binary = binarizer.toBinaryArray(image);

        // Expectation with threshold = 100:
        // 0x002000 → distance 32 < 100 → white (1)
        // 0x008000 → distance 128 > 100 → black (0)
        // 0x006400 → distance 100 == threshold → black (0), because threshold is exclusive

        assertEquals(1, binary[0][0]);
        assertEquals(0, binary[0][1]);
        assertEquals(0, binary[0][2]);
    }

    @Test
    public void testToBinaryArray_changeRed() {
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        int targetColor = 0x000000; // pure black
        int threshold = 100;

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);

        // Format: 0xRR0000 (only red varies)
        BufferedImage image = new BufferedImage(3, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x200000); // R = 32 → distance = 32
        image.setRGB(1, 0, 0x800000); // R = 128 → distance = 128
        image.setRGB(2, 0, 0x640000); // R = 100 → distance = 100

        int[][] binary = binarizer.toBinaryArray(image);

        assertEquals(1, binary[0][0]);
        assertEquals(0, binary[0][1]);
        assertEquals(0, binary[0][2]);
    }

    @Test
    public void testToBinaryArray_changeBlue() {
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        int targetColor = 0x000000; // pure black
        int threshold = 100;

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);

        // Format: 0x0000BB (only blue varies)
        BufferedImage image = new BufferedImage(3, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x000020); // B = 32 → distance = 32
        image.setRGB(1, 0, 0x000080); // B = 128 → distance = 128
        image.setRGB(2, 0, 0x000064); // B = 100 → distance = 100

        int[][] binary = binarizer.toBinaryArray(image);

        assertEquals(1, binary[0][0]);
        assertEquals(0, binary[0][1]);
        assertEquals(0, binary[0][2]);
    }

    @Test
    public void testToBinaryArray_grayscaleGradient() {
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        int targetColor = 0x000000; // black
        int threshold = 180;

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);

        BufferedImage image = new BufferedImage(3, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x111111); // grayscale ~17, distance ≈ 29.4
        image.setRGB(1, 0, 0x808080); // grayscale 128, distance ≈ 221.7
        image.setRGB(2, 0, 0x646464); // grayscale 100, distance ≈ 173.2

        int[][] binary = binarizer.toBinaryArray(image);

        assertEquals(1, binary[0][0]);
        assertEquals(0, binary[0][1]);
        assertEquals(1, binary[0][2]);
    }

    @Test
    public void testToBinaryArray_colorDiagonal() {
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        int targetColor = 0x000000;
        int threshold = 160;

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);

        BufferedImage image = new BufferedImage(3, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x404080); // ≈ 156.76 distance
        image.setRGB(1, 0, 0xA0A020); // ≈ 230.5 distance
        image.setRGB(2, 0, 0x000000); // distance = 0

        int[][] binary = binarizer.toBinaryArray(image);

        assertEquals(1, binary[0][0]);
        assertEquals(0, binary[0][1]);
        assertEquals(1, binary[0][2]);
    }

    // toBufferedImage TESTS!!!!
    @Test
    public void testToBufferedImage_nullArray(){
        // set up
        int[][] image = null;
        ImageBinarizer binarizer = new DistanceImageBinarizer(null, 0, 0);

        assertThrows(NullPointerException.class, () -> {
            binarizer.toBufferedImage(image);
        });
    }

    @Test
    public void testToBufferedImage_nullSubArray(){
        // set up
        int[][] image = {
            {0},
            null
        };
        ImageBinarizer binarizer = new DistanceImageBinarizer(null, 0, 0);

        assertThrows(NullPointerException.class, () -> {
            binarizer.toBufferedImage(image);
        });
    }

    @Test
    public void testToBufferedImage_emptyArray(){
        // set up
        int[][] image = {};
        ImageBinarizer binarizer = new DistanceImageBinarizer(null, 0, 0);

        assertThrows(IllegalArgumentException.class, () -> {
            binarizer.toBufferedImage(image);
        });
    }

    @Test
    public void testToBufferedImage_emptySubArray(){
        // set up
        int[][] image = {
            {}
        };
        ImageBinarizer binarizer = new DistanceImageBinarizer(null, 0, 0);

        assertThrows(IllegalArgumentException.class, () -> {
            binarizer.toBufferedImage(image);
        });
    }

    @Test
    public void testToBufferedImage_simple(){
        // set up
        int[][] image = {
            {0, 0},
            {1, 1}
        };
        // constructor params can be set to default since the fields are not used in 
        // toBufferedImage
        ImageBinarizer binarizer = new DistanceImageBinarizer(null, 0, 0);

        BufferedImage actual = binarizer.toBufferedImage(image);

        assertEquals(2, actual.getWidth());
        assertEquals(2, actual.getHeight());

        // 0x000000 = black / 0
        // 0xffffff = white / 1
        // since getRGB returns an ARGB value, add ff to the left 
        // of every expected rgb value. The ff represents the alpha value
        // of the color, which should be set to max by default.
        assertEquals(0xff000000, actual.getRGB(0, 0));
        assertEquals(0xff000000, actual.getRGB(1, 0));
        assertEquals(0xffffffff, actual.getRGB(0, 1));
        assertEquals(0xffffffff, actual.getRGB(1, 1));
    }

    @Test
    public void testToBufferedImage_allBlack() {
        int[][] binary = {
            {0, 0},
            {0, 0}
        };

        ImageBinarizer binarizer = new DistanceImageBinarizer(null, 0, 0);
        BufferedImage image = binarizer.toBufferedImage(binary);

        assertEquals(2, image.getWidth());
        assertEquals(2, image.getHeight());

        assertEquals(0xff000000, image.getRGB(0, 0));
        assertEquals(0xff000000, image.getRGB(1, 0));
        assertEquals(0xff000000, image.getRGB(0, 1));
        assertEquals(0xff000000, image.getRGB(1, 1));
    }

    @Test
    public void testToBufferedImage_allWhite() {
        int[][] binary = {
            {1, 1},
            {1, 1}
        };

        ImageBinarizer binarizer = new DistanceImageBinarizer(null, 0, 0);
        BufferedImage image = binarizer.toBufferedImage(binary);

        assertEquals(2, image.getWidth());
        assertEquals(2, image.getHeight());

        assertEquals(0xffFFFFFF, image.getRGB(0, 0));
        assertEquals(0xffFFFFFF, image.getRGB(1, 0));
        assertEquals(0xffFFFFFF, image.getRGB(0, 1));
        assertEquals(0xffFFFFFF, image.getRGB(1, 1));
    }

    @Test
    public void testToBufferedImage_checkerboardPattern() {
        int[][] binary = {
            {0, 1},
            {1, 0}
        };

        ImageBinarizer binarizer = new DistanceImageBinarizer(null, 0, 0);
        BufferedImage image = binarizer.toBufferedImage(binary);

        assertEquals(2, image.getWidth());
        assertEquals(2, image.getHeight());

        assertEquals(0xff000000, image.getRGB(0, 0));
        assertEquals(0xffFFFFFF, image.getRGB(1, 0));
        assertEquals(0xffFFFFFF, image.getRGB(0, 1));
        assertEquals(0xff000000, image.getRGB(1, 1));
    }

    @Test
    public void testToBufferedImage_nonSquareDimensions() {
        int[][] binary = {
            {1, 0, 1},
            {0, 1, 0}
        };

        ImageBinarizer binarizer = new DistanceImageBinarizer(null, 0, 0);
        BufferedImage image = binarizer.toBufferedImage(binary);

        assertEquals(3, image.getWidth());
        assertEquals(2, image.getHeight());

        assertEquals(0xffFFFFFF, image.getRGB(0, 0));
        assertEquals(0xff000000, image.getRGB(1, 0));
        assertEquals(0xffFFFFFF, image.getRGB(2, 0));

        assertEquals(0xff000000, image.getRGB(0, 1));
        assertEquals(0xffFFFFFF, image.getRGB(1, 1));
        assertEquals(0xff000000, image.getRGB(2, 1));
    }


}
