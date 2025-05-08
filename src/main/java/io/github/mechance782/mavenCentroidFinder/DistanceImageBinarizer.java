package io.github.mechance782.mavenCentroidFinder;

import java.awt.image.BufferedImage;

/**
 * An implementation of the ImageBinarizer interface that uses color distance
 * to determine whether each pixel should be black or white in the binary image.
 * 
 * The binarization is based on the Euclidean distance between a pixel's color and a reference target color.
 * If the distance is less than the threshold, the pixel is considered white (1);
 * otherwise, it is considered black (0).
 * 
 * The color distance is computed using a provided ColorDistanceFinder, which defines how to compare two colors numerically.
 * The targetColor is represented as a 24-bit RGB integer in the form 0xRRGGBB.
 */
public class DistanceImageBinarizer implements ImageBinarizer {
    private final ColorDistanceFinder distanceFinder;
    private final int threshold;
    private final int targetColor;

    /**
     * Constructs a DistanceImageBinarizer using the given ColorDistanceFinder,
     * target color, and threshold.
     * 
     * The distanceFinder is used to compute the Euclidean distance between a pixel's color and the target color.
     * The targetColor is represented as a 24-bit hex RGB integer (0xRRGGBB).
     * The threshold determines the cutoff for binarization: pixels with distances less than
     * the threshold are marked white, and others are marked black.
     *
     * @param distanceFinder an object that computes the distance between two colors
     * @param targetColor the reference color as a 24-bit hex RGB integer (0xRRGGBB)
     * @param threshold the distance threshold used to decide whether a pixel is white or black
     */
    public DistanceImageBinarizer(ColorDistanceFinder distanceFinder, int targetColor, int threshold) {
        this.distanceFinder = distanceFinder;
        this.targetColor = targetColor;
        this.threshold = threshold;
    }

    /**
     * Converts the given BufferedImage into a binary 2D array using color distance and a threshold.
     * Each entry in the returned array is either 0 or 1, representing a black or white pixel.
     * A pixel is white (1) if its Euclidean distance to the target color is less than the threshold.
     *
     * @param image the input RGB BufferedImage
     * @return a 2D binary array where 1 represents white and 0 represents black
     */
    @Override
    public int[][] toBinaryArray(BufferedImage image) {
        if (image == null){
            throw new NullPointerException("Image may not be null");
        }
        // get the width and height of the image --> getWidth() and getHeight()
        // create a 2d array with the "width" and "height" set to width and height
        int[][] binary = new int[image.getHeight()][image.getWidth()];

        // loop through the array
        for (int y = 0; y < binary.length; y++){
            for (int x = 0; x < binary[y].length; x++){
                // grab the pixel at each "x" and "y" point
                int pixel = image.getRGB(x, y);
                // compare the pixel color (getRGB) to targetColor (using EuclideanColorDistance method)
                double difference = distanceFinder.distance(pixel, targetColor);
                // set the coordinate to white (1) or black(0)
                if (difference < threshold){
                    binary[y][x] = 1;
                } else {
                    binary[y][x] = 0;
                }
            }
        }
            
        // return the array
        return binary;
    }

    /**
     * Converts a binary 2D array into a BufferedImage.
     * Each value should be 0 (black) or 1 (white).
     * Black pixels are encoded as 0x000000 and white pixels as 0xFFFFFF.
     *
     * @param image a 2D array of 0s and 1s representing the binary image
     * @return a BufferedImage where black and white pixels are represented with standard RGB hex values
     */
    @Override
    public BufferedImage toBufferedImage(int[][] image) {
        if (image == null){
            throw new NullPointerException("Array cannot be null");
        }
        if (image.length == 0){
            throw new IllegalArgumentException("Array cannot be empty");
        }
        // create a new BufferedImage with width and height proportionate to image[][]
        BufferedImage newImage = new BufferedImage(image[0].length, image.length, BufferedImage.TYPE_INT_RGB);
        // loop through the array
        for (int y = 0; y < image.length; y++){
            if (image[y] == null){
                throw new NullPointerException("Sub array cannot be null");
            }
            if (image[y].length == 0){
                throw new IllegalArgumentException("Sub array cannot be empty");
            }
            for (int x = 0; x < image[y].length; x++){
                // set the pixel at the "x" and "y" point (setRGB)
                // 0xFFFFF --> white (1)
                if (image[y][x] == 1){
                    newImage.setRGB(x, y, 0xffffff);
                } else {
                    // 0x000000 --> black (0)
                    newImage.setRGB(x, y, 0x000000);
                }
            }
        }
            
        // return the new buffered image!
        return newImage;
    }
}
