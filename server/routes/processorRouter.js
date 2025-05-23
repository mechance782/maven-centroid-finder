// router
import express from 'express';
import {allVideos, thumbnail, jobStatus, processingJob} from './../controllers/processorController.js';

const router = express.Router();

// mount controller functions to router
router.get("/api/videos", allVideos);
router.get("/thumbnail/:filename", thumbnail);
router.get("/process/:jobId/status", jobStatus);
router.post("/process/:filename", processingJob);

export default router;