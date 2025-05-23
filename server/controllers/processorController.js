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
export const thumbnail = async(req, res) => {
    // get filename from path params
    const filename = req.params.filename;
    // call model to get the video path
    const filepath = dataLayer.getVideoPath(filename);
    //  error if filename isnt found in model
    if(!filepath) res.status(500).json({
        "error": `Error finding ${filename} in video folder.`
    });
    // if exists send back path for thumbnail extraction
    
    // get thumbnail <--- somehow??
    // 200: OK, return the thumbnail
    // 500: Error generating thumbnail

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
    // get query params --> targetColor, threshold

    // make asynchronous call to analyze video
        // returns a jobID we can poll
    
    // 202 - Accepted: return the job id
    // 400 - Bad Request:  "Missing targetColor or threshold query parameter."
    // 500 - Internal Server Error: "Error starting job"
}