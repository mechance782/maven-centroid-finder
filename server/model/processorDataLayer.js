import path from 'path';
import { promises as FS } from 'fs'; 
import fs from 'fs';
import ffmpeg from 'fluent-ffmpeg';
import ffmpegInstall from '@ffmpeg-installer/ffmpeg';
import { timeStamp } from 'console';
import { spawn } from 'node:child_process';
import { v4 as uuidv4 } from 'uuid';
import base from './jobLogic.js'
const {baseGetJobStatus, baseStartNewProcessingJob, waitForLogContent, handleCsvFile} = base;


ffmpeg.setFfmpegPath(ffmpegInstall.path);

const THUMB_DIR = path.resolve(process.env.THUMBNAIL_PATH || 'public/thumbnails');
// Create or import Map to track child processes here
const processingJobs = new Map();

// CREATE

export const add = (a, b) => a + b;

// expected args: filename, targetcolor, threshold
// outputCsv is generated in server folder
const startNewProcessingJob = (filename, targetColor, threshold) => {
    // create paths and filenames
    const jarPath = path.join(process.cwd(), '..', process.env.JAR_PATH);
    const videoPath = path.join(process.cwd(), process.env.VIDEO_PATH, filename);
    const outputcsv = filename + ".csv";
    // create log file
    const logFilePath = path.join('/tmp', `${filename}-${Date.now()}.log`);
    const logFile = fs.openSync(logFilePath, 'a');
   
    try {
        // spawn child process using args
        // process is seperated from main app, 
        // and output is redirected to log file
        const job = spawn('java', ['-jar', jarPath, videoPath, outputcsv, targetColor, threshold], {
            detached: true,
            stdio: ['ignore', logFile, logFile],
        })

        job.unref();

        //store process information in map with uuid and correlating csv filename
        const jobId = uuidv4();
        processingJobs.set(jobId, {csvFile: outputcsv, logFilePath});

        // check if child process exists then return id
        if (job.pid) return jobId;
        return null;
    } catch (err) {
        console.log("Error starting child process: ", err);
        return null;
    }
    
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
    const videoFolderPath = path.join(process.cwd(), process.env.VIDEO_PATH);
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
    await FS.mkdir(THUMB_DIR, {recursive: true});
    
    const outputPath = path.join(THUMB_DIR, `${filename}-thumbnail.png`);
    
    return new Promise((resolve, reject) => {
        ffmpeg(videopath)
        .screenshots({
        timestamps: [1],
        filename: path.basename(outputPath),
        folder: process.env.THUMBNAIL_PATH
        })
        .on('end', () => resolve(outputPath))
        .on('error', e => {
            console.error('FFmpeg error: ', e);
            e.message=("Error generating thumbnail: ", e);
            e.status =(500);
            reject(e);
        });
    }); 
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

const getThumbnail = async(videoPath, filename) => {

    console.log(thumbnailFolderPath + "generating thumbnail");
    try{
        await fs.mkdir(process.env.THUMBNAIL_PATH, { recursive: true });

        thumbnailList = await fs.readdir(process.env.THUMBNAIL_PATH);
        console.log("Thumbnail list: ", thumbnailList);
        const thumb = thumbnailList.find( f=> f.endsWith(ending) && f.slice(0, -ending.length) === filename);
        console.log("Thumb: " + thumb);
        if(thumb){
            console.log(`Found existing thumbnail: ${thumb}`);
            return path.join(process.env.THUMBNAIL_PATH, thumb); 
        } 

        console.log(`Generating thumbnail for ${filename}`);
        return await generateThumbnail(videoPath, filename);
    } catch (e){
        console.log("Error getting thumbnail, in getThumbnail", e);
        e.status =(500);
        throw e;
    }
}

const getVideoPath = (filename) => {
    // call get all videos to get a list of videos
    const videoList = getAllVideos();
    // check if filename is included in the array (and that a videolist exists)
    if(videoList && videoList.includes(filename)){
        // return a string concatenation of the filepath if found
        return path.join('.',process.env.VIDEO_PATH + filename);
    } else{
        console.log(`${filename} does not exist in videos folder.`);
        return null;
    }
}

export default {getAllVideos, getJobStatus, generateThumbnail, startNewProcessingJob, getVideoPath, add, getThumbnail}