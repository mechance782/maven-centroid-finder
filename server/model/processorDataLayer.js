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
    // take all file names in folder and add to an array
    // return array
}


// getThumbnail (filename)
const getThumbnail = (filename) => {
    // make ffmpeg with .env file path + filename 
    // use .screenshots on ffmpeg object
    // name screenshot and place in public folder
    // return filepath to jpeg thumbnail
}

