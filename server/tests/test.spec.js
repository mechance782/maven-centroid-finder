import mock from 'mock-fs';
import { expect } from 'chai';
import dataLayer from '../model/processorDataLayer.js';
import { allVideos, thumbnail, jobStatus, processingJob } from '../controllers/processorController.js';
const { getAllVideos, getJobStatus, startNewProcessingJob, add } = dataLayer;

describe('add()', () => {
  it('adds two numbers', () => {
    expect(add(2, 3)).to.equal(5);
  });
});

/** --------------------- CONTROLLER TESTING --------------------- */
describe('Controller Testing', ()=>{
  /** -------- ALL VIDEOS -------- */
  describe('allVideos', ()=>{
    it('Should return list of videos & 200 status code', async()=>{

    });
    it('Should send 500 status if error occurs', async()=>{

    });
  });
  /** -------- GET THUMBNAIL -------- */
  describe('thumbnail', ()=>{
    it('Should send file and 200 status code if successful', async()=>{

    });
    it('Should throw e if error occurs', async()=>{

    });
  });
  /** -------- GET JOB STATUS -------- */
  describe('jobStatus', ()=>{
    it('Should send 200 and JSON with job info', async()=>{

    });
    //TODO: Figure out what else this method should do
  });
  /** --------  POST PROCESSING JOB -------- */
  describe('processingJob', ()=>{
    it('Should send 400 if missing/bad params', async()=>{

    });
    it('Should send 500 if error occurs', async()=>{

    });
    it('Should send 202 w/ job ID', async()=>{

    });
  });
});


/** --------------------- MODEL TESTING --------------------- */
describe('Model Testing', ()=> {
  // restore mock file system after each test
  afterEach(() => {
    mock.restore();
  })
    /** -------- START NEW PROCESSING JOB -------- */
  describe('startNewProcessingJob', ()=>{
    it('Return job id if child process exists', ()=>{

    });
    it('Should return null if child process doesnt exist', ()=>{

    });
    it('Should return null if error occurs', ()=>{

    });
  });
  /** -------- GET JOB STATUS -------- */
  describe('getJobStatus', ()=>{
    it('Should return error if no process w/ job ID exists', ()=>{

    });
    it('Should return status of processing if job is still active', ()=>{

    });
    it('Should return error if exit code doesnt equal 0', ()=>{

    });
    it('Should return state of done if found in results folder', ()=>{

    });
    it('Should throw error if job status cant be found', ()=>{

    });
  });
  /** -------- GET ALL VIDEOS -------- */
  describe('getAllVideos', ()=>{
    // set up
    beforeEach(() => {
      process.env.VIDEO_PATH = 'videos';
    })
    // clean up
    afterEach(() => {
      delete process.env.VIDEO_PATH;
    })

    it('Should return a list of all videos', ()=>{
      mock({
        'videos': {
          'video1.mp4': Buffer.from('fake mp4 content 1'),
          'video2.mp4': Buffer.from('fake mp4 content 2'),
          'video3.mp4': Buffer.from('fake mp4 content 3'),
        }
      })
      const result = getAllVideos();
      expect(result).to.be.an('array').with.lengthOf(3);
      expect(result).to.include.members(['video1.mp4', 'video2.mp4', 'video3.mp4']);

    });

    it('Should return empty array if no videos found', ()=>{

      mock({
        'videos': {
        }
      })
      const result = getAllVideos();
      expect(result).to.be.an('array').with.lengthOf(0);
    });

    it('Should return null if error occurs', ()=>{
      const result = getAllVideos();
      expect(result).to.be.null;
    });
  });
  /** --------  GENERATE NEW THUMBNAIL -------- */
  describe('generateThumbnail', ()=>{
    it('Should generate new thumbnail in thumbnail folder if successful', async()=>{

    });
    it('Should throw error if an error occurs', async()=>{

    });
  });
  /** --------  GET THUMBNAIL -------- */
  describe('getThumbnail', ()=>{
    it('Should return thumbnail path if thumbnail already exists', async()=>{

    });
    it('Should generate a thumbnail if no thumbnail is found', async()=>{

    });
    it('Should throw error if an error occurs', async()=>{

    });
  });
  /** --------  GET VIDEO PATH -------- */
  describe('getVideoPath', ()=>{
    it('Should return video path', ()=>{

    });
    it('Should return null if no video path found', ()=>{

    });
  });
});
