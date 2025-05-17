package io.github.mechance782.mavenCentroidFinder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.PrintWriter;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
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

    class FakeFrameGrabber extends FrameGrabber {
        private int lengthInFrames;
        private double frameRate;
        private int currentFrame = 0;

        public FakeFrameGrabber(int lengthInFrames, double frameRate){
            this.lengthInFrames = lengthInFrames;
            this.frameRate = frameRate;
        }
         
        // Auto generated these since VS Code was mad at me. Only adding logic to methods we need
        @Override
        public void start() throws Exception {
            throw new UnsupportedOperationException("Unimplemented method 'start'");
        }

        @Override
        public void stop() throws Exception {
            throw new UnsupportedOperationException("Unimplemented method 'stop'");
        }

        @Override
        public void trigger() throws Exception {
            throw new UnsupportedOperationException("Unimplemented method 'trigger'");
        }
    
        @Override
        public int getLengthInFrames(){
            return lengthInFrames;
        }

        @Override
        public Frame grab(){
            // Simulate grabbing a frame
            if(currentFrame < getLengthInFrames()){
                currentFrame++;
                return new Frame(); // Just returns a frame since we are just testing that it can write to a file
            }
            return null;
        }

        @Override
        public double getFrameRate(){
            return frameRate;
        }

        @Override
        public void release() throws Exception {
            throw new UnsupportedOperationException("Unimplemented method 'release'");
        }
    }

    // Tests that centroid to csv can correctly write to a file
    @Test
    void testCentroidToCsv_recordsCentroid_validInput(){

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
