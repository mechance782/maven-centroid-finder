package io.github.mechance782.mavenCentroidFinder;


public interface VideoAnalyzer {

    /**
     * Parses through video frames to find the largest group at each second
     * and record the groups centroids in a csv file, with the corresponding timestamp
     * 
     * @param fileName file path to mp4 video
     */
    public void centroidToCsv(String fileName);

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
    public void writeCentroid(int seconds, Group group);

}
