package io.github.mechance782.mavenCentroidFinder;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.awt.image.BufferedImage;


public class BinarizingImageGroupFinderTest {
    // set up fakes to make testing simpler
    class FakeBinarizer implements ImageBinarizer{
        private final int[][] predefinedOutput;

        public FakeBinarizer(int[][] predefinedOutput){
            this.predefinedOutput = predefinedOutput;
        }

        @Override
        public int[][] toBinaryArray(BufferedImage image){
            return predefinedOutput;
        }

        @Override
        public BufferedImage toBufferedImage(int[][] image){
            return new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
        }
    }

    class FakeGroupFinder implements BinaryGroupFinder{
        private final List<Group> predefinedGroups;

        public FakeGroupFinder(List<Group> predefinedGroups){
            this.predefinedGroups = predefinedGroups;
        }

        @Override
        public List<Group> findConnectedGroups(int[][] image){
            return predefinedGroups;
        }
    }

    // Tests Binarizing Image Group Finder with fakes
    @Test
    public void testReturnsPredefinedGroups(){
        BufferedImage dummyImage = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        int[][] fakeBinary = {
            {1, 0}, 
            {1, 0}
        };

        Group group1 = new Group(2, new Coordinate(1, 0));
        List<Group> predefinedGroups = List.of(group1);

        BinarizingImageGroupFinder groupFinder = new BinarizingImageGroupFinder(new FakeBinarizer(fakeBinary), 
        new FakeGroupFinder(predefinedGroups));

        List<Group> result = groupFinder.findConnectedGroups(dummyImage);

        assertEquals(predefinedGroups, result);
    }

    @Test 
    public void testNoWhitePixels(){
        BufferedImage dummyImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        int[][] fakeBinary = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };

        List<Group> emptyGroups = List.of();
        BinarizingImageGroupFinder groupFinder = new BinarizingImageGroupFinder(new FakeBinarizer(fakeBinary), 
        new FakeGroupFinder(emptyGroups));

        List<Group> result = groupFinder.findConnectedGroups(dummyImage);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testMultipleGroupsDescOrder(){
        BufferedImage dummyImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        int[][] fakeBinary = {
            {1,0,1},
            {0,1,0},
            {1,0,0}
        };
        
        Group largerGroup = new Group(2, new Coordinate(1,1));
        Group smallerGroup = new Group(1, new Coordinate(0,2));

        List<Group> groups = List.of(largerGroup, smallerGroup);

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(
            new FakeBinarizer(fakeBinary),
            new FakeGroupFinder(groups)
        );

        List<Group> result = finder.findConnectedGroups(dummyImage);

        // Assuming Group implements Comparable and sorts by size
        assertEquals(groups, result);
    }

    @Test
    public void testWithSingularPixel(){
        BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        int[][] fakeBinary = {
            {1}
        };

        Group group1 = new Group(1, new Coordinate(0, 0));
        List<Group> predefinedGroups = List.of(group1);

        BinarizingImageGroupFinder groupFinder = new BinarizingImageGroupFinder(
            new FakeBinarizer(fakeBinary), new FakeGroupFinder(predefinedGroups));
        
        List<Group> result = groupFinder.findConnectedGroups(dummyImage);

        assertEquals(predefinedGroups, result);
    }

    // Tests Binarizing Image Group Finder with real input
    @Test
    public void testFindConnectedGroups_withRealBinarizerAndFinder(){
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        
        int targetColor = 0xb69a7b;
        int threshold = 30;

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);
        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();

        // Create 3x3 pixel image
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


        List<Group> groups = finder.findConnectedGroups(binarizer.toBinaryArray(image));

        assertEquals(1, groups.size());

        Group testGroup = groups.get(0);
        assertEquals(4, testGroup.size());

    }
}