package io.github.mechance782.mavenCentroidFinder;

import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.Frame;
import java.awt.image.BufferedImage;
import java.util.List;

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
     * If no groups are found, a group of size 0, with coordinates -1, -1 should be returned.
     * 
     * @param frame frame to search for centroid in
     */
    @Override
    public Group largestCentroid(Frame frame) {
        
        // Conver the frame to a BufferedImage for processing
        BufferedImage image = frameConverter.convert(frame);

        // Find all of the connected groups in the image
        List<Group> foundGroups = groupFinder.findConnectedGroups(image);

        // If no groups are found, return the default "empty" group
        if(foundGroups.isEmpty()){
            return new Group(0, new Coordinate(-1, -1));
        }

        // Return the first group found in the sorted list 
        return foundGroups.get(0);
    }
    
}
