import path from 'path';
import { promises as FS } from 'fs'; 
import fs from 'fs';
import ffmpeg from 'fluent-ffmpeg';
import ffmpegInstall from '@ffmpeg-installer/ffmpeg';
import { spawn } from 'node:child_process';
import { v4 as uuidv4 } from 'uuid';
import base from './jobLogic.js'
import Base from './thumbnailLogic.js'
const {baseGetJobStatus, baseStartNewProcessingJob, waitForLogContent, handleCsvFile} = base;
const {baseThumbnailGenerator} = Base;


ffmpeg.setFfmpegPath(ffmpegInstall.path);

const THUMB_DIR = path.resolve(process.env.THUMBNAIL_PATH || './public/thumbnails');
// Create or import Map to track child processes here
const processingJobs = new Map();

// CREATE

export const add = (a, b) => a + b;

// expected args: filename, targetcolor, threshold
// outputCsv is generated in server folder
const startNewProcessingJob = (filename, targetColor, threshold) => {
    return baseStartNewProcessingJob(filename, targetColor, threshold, {
        fs,
        path,
        uuidv4,
        spawn,
        processingJobs
    })
}


const getJobStatus = async (jobId) => {
    return await baseGetJobStatus(jobId, {
        processingJobs,
        fs,
        FS,
        path,
        waitForLogContent,
        handleCsvFile
    })
}


// getAllVideos
const getAllVideos = () => {
    // use .env file path to find video folder
    const videoFolderPath = path.resolve(process.env.VIDEO_PATH || './public/videos');
    // take all file names in folder and add to an array

    try {
        const videoList = fs.readdirSync(videoFolderPath);
        
        return videoList;
    } catch (error) {
        console.log("Error reading files in video folder: ", error);
        return null;
    }
}

// ffmpeg.getAvailableFormats((err) => {
//   if (err) console.error('FFmpeg still not found:', err);
//   else     console.log('FFmpeg detected, ready to transcode.');
// });



// getThumbnail (filename)
 const generateThumbnail = async (videopath, filename) => {
    return await baseThumbnailGenerator(videopath, filename, {
        fs,
        path,
        ffmpeg
    })
 }

    // https://www.mux.com/articles/extract-thumbnails-from-a-video-with-ffmpeg
    // const outpath = path.join(process.env.THUMBNAIL_PATH, `${filename}-thumbnail.png`);
    // const command = `ffmpeg -y -ss 1 -i "${videopath}" -frames:v 1 -vf scale=320:-1 "${outpath}"`;

    // exec(command, (err) => {
    //     if(err){
    //         return res.status(500).json({error: `Error generating thumbnail`});
    //     }
    //     res.sendFile(path.resolve(outpath));
    // });
// }

const getVideoPath = (filename) => {
    // call get all videos to get a list of videos
    const videoList = getAllVideos();
    // check if filename is included in the array (and that a videolist exists)
    if(videoList && videoList.includes(filename)){
        // return a string concatenation of the filepath if found
        const videoDir = path.resolve(process.env.VIDEO_PATH || './public/videos');

        return path.join(videoDir, filename);
    } else{
        console.log(`${filename} does not exist in videos folder.`);
        return null;
    }
}

export default {getAllVideos, getJobStatus, generateThumbnail, startNewProcessingJob, getVideoPath, add}