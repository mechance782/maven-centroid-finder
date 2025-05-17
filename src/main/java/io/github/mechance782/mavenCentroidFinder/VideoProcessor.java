package io.github.mechance782.mavenCentroidFinder;
import java.io.PrintWriter;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Frame;

/**
 * The VideoProcessor class implements the VideoAnalyzer interface
 * and coordinates processing of video frames and output of object centroid data.
 * 
 * It uses a FrameProcessor to perform frame-level analysis and a
 * PrintWriter to log the results, such as centroid positions at given timestamps.
 * 
 */

public class VideoProcessor implements VideoAnalyzer {
    
    FrameCentroidFinder frameProcessor;
    PrintWriter writer;

    public VideoProcessor(FrameCentroidFinder processor, PrintWriter writer){
        this.frameProcessor = processor;
        this.writer = writer;
    }

    /**
     * Parses through video frames to find the largest group at each second
     * and record the groups centroids in a csv file, with the corresponding timestamp.
     * 
     * Use frameGrabber to parse through frames, pass each frame to processor.largestCentroid
     * to find the frames largest group, then record the group and timestamp to the csv file
     * using writeCentroid
     * 
     * @param fileName file path to mp4 video
     */
    @Override
    public void centroidToCsv(String fileName) {

        try (FrameGrabber grabber = new FFmpegFrameGrabber(fileName)) {
            grabber.start();
            
            double doubleRate = grabber.getFrameRate();
            int rate = (int) doubleRate;

            for(int i = 0; i < grabber.getLengthInFrames(); i++){
                Frame frame = grabber.grabFrame();

                if (i % rate == 0){
                    Group frameCentroid = frameProcessor.largestCentroid(frame);
                    int seconds = i / rate;
                    writeCentroid(seconds, frameCentroid);
                }

            }
        } catch (Exception e) {
            System.out.println("Frame Grabber Error: " + e);
        }
    }

    /**
     * Writes the centroid information of a specified group at a given time (in seconds)
     * to the analysis output. This could be used to record object positions for 
     * further processing or reporting.
     *
     * @param seconds the time in the video (in seconds) at which the centroid should be written
     * @param group the group of objects for which the centroid is calculated and written;
     *              must not be {@code null}
     * 
     * @throws IllegalArgumentException if the group is null or the time is invalid
     */
    @Override
    public void writeCentroid(int seconds, Group group){
        if (group == null || seconds < 0){
            throw new IllegalArgumentException();
        }

        String csvRow = seconds + "," + group.centroid().x() + "," + group.centroid().y();

        writer.println(csvRow);
        
    }    
}
