package io.github.mechance782.mavenCentroidFinder;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.bytedeco.javacv.Frame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;


public class VideoProcessorTest {
    // Setting up fakes to make testing simpler
    class FakeFrameProcessor implements FrameCentroidFinder{

        private final Group fakeGroup;

        public FakeFrameProcessor(Group fakeGroup){
            this.fakeGroup = fakeGroup;
        }

        @Override
        public Group largestCentroid(Frame fakeFrame){
            return fakeGroup;
        }
    }

    // Tests that centroid to csv can correctly write to a file
    @Test
    void testCentroidToCsv_recordsCentroid_validInput(){
        // Set up fake grabber and processor
        Group expectedGroup = new Group(4, new Coordinate(1244, 622));
        FakeFrameProcessor fakeProcessor = new FakeFrameProcessor(expectedGroup);

        // Set up writers so we can track what is getting written to the file
        StringWriter writer = new StringWriter();
        PrintWriter printer = new PrintWriter(writer);

        // Run the test method
        VideoProcessor videoProcessor = new VideoProcessor(fakeProcessor, printer);
        videoProcessor.writeCentroid(0, expectedGroup);

        printer.flush();

        // Set up expected output
        String expectedOutput = "0,1244,622";

        //  Compare actual output to expected output
        String actualOutput = writer.toString().trim();
        assertEquals(expectedOutput, actualOutput);
        
    }

    @Test
    void testCentroidToCsv_noFrames(){

    }
    
    // Tests the write centroid method with a valid input
    @Test
    void testWriteCentroid_validInput_writesToOutput(){
        Group group = new Group(4, new Coordinate(2, 4));
        FakeFrameProcessor mockProcessor = new FakeFrameProcessor(group);
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        VideoProcessor videoProcessor = new VideoProcessor(mockProcessor, printWriter);
        
        videoProcessor.writeCentroid(5, group);
        
        String output = writer.toString().trim();
        assertEquals("5,2,4", output);
    }

    @Test
    void testWriteCentroid_ZeroStartTime(){
        Group group = new Group(2, new Coordinate(5, 2));
        FakeFrameProcessor mockProcessor = new FakeFrameProcessor(group);
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        VideoProcessor videoProcessor = new VideoProcessor(mockProcessor, printWriter);
        
        videoProcessor.writeCentroid(0, group);
        
        String output = writer.toString().trim();
        assertEquals("0,5,2", output);
    
    }

    @Test
    void testWriteCentroid_NegativeStartTime(){
        Group group = new Group(4, new Coordinate(3, 6));
        FakeFrameProcessor mockProcessor = new FakeFrameProcessor(group);
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        VideoProcessor videoProcessor = new VideoProcessor(mockProcessor, printWriter);

        assertThrows(IllegalArgumentException.class, () -> {
            videoProcessor.writeCentroid(-4, group);
        });
    }

    
    // Tests what happens when a null group is passed to the method
    @Test
    void testWriteCentroid_nullGroup_throwsException(){
        FakeFrameProcessor mockProcessor = new FakeFrameProcessor(null);
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        VideoProcessor videoProcessor = new VideoProcessor(mockProcessor, printWriter);

        assertThrows(IllegalArgumentException.class, () -> {
            videoProcessor.writeCentroid(3, null);
        });
    }

}
