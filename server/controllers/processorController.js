import dataLayer from '../model/processorDataLayer.js';

// get all videos
export const allVideos = async(req, res) => {
    // call model folder to get all the videos in the folder
    const list = dataLayer.getAllVideos();
    // 200: return all videos as a json payload ??? <-- not too sure about it being a json payload
    if (list){
        res.status(200).json(list);
    } else {
        // 500: Error reading video directory
        res.status(500).json({
            "error": "Error reading video directory"
        })
    }

}

// get thumbnail
// referenced this article on how to use ffmpeg
// https://mohammedshamseerpv.medium.com/generate-thumbnails-from-videos-in-node-js-using-fluent-ffmpeg-62583d1c2e61
export const thumbnail = async(req, res) => {
    // get filename from path params
    const filename = req.params.filename;
    try{
        const videoPath =  await dataLayer.getVideoPath(filename);
        if(!videoPath) return res.status(404).json({error: 'Video not found.'});

        console.log(`Getting thumbnail of ${videoPath} and ${filename}`);
        const thumbnailPath = await dataLayer.generateThumbnail(videoPath, filename);

        res.setHeader('Cache-Control', 'public, max-age=86400');
        res.status(200).sendFile(thumbnailPath, err => err && nextTick(err));
    } catch (e){
        throw e;
    }
    

}
// get job status
export const jobStatus = async(req, res) => {
    // get the job id from the path parameters

    // call model folder to get the  processing status
        // if finished, send an HTTP 200 OK response containing a JSON payload with jobId, status and csv file path
        // 202: accepted response with jobId and status

}

// post processing job
export const processingJob = async(req, res) => {
    // get path param --> filename
    const filename = req.params.filename;
    // get query params --> targetColor, threshold
    if (!(req.query.targetColor && req.query.threshold)){
        // 400 - Bad Request:  "Missing targetColor or threshold query parameter."
        res.status(400).json({
            "error": "Mssing targetColor or threshold query parameter."
        })
    }
    const targetColor = req.query.targetColor;
    const threshold = req.query.threshold;
    // make asynchronous call to analyze video
        // returns a jobID we can poll
    const jobId = dataLayer.startNewProcessingJob(filename, targetColor, threshold);

    if (jobId){
       // 202 - Accepted: return the job id 
       res.status(202).json({
        "jobsId": jobId
       })
    } else {
        res.status(500).json({
            "error": "Error starting job"
        })
    }
    
    
    // 500 - Internal Server Error: "Error starting job"
}