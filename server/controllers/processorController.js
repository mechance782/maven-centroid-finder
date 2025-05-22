// get all videos
export const allVideos = async(req, res) => {
    
}

// get thumbnail
export const thumbnail = async(req, res) => {
    // get filename from query params


}
// get job status
export const jobStatus = async(req, res) => {
    // get the job id from the path parameters

    // call model folder to get the  processing status
        // if finished, send an HTTP 200 OK response containing a JSON payload with jobId, status and csv file path
        // if not, send a 202: accepted response with jobId and status

}

// post processing job
export const processingJob = async(req, res) => {

}