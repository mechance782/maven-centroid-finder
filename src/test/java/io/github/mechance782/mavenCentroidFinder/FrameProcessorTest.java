package io.github.mechance782.mavenCentroidFinder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.image.BufferedImage;
import java.util.List;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.Frame;

import org.junit.jupiter.api.Test;

public class FrameProcessorTest {


    // Fake Image Group Finder to set up FrameProcessor
    class FakeImageGroupFinder implements ImageGroupFinder {

        private final List<Group> predefinedGroups;

        public FakeImageGroupFinder(List<Group> predefinedGroups){
            this.predefinedGroups = predefinedGroups;
        }

        @Override
        public List<Group> findConnectedGroups(BufferedImage image) {
            return predefinedGroups;
        }
    }

    @Test
    public void largestCentroid_OneGroup(){
        // Create frame by making image then converting it to frame.
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0xff0000);
        image.setRGB(0, 1, 0xff0000);
        image.setRGB(0, 2, 0xff0000);

        Java2DFrameConverter converter = new Java2DFrameConverter();
        Frame frame = converter.convert(image);
        converter.close();

        // Define list of groups found in the frame
        List<Group> groups = List.of(new Group(3, new Coordinate(0, 1)));

        // Define expected largestCentroid output
        Group expected = new Group(3, new Coordinate(0, 1));

        // Create FrameProcessor with FakeImageGroupFinder and predefined list of groups
        FrameProcessor processor = new FrameProcessor(new FakeImageGroupFinder(groups));
        Group actual = processor.largestCentroid(frame);

        assertEquals(expected, actual);
    }

    @Test
    public void largestCentroid_ManyGroups(){
        BufferedImage image = new BufferedImage(7, 8, BufferedImage.TYPE_INT_RGB);
        int red = 0xFF0000;
        // Row 0
        image.setRGB(2, 0, red);
        // Row 1
        image.setRGB(2, 1, red);
        image.setRGB(4, 1, red);
        // Row 2
        image.setRGB(3, 2, red);
        image.setRGB(4, 2, red);
        // Row 3
        image.setRGB(3, 3, red);
        image.setRGB(4, 3, red);
        // Row 5
        image.setRGB(1, 5, red);
        image.setRGB(2, 5, red);
        image.setRGB(3, 5, red);
        image.setRGB(4, 5, red);
        // Row 6
        image.setRGB(2, 6, red);
        image.setRGB(3, 6, red);
        image.setRGB(6, 6, red);
        // Row 7
        image.setRGB(5, 7, red);
        image.setRGB(6, 7, red);

        Java2DFrameConverter converter = new Java2DFrameConverter();
        Frame frame = converter.convert(image);
        converter.close();

        List<Group> groups = List.of(new Group(6, new Coordinate(2, 5)),
        new Group(5, new Coordinate(3, 2)), new Group(3, new Coordinate(5, 6)),
        new Group(2, new Coordinate(2, 0)));

        Group expected = new Group(6, new Coordinate(2, 5));

        // Create FrameProcessor with FakeImageGroupFinder and predefined list of groups
        FrameProcessor processor = new FrameProcessor(new FakeImageGroupFinder(groups));
        Group actual = processor.largestCentroid(frame);

        assertEquals(expected, actual);
    }

    public void largestCentroid_NoGroup(){
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);

        Java2DFrameConverter converter = new Java2DFrameConverter();
        Frame frame = converter.convert(image);
        converter.close();

        // Define list of groups found in the frame
        // Image Group Finder returns an empty list if no groups are found
        List<Group> groups = List.of();

        // Define expected largestCentroid output
        Group expected = new Group(0, new Coordinate(-1, -1));

        // Create FrameProcessor with FakeImageGroupFinder and predefined list of groups
        FrameProcessor processor = new FrameProcessor(new FakeImageGroupFinder(groups));
        Group actual = processor.largestCentroid(frame);

        assertEquals(expected, actual);
    }

    public void largestCentroid_RealImageGroupFinder(){
        // Create Frame from BufferedImage
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0xb69a7a); // 1
        image.setRGB(1, 0, 0xb69a98); // 1
        image.setRGB(2, 0, 0xb69a9a); // 0
        image.setRGB(0, 1, 0xb69a9f); // 0
        image.setRGB(1, 1, 0xb69a7f); // 1
        image.setRGB(2, 1, 0xbffa7b); // 0
        image.setRGB(0, 2, 0xf6fa7b); // 0
        image.setRGB(1, 2, 0xb69a76); // 1
        image.setRGB(2, 2, 0x00ffff); // 0

        Java2DFrameConverter converter = new Java2DFrameConverter();
        Frame frame = converter.convert(image);
        converter.close();

        // Create all the objects needed to make a real FrameProcessor
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        int targetColor = 0xb69a7b;
        int threshold = 30;
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);
        ImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());
        FrameProcessor processor = new FrameProcessor(finder);

        // set expected and actual values
        Group expected = new Group(4, new Coordinate(0, 0));
        Group actual = processor.largestCentroid(frame);

        assertEquals(expected, actual);
    }
}
