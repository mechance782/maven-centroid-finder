// router
import express from 'express';
import controller from './../controllers/processorController.js';

const router = express.Router();

// mount controller functions to router
router.get("/api/videos", controller.allVideos);
router.get("/thumbnail/:filename", controller.thumbnail);
router.get("/process/:jobId/status", controller.jobStatus);
router.post("/process/:filename", controller.processingJob);

export default router;