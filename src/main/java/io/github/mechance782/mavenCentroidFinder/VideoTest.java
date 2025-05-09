package io.github.mechance782.mavenCentroidFinder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;


public class VideoTest {

    public static void main(String[] args) {
        
        try(FrameGrabber grabber = new FFmpegFrameGrabber(args[0])){
            grabber.start();
            System.out.println("Grabber has been started!");
            System.out.println("Video length in frames: " + grabber.getLengthInFrames());
            System.out.println("Video format: " + grabber.getFormat());

            Java2DFrameConverter converter = new Java2DFrameConverter();

            for (int i = 0; i < grabber.getLengthInFrames() - 1; i++){
                Frame frame = grabber.grab();

                if(frame != null){
                    System.out.println("Grabbed a frame" + frame);
                    if (i % 10 == 0){
                        BufferedImage image = converter.convert(frame);
                        try {
                            File outputFile = new File("image" + i + ".png");
                            ImageIO.write(image, "png", outputFile);
                        } catch (IOException e){
                            System.err.println(e.getMessage());
                        }

                    }
                } else{
                    System.out.println("Frame is null, failed to grab.");
                } 
            }
            

            grabber.stop();
        }catch(Exception e){
            System.out.println("Error: " + e);;
        }
    }
    
}
