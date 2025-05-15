package io.github.mechance782.mavenCentroidFinder;

import org.bytedeco.javacv.Frame;

/**
 * An Interface for finding the largest centroid in a Frame.
 */
public interface FrameCentroidFinder {
    /**
     * Finds the largest centroid in the frame and returns it as a Group.
     * 
     * @param frame 
     * @return a Group representation of the largest centroid in the frame
     */
    public Group largestCentroid(Frame frame);
}
