package io.github.mechance782.mavenCentroidFinder;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;




// sqrt((r1 - r2)^2 + (g1 - g2)^2 + (b1 - b2)^2)
public class EuclideanColorDistanceTest {
    
    @Test
    public void testDistance_SameColor(){
        // 62, 154, 153
        int colorA = 0x3e9a99;
        int colorB = 0x3e9a99;

        double expected = 0;

        ColorDistanceFinder finder = new EuclideanColorDistance();
        double actual = finder.distance(colorA, colorB);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testDistance_PrimaryAndBlack(){
        // 255, 0, 0
        int colorA = 0xff0000;
        // 0, 0, 0
        int colorB = 0x000000;

        double expected = 255;

        ColorDistanceFinder finder = new EuclideanColorDistance();
        double actual = finder.distance(colorA, colorB);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void testDistance_SimilarColors(){
        // 182, 154, 123
        int colorA = 0xb69a7b;
        // 182, 154, 153
        int colorB = 0xb69a99;

        double expected = 30;

        ColorDistanceFinder finder = new EuclideanColorDistance();
        double actual = finder.distance(colorA, colorB);
        assertEquals(expected, actual, 0.001);

    }

    @Test
    public void testDistance_VeryDifferentColors(){
        // 224, 194, 247
        int colorA = 0xe0c2f7;
        // 33, 54, 74
        int colorB = 0x21364a;

        double expected = 293.2746;

        ColorDistanceFinder finder = new EuclideanColorDistance();
        double actual = finder.distance(colorA, colorB);
        assertEquals(expected, actual, 0.001);
        
    }
}
