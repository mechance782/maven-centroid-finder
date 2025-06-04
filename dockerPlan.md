What base docker image will you use? (Do research online and with AIs!) - Used AI for this!
Option 1: Use an official Java image (like openjdk) and install Node.js on top.

Option 2: Use an official Node.js image and install Java on top.

Option 3: Use a minimal Linux base (like debian or alpine) and install both Java and Node.js yourself.

How will you make sure that both node and Java can run in your image?
- If we install node.js alonsdie java in the dockerfile then we can run both.
- Running a installation verification as simple as node -v and java -version will help.

How will you test your Dockerfile and image?
- Build the image locally, run a container and check the node and java versions
- Start the container and run our app, then make sure we can connect to that image
- I have also seen some things about using ContainerStructureTest created by Google to test the structure of a container image?
How will you make sure the endpoints are available outside the image?
- 
How will your code know where to access the video/results directory? Hint: environment variables and volumes. (We'll talk about volumes on Thursday)
How can you make you docker image small, cacheable, and quick to make changes to?