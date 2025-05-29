import mock from 'mock-fs';
import sinon from 'sinon';
import { expect } from 'chai';
import dataLayer from '../model/processorDataLayer.js';
import { allVideos, thumbnail, jobStatus, processingJob } from '../controllers/processorController.js';
import { baseGetJobStatus, handleCsvFile } from '../model/jobLogic.js';
import { mkdir } from 'node:fs';
import path from 'path';
import * as fs from 'fs';
import * as ffmpegNs from 'fluent-ffmpeg';
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
    const processingJobs = new Map();
    const fakeFs = {
      existsSync:sinon.stub(),
      mkdirSync: sinon.stub(),
      renameSync: sinon.stub()
    }

    const fakeFsPromises = {
      unlink: sinon.stub().resolves()
    }

    const fakePath = {
      join: (...args) => args.join('/')
    }

    afterEach(() => {
      sinon.restore();
      processingJobs.clear();
    })

    it('Should return error if no process w/ job ID exists', async ()=>{
      const result = await baseGetJobStatus('bad-id', {
        processingJobs,
        fs: fakeFs,
        FS: fakeFsPromises,
        path: fakePath,
        waitForLogContent: async () => '',
        handleCsvFile
      })

      expect(result).to.deep.equal({error: 'Job ID not found'})

    });
    it('Should return status of processing if job is still active', async ()=>{
      processingJobs.set('id-123', {
        csvFile: 'job.csv',
        logFilePath: 'log.txt'
      });

      fakeFs.existsSync.withArgs('log.txt').returns(true);
      fakeFs.existsSync.withArgs('job.csv').returns(false);
      

      const result = await baseGetJobStatus('id-123', {
        processingJobs,
        fs: fakeFs,
        FS: fakeFsPromises,
        path: fakePath,
        waitForLogContent: async () => '',
        handleCsvFile: () => {}
      })

      expect(result.status).to.equal('processing');

    });
    it('Should return status of error if output logs contain an error', async ()=>{
      processingJobs.set('id-123', {
        csvFile: 'job.csv',
        logFilePath: 'log.txt'
      });

      fakeFs.existsSync.withArgs('log.txt').returns(true);
      fakeFs.existsSync.withArgs('job.csv').returns(false);

      const result = await baseGetJobStatus('id-123', {
        processingJobs,
        fs: fakeFs,
        FS: fakeFsPromises,
        path: fakePath,
        waitForLogContent: async () => 'error',
        handleCsvFile: () => {}
      })

      expect(result.status).to.equal('error');
      expect(result.error).to.equal('Error processing video: Unexpected ffmpeg error')
    });
    it('Should return state of done if csv is found in results folder', async ()=>{
      processingJobs.set('id-123', {
        csvFile: 'job.csv',
        logFilePath: 'log.txt'
      });

      fakeFs.existsSync.withArgs('log.txt').returns(true);
      fakeFs.existsSync.withArgs('job.csv').returns(true);
      fakeFs.existsSync.withArgs('csvFilePath').returns(true);

      const result = await baseGetJobStatus('id-123', {
        processingJobs,
        fs: fakeFs,
        FS: fakeFsPromises,
        path: {join: () => 'csvFilePath'},
        waitForLogContent: async () => '',
        handleCsvFile: () => { return {
            "status": "done",
            "result": "/results/jobs.csv"
        }}
      })

      expect(result.status).to.equal('done');
      expect(result.result).to.equal('/results/jobs.csv');
    });
    it('Should throw error if job status cant be found', async ()=>{

      processingJobs.set('id-123', {
        csvFile: 'job.csv',
        logFilePath: 'log.txt'
      });

      fakeFs.existsSync.withArgs('log.txt').returns(true);
      fakeFs.existsSync.withArgs('job.csv').returns(true);
      fakeFs.existsSync.withArgs('csvFilePath').returns(true);

      const result = await baseGetJobStatus('id-123', {
        processingJobs,
        fs: fakeFs,
        FS: fakeFsPromises,
        path: {join: () => 'csvFilePath'},
        waitForLogContent: async () => '',
        handleCsvFile: () => { return {
            "error": "Error fetching job status"
        }}
      })

      expect(result.error).to.equal("Error fetching job status")
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
  describe('generateThumbnail', () => {
  const THUMB_DIR = path.resolve('test/tmp/thumbs');
  const filename  = 'demo';
  const videoPath = '/videos/demo.mp4';
  const output    = path.join(THUMB_DIR, `${filename}-thumbnail.png`);

  /** stub fluent-ffmpeg BEFORE importing the service */
  before(async () => {
    process.env.THUMBNAIL_PATH = THUMB_DIR;

    sinon.stub(fs, 'mkdir').resolves();

    const fakeProcessor = {
      screenshots: sinon.stub().returnsThis(),
      on(event, cb) {            // store callbacks so tests can trigger them
        this[`_${event}`] = cb;
        return this;
    }
};

    // Detect whether fluent-ffmpeg is `default` or the fn itself
    const ffmpegExport = ffmpegNs.default;
    sinon.replace(ffmpegExport, sinon.stub().callsFake(() => fakeProcessor));

    ({ generateThumbnail } = await import('../model/processorDataLayer.js'));
  });

  after(() => {
    sinon.restore();
    delete process.env.THUMBNAIL_PATH;
  });

  it('resolves path on success', async () => {
    
  });

  it('rejects with status 500 on error', async () => {

  });
})
});
  /** --------  GET VIDEO PATH -------- */
  describe('getVideoPath', ()=>{
    it('Should return video path', ()=>{

    });
    it('Should return null if no video path found', ()=>{

    });
})
