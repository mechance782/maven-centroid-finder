// router
import express from 'express';
import controller from './../controllers/processorController.js';

const router = express.Router();

// mount controller functions to router
router.get("/api/videos", controller.videos);
router.get("/thumbnail/:filename", controller.thumbnail);
router.get("/process/:jobId/status", controller.status);
router.post("/process/:filename", controller.process);

export default router;