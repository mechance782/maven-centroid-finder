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
- Use postman to check that web API routes are still working as expected


How will you make sure the endpoints are available outside the image?
- Using EXPOSE <port> we will make the necessary ports available outside the image
- When going to run, make sure that we include:
docker run -p 3000:3000 appname:latest (USED AI HERE)
- check at localhost:3000 to make sure we can connect to it

How will your code know where to access the video/results directory? Hint: environment variables and volumes. (We'll talk about volumes on Thursday)
- We're storing our file paths in a .env file. Volume is just a command that mounts the local file to the docker container. This means that the file is being shared between your machine and the container, so it keeps the .env seperate from source code, and allows us to edit it from the machine and see the changes in the container.

How can you make you docker image small, cacheable, and quick to make changes to?
- ??? also not entirely sure