// expected args: filename, targetcolor, threshold
// outputCsv is generated in server folder
export const baseStartNewProcessingJob = (filename, targetColor, threshold, {
    fs,
    path,
    uuidv4,
    spawn,
    processingJobs
}) => {
    // create paths and filenames
    const jarPath = path.resolve(process.env.JAR_PATH || '../processor/target/centroid-finder-jar-with-dependencies.jar');
    const videoDir = path.resolve(process.env.VIDEO_PATH || './public/videos');
    const videoPath = path.join(videoDir, filename);
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


export const baseGetJobStatus = async (jobId, {
    processingJobs, 
    fs, 
    FS, 
    path,
    waitForLogContent, 
    handleCsvFile
}) => {
    // if jobId isn't stored in the map, return error
    if (!processingJobs.has(jobId)){
        return {
            "error": "Job ID not found"
        }
    } 
    // get processing job information using jobId
    const {csvFile, logFilePath} = processingJobs.get(jobId);

    // check for errors in logfile
    if (fs.existsSync(logFilePath)){
        try {
            const outputLogs = await waitForLogContent(FS, logFilePath);
            // check for errors and exceptions in log
            if (outputLogs.toLowerCase().includes('error') || outputLogs.toLowerCase().includes('exception')){
                
                console.error(outputLogs);
                return {
                    "status": "error",
                    "error": "Error processing video: Unexpected ffmpeg error"
                }
            }

            // clean up log file which is now unnecessary 
            try {
                await FS.unlink(logFilePath);
            } catch (err) {
                console.log("Error: failed to delete log file ", err)
            }
            
        } catch (err){
            console.log(err);
        }   
    }

    // path to expected csv file and path to it's new destination folder
    const csvFilePath = path.resolve(csvFile);
    const csvFolder = path.resolve(process.env.RESULTS_PATH || './public/results');
    const csvDestination = path.resolve(csvFolder + '/' + csvFile);


    // if csv file is generated, move it, then return status + results
    if (fs.existsSync(csvFilePath)){
        return handleCsvFile(csvFolder, csvFilePath, csvFile, fs);
    } else if (fs.existsSync(csvDestination)) {
        return {
            "status": "done",
            "result": "/results/" + csvFile
        }
    }

    // if no errors and no csv file, then status is processing
    return {
        "status": "processing"
    }
}

// Helper method for getJobStatus
// handles fs logic for moving generated csv to correct directory
// returns object with status and csv file path or with error message
export const handleCsvFile = (csvFolder, csvFilePath, csvFile, fs) => {
    try {
        
        const stats = fs.statSync(csvFilePath);

        if (stats.size <= 0) {
            return {
                "status": "processing"
            }
        }
        
        
        // if the csv results directory does not exist yet, then make it
        if (!fs.existsSync(csvFolder)){
            fs.mkdirSync(csvFolder, {recursive: true});
        }

        // get csv file and move it to results directory
        fs.renameSync(csvFilePath, csvFolder + '/' + csvFile);
        
        return {
            "status": "done",
            "result": "/results/" + csvFile
        }
        
    } catch (err){
        console.error("Error moving csv file:", err);
        return {
            "error": "Error fetching job status"
        }
    }
}

// helper method for getJobStatus
// ensures the log file is read AFTER jar output is flushed and the log file has content
const waitForLogContent = async (FS, filePath, maxRetries = 5, delay = 500) => {

    // try to read the log file every 500 ms
    // after 5 tries, throw error
    for (let i = 0; i < maxRetries; i++) {
        try {
            const content = await FS.readFile(filePath, 'utf8');
            if (content.trim().length > 0) return content;
        } catch (err) {
            // File may not be created yet, so ignore the error for now
        }
        await new Promise(res => setTimeout(res, delay));
    }
    throw new Error("Log file is still empty after retries");
};

export default {baseGetJobStatus, baseStartNewProcessingJob, waitForLogContent, handleCsvFile}