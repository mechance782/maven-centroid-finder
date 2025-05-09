Native dependency:
- written in low-level languages and compiled for specific operating systems 
- can cause portability issues when running on different systems, creates larger build size, and native errors are harder to understand
- Native libraries are faster, and have more extensive features. 
- Apparently many video processing libraries are native.

JavaCV: 
- Native
- Two libraries combined into one (FFmpeg + OpenCV)
- handles multiple video formats
- easily creates BufferedImages

Pros:
-	Gives you access to OpenCV and FFmpeg, making it a powerful and flexible option
-	Optimizations have been put in place making frame capturing, processing, and encoding fast and efficient.
-	There is the possibility of being able to add real-time processing. Meaning that we could add integrations for live video feeds.
-	Has a ton of advanced features such as video transformation, object detection, and image processing.
  -	Talked to AI about other things we could add to our project and it introduced the idea of live feedback, behavior tracking (circling, pacing, etc.), and even a heatmap?
Cons:
-	This is definitely a more intermediate to an advanced option, the API is more complex and the syntax can be difficult for beginners to learn.
-	This has more dependencies and brings in native libraries, increasing the build size and setup time


JCodec
- 100% Java
- Supports only mp4, no audio
- Creates BufferedImages
- limited support + not suitable for complex or real-time processing (?)
- Only viable non-native option


OpenIMAJ
Pros:
-	Beginner friendly, the syntax is very simple for loading and iterating video frame
-	Lightweight: There aren’t a huge amount of dependencies that need to be pulled in, making it easier to run and set up
-	Built in frame iterator that makes frame by frame processing easier
-	OpenIMAJ is very well documented, making it very accessible for beginners to learn how to use
Cons:
-	It isn’t as fast or optimized for high-res or high-framerate videos
-	While there is good documentation, the online community is smaller and there aren’t as many tutorials online on how to use it
-	Arent as many advanced features, pretty much just a bare bones frame by frame processing




