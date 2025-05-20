package io.github.mechance782.mavenCentroidFinder;

import java.io.File;
import java.io.PrintWriter;

/**
 * Parses command line arguments and launches the video processing tracker
 * 
 * Expected CLI: java -jar videoprocessor.jar inputPath outputCsv targetColor threshold
 * @param args
 */
public class CentroidTrackerApp {
    public static void main(String[] args) {
        // check for correct number of arguments
        if (args.length != 4){
            System.out.println("Usage: java -jar videoprocessor.jar <inputPath> <outputCsv> <targetColor> <threshold>");
            return;
        }

        String inputVideoPath = args[0];
        String outputCsvPath = args[1];
        String hexTargetColor = args[2];

        // try parsing threshold argument to int (and validates int input)
        int threshold = 0;
        try {
            threshold = Integer.parseInt(args[3]);
        } catch (Exception e) {
            System.err.println("Threshold must be an integer.");
            return;
        }

        // try parsing hex color to int (which validates hex number input)
        int targetColor = 0;
        try {
            targetColor = Integer.parseInt(hexTargetColor, 16);
        } catch (Exception e) {
            System.err.println("Invalid hex target color. Please provide a color in RRGGBB format.");
            return;
        }

        // check if File exists at given path string
        File videoFile = new File(inputVideoPath);
        if (!videoFile.exists()){
            System.err.println("Invalid input path to mp4 video. File not Found");
            return;
        }

        // Create all objects needed for Video Processor
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();

        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);

        ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());

        FrameCentroidFinder frameProcessor = new FrameProcessor(groupFinder);

        // create printwriter for video processor, then call centroidToCsv with the input path
        try (PrintWriter writer = new PrintWriter(outputCsvPath)) {
            VideoAnalyzer videoProcessor = new VideoProcessor(frameProcessor, writer);
            videoProcessor.centroidToCsv(inputVideoPath);
        } catch (Exception e) {
            System.err.println("Error writing to csv file");
            e.printStackTrace();
        }


    }

}
