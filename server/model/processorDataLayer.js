import path from 'path';
import fs from 'fs';
// Create or import Map to track child processes here

// CREATE


const startNewProcessingJob = (args) => {
    // spawn child process using args
    // if spawn event fires, create jobId and store process in map
    // if not then handle 505 error
    // return jobId
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
const getThumbnail = (filename) => {
    // make ffmpeg with .env file path + filename 
    // use .screenshots on ffmpeg object
    // name screenshot and place in public folder
    // return filepath to jpeg thumbnail
}

const getVideoPath = (filename) => {
    // call get all videos to get a list of videos
    const videoList = getAllVideos();
    // check if filename is included in the array (and that a videolist exists)
    if(videoList && videoList.includes(filename)){
        // return a string concatenation of the filepath if found
        return './public/videos/' + filename;
    } else{
        console.log(`${filename} does not exist in videos folder.`);
        return null;
    }
}

export default {getAllVideos, getJobStatus, getThumbnail, startNewProcessingJob, getVideoPath}