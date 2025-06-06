# Use OpenJDK as base image
FROM eclipse-temurin:17-jdk-alpine

# Install Node.js (v18) on Alpine
RUN apk add --no-cache curl nodejs npm

# set directory
WORKDIR /app

# copy jar into app directory
COPY processor/target/centroid-finder-jar-with-dependencies.jar ./centroid.jar

# copy all of server into directory
COPY server ./server

# set directory to server (so docker knows where to find the package.json)
WORKDIR /app/server

# Install npm dependencies
RUN npm install

# Expose the port API is running on
EXPOSE 3000

# Run your start script
CMD ["npm", "run", "start"]