import path from 'path';
import fs from 'fs';
import ffmpeg from 'fluent-ffmpeg';
import { timeStamp } from 'console';
import { spawn } from 'node:child_process';
import { v4 as uuidv4 } from 'uuid';

// Create or import Map to track child processes here
const processingJobs = new Map();

// CREATE

export const add = (a, b) => a + b;

// expected args: filename, targetcolor, threshold
const startNewProcessingJob = (filename, targetColor, threshold) => {
    const jarPath = path.join(import.meta.dirname + '/../..' + process.env.JAR_PATH);
    const videoPath = path.join(import.meta.dirname + '/..' + process.env.VIDEO_PATH + '/' + filename);
    const outputcsv = filename + ".csv";
    // spawn child process using args
    try {
        const job = spawn('java', ['-jar', jarPath, videoPath, outputcsv, targetColor, threshold], {
            detached: true,
            stdio: 'ignore',
        })

        job.unref();

        // if spawn event fires, store process in map
        const jobId = uuidv4();
        job.on("spawn", () => {
            processingJobs.set(jobId, job);
            // console.log(processingJobs.get(jobId));
            
        });

        // check if child process exists
        if (job.pid) return jobId;
        return null;
    } catch (err) {
        return null;
    }
    
}



// READ

// getJobStatus (jobId)
const getJobStatus = (jobId) => {
    // get child process from map using job id
    // check for errors
    // check if process is running
    // if process closes, move created csv file into public/results
    // return current status and any resulting errors or results
}


// getAllVideos
const getAllVideos = () => {
    // use .env file path to find video folder
    const videoFolderPath = path.join(import.meta.dirname + '/..' + process.env.VIDEO_PATH);
    // take all file names in folder and add to an array

    try {
        const videoList = fs.readdirSync(videoFolderPath);
        
        return videoList;
    } catch (error) {
        console.log("Error reading files in video folder: " + error);
        return null;
    }
}


// getThumbnail (filename)
const generateThumbnail = async (filepath, filename) => {
    // grab the output file path for thumbnails
    const outputPath = process.env.THUMBNAIL_PATH;

    try{
        Ffmpeg(filepath).screenshots({
        timestamps: [1],
        filename: `${filename}-thumbnail.jpg`,
        folder: outputPath
        });
    } catch(e){
        e.message=("Error generating thumbnail: ", e);
        e.status =(500);
        throw e;
    }
}

const getThumbnail = (filename) => {
    const thumbnailFolderPath = path.join(import.meta.dirname + '/..' + process.env.THUMBNAIL_PATH);
    let thumbnailList;
    try{
        thumbnailList = fs.readdirSync(thumbnailFolderPath);
    } catch (e){
        e.message=("Error reading thumbnails: ", e);
        e.status =(500);
        throw e;
    }

    for(thumbnail in thumbnailList){
        let filenameArray = thumbnail.split('-thumbnail.jpg');
        let file = filenameArray[0];

        if(file == filename){
            return thumbnail;
        }
        else{
            generateThumbnail(filename);
            return process.env.THUMBNAIL_PATH + filename + '-thumbnail.jpg';
        }
    }   
}

const getVideoPath = (filename) => {
    // call get all videos to get a list of videos
    const videoList = getAllVideos();
    // check if filename is included in the array (and that a videolist exists)
    if(videoList && videoList.includes(filename)){
        // return a string concatenation of the filepath if found
        return process.env + filename;
    } else{
        console.log(`${filename} does not exist in videos folder.`);
        return null;
    }
}

export default {getAllVideos, getJobStatus, getThumbnail, startNewProcessingJob, getVideoPath, add}