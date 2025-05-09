Native dependency:
- written in low-level languages and compiled for specific operating systems 
- can cause portability issues when running on different systems, creates larger build size, and native errors are harder to understand
- Native libraries are faster, and have more extensive features. 
- Apparently many video processing libraries are native.

JavaCV: 
- https://javadoc.io/doc/org.bytedeco/javacv/latest/index.html (look at Frame Class and Java2DFrameConverter )
- Native (in Maven, all native binaries are included, so it works cross-platform)
- Two libraries combined into one (FFmpeg + OpenCV)
- handles multiple video formats
- easily creates BufferedImages

JCodec
- 100% Java
- Supports only mp4, no audio
- Creates BufferedImages
- limited support + not suitable for complex or real-time processing (?)
- Only viable non-native option



