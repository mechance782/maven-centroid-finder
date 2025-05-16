package io.github.mechance782.mavenCentroidFinder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.PrintWriter;
import org.bytedeco.javacv.Frame;
import java.io.StringWriter;

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
}
