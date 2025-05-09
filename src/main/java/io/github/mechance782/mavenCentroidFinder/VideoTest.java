package io.github.mechance782.mavenCentroidFinder;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;


public class VideoTest {

    public static void main(String[] args) {
        FrameGrabber grabber = new FFmpegFrameGrabber(args[0]);
        
    }
    
}
