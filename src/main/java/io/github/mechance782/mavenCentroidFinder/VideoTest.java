package io.github.mechance782.mavenCentroidFinder;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;


public class VideoTest {

    public static void main(String[] args) {
        
        try(FrameGrabber grabber = new FFmpegFrameGrabber(args[0])){
            grabber.start();
            System.out.println("Grabber has been started!");
            System.out.println("Video length in frames: " + grabber.getLengthInFrames());
            System.out.println("Video format: " + grabber.getFormat());

            Frame frame = grabber.grab();
            if(frame != null){
                System.out.println("Grabbed a frame");
            } else{
                System.out.println("Frame is null, failed to grab.");
            }

            grabber.stop();
        }catch(Exception e){
            System.out.println("Error: " + e);;
        }
    }
    
}
