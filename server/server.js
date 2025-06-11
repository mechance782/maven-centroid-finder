import express from 'express';
import cors from 'cors';
import router from './routes/processorRouter.js'
import dotenv from 'dotenv';
import path from 'path';

dotenv.config({
    path: import.meta.dirname + '/../.env'
});


const app = express();
const PORT = 3000;

// Allows this server to accept requests from different domains (origins)
app.use(cors());

// Set the view to public folder
app.use(express.static('public'));
if (process.env.RESULTS_PATH && process.env.VIDEO_PATH){
   app.use('/results', express.static(path.resolve(process.env.RESULTS_PATH)));
   app.use('/videos', express.static(path.resolve(process.env.VIDEO_PATH)));
}




// This allows us to read in JSON payloads from the req body
app.use(express.json());

app.use("/", router);

app.listen(PORT, console.log(`Server started on http://localhost:${PORT}`));