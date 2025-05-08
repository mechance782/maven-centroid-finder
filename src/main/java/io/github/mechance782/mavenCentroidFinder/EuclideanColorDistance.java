package io.github.mechance782.mavenCentroidFinder;

public class EuclideanColorDistance implements ColorDistanceFinder {
    /**
     * Returns the euclidean color distance between two hex RGB colors.
     * 
     * Each color is represented as a 24-bit integer in the form 0xRRGGBB, where
     * RR is the red component, GG is the green component, and BB is the blue component,
     * each ranging from 0 to 255.
     * 
     * The Euclidean color distance is calculated by treating each color as a point
     * in 3D space (red, green, blue) and applying the Euclidean distance formula:
     * 
     * sqrt((r1 - r2)^2 + (g1 - g2)^2 + (b1 - b2)^2)
     * 
     * This gives a measure of how visually different the two colors are.
     * 
     * @param colorA the first color as a 24-bit hex RGB integer
     * @param colorB the second color as a 24-bit hex RGB integer
     * @return the Euclidean distance between the two colors
     */
    @Override
    public double distance(int colorA, int colorB) {
        int[] colorA_RGBified = rgbConverter(colorA);
        int[] colorB_RGBified = rgbConverter(colorB);

        int r1 = colorA_RGBified[0];
        int r2 = colorB_RGBified[0];

        int g1 = colorA_RGBified[1];
        int g2 = colorB_RGBified[1];

        int b1 = colorA_RGBified[2];
        int b2 = colorB_RGBified[2];

        double colorDifference = Math.sqrt(diffCalc(r1, r2) + diffCalc(g1, g2) + diffCalc(b1, b2));

        return colorDifference;
    }

    private int diffCalc(int color1, int color2){
        int colorDifference = color1-color2;

        return (int) Math.pow(colorDifference, 2);
    }

    private int[] rgbConverter(int color){
        int[] rgbHolder = new int[3];

        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;
        
        rgbHolder[0] = red;
        rgbHolder[1] = green;
        rgbHolder[2] = blue;

        return rgbHolder;
    }

}
