package io.github.mechance782.mavenCentroidFinder;
import java.io.PrintWriter;


/**
 * The VideoProcessor class implements the VideoAnalyzer interface
 * and coordinates processing of video frames and output of object centroid data.
 * 
 * It uses a FrameProcessor to perform frame-level analysis and a
 * PrintWriter to log the results, such as centroid positions at given timestamps.
 * 
 */

public class VideoProcessor implements VideoAnalyzer {
    
    FrameProcessor processor;
    PrintWriter printer;

    public VideoProcessor(FrameProcessor processor, PrintWriter printer){
        this.processor = processor;
        this.printer = printer;
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

    }    
}
