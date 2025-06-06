# Use Debian-based Eclipse Temurin JDK 17
FROM eclipse-temurin:17-jdk

# Install curl, Node.js (v18), and npm
RUN apt-get update && apt-get install -y curl gnupg && \
    curl -fsSL https://deb.nodesource.com/setup_18.x | bash - && \
    apt-get install -y nodejs && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# set directory
WORKDIR /app

# copy jar into app directory
COPY processor/target/centroid-finder-jar-with-dependencies.jar ./centroid.jar

# copy package json so we can install node packages before anything else
# good for caching since npm install won't have to run on every build now
# means faster builds
COPY server/package*.json ./server/

WORKDIR /app/server
# omit=dev tag will ignore dev dependencies in package json
RUN npm install --omit=dev

# back to main directory
WORKDIR /app



# set env vars 
ENV VIDEO_PATH=/videos
ENV RESULTS_PATH=/results
ENV THUMBNAIL_PATH=/thumbnails
ENV JAR_PATH=/app/centroid

# copy all of server into directory
COPY server ./server

# set directory to server for run command
WORKDIR /app/server

# Expose the port API is running on
EXPOSE 3000

# Run your start script
CMD ["npm", "run", "start"]