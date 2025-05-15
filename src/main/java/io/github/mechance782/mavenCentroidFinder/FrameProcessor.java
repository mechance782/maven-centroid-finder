package io.github.mechance782.mavenCentroidFinder;

import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.Frame;

public class FrameProcessor implements FrameCentroidFinder{
    private final ImageGroupFinder groupFinder;
    private final Java2DFrameConverter frameConverter;

    /**
     * Constructs a FrameProcessor using the given groupFinder and a default field, 
     * frameConverter (used to convert Frame into BufferedImage).
     * 
     * @param groupFinder object that finds and returns a sorted list of Groups from an image
     */
    public FrameProcessor(ImageGroupFinder groupFinder) {
        this.groupFinder = groupFinder;
        this.frameConverter = new Java2DFrameConverter();
    }

    /**
     * This method will use frameConverter to convert frame into a BufferedImage
     * The BufferedImage will be used as a parameter for findConnectedGroups from
     * the groupFinder object.
     * 
     * The first group in the sorted list will be the largest group, and will be returned.
     * 
     * @param frame frame to search for centroid in
     */
    @Override
    public Group largestCentroid(Frame frame) {
        return null;
    }
    
}
