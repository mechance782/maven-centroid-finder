# Use OpenJDK as base image
FROM eclipse-temurin:17-jdk-alpine

# Install Node.js (v18) on Alpine
RUN apk add --no-cache curl nodejs npm

# Expose the port API is running on
EXPOSE 3000

