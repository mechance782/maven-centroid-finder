// test/add.spec.js
import { expect } from 'chai';
import { add }   from '../model/processorDataLayer.js';

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
    it('Should return a list of all videos', ()=>{

    });
    it('Should return null if no videos found', ()=>{

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
