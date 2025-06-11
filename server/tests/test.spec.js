import mock from 'mock-fs';
import path from 'path';
import sinon from 'sinon';
import { expect } from 'chai';
import dataLayer from '../model/processorDataLayer.js';
import { allVideos, thumbnail, jobStatus, processingJob } from '../controllers/processorController.js';
import { baseGetJobStatus, handleCsvFile, baseStartNewProcessingJob } from '../model/jobLogic.js';
import { baseThumbnailGenerator } from '../model/thumbnailLogic.js';
const { getAllVideos, getJobStatus, startNewProcessingJob } = dataLayer;

/** --------------------- CONTROLLER TESTING --------------------- */
describe('Controller Testing', ()=>{
  afterEach(() => {
    sinon.restore();
  })
  /** -------- ALL VIDEOS -------- */
  describe('allVideos', ()=>{
    
    const req = {}
    const res = {
      status: sinon.stub().returnsThis(),
      json: sinon.stub()
    }

    it('Should return list of videos & 200 status code', async()=>{
      sinon.stub(dataLayer, "getAllVideos").returns(['video1.mp4', 'video2.mp4', 'video3.mp4']);

      await allVideos(req, res);

      expect(res.status.calledWith(200)).to.be.true;
      expect(res.json.calledWith(['video1.mp4', 'video2.mp4', 'video3.mp4'])).to.be.true;
    });

    it('Should send 500 status if error occurs', async()=>{
      sinon.stub(dataLayer, "getAllVideos").returns(null);

      await allVideos(req, res);

      expect(res.status.calledWith(500)).to.be.true;
      expect(res.json.calledWith({"error": "Error reading video directory"})).to.be.true;
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
    let req, res;
    beforeEach(() => {
      req = {
        params: {
          jobId: 'id-123'
        }
      }
      res = {
        status: sinon.stub().returnsThis(),
        json: sinon.stub()
      }
    })
    
    it('Should send 200 and JSON with result path', async()=>{
      sinon.stub(dataLayer, "getJobStatus").resolves({
        status: 'done',
        result: 'results/video.csv'
      });

      await jobStatus(req, res);

      expect(res.status.calledWith(200)).to.be.true;
      expect(res.json.calledWith({
        status: 'done',
        result: 'results/video.csv'
      })).to.be.true;
    });

    it('Should send 200 and JSON with processing status', async()=>{
      sinon.stub(dataLayer, "getJobStatus").resolves({
        status: 'processing'
      });

      await jobStatus(req, res);

      expect(res.status.calledWith(200)).to.be.true;
      expect(res.json.calledWith({
        status: 'processing'
      })).to.be.true;
    });

    it('Should send 200 and JSON with error information', async()=>{
      sinon.stub(dataLayer, "getJobStatus").resolves({
        status: 'error',
        error: "Error processing video: Unexpected ffmpeg error"
      });

      await jobStatus(req, res);

      expect(res.status.calledWith(200)).to.be.true;
      expect(res.json.calledWith({
        status: 'error',
        error: "Error processing video: Unexpected ffmpeg error"
      })).to.be.true;
    });

    it('Should send 404 and JSON with error information', async()=>{
      sinon.stub(dataLayer, "getJobStatus").resolves({
        error: "Job ID not found"
      });

      await jobStatus(req, res);

      expect(res.status.calledWith(404)).to.be.true;
      expect(res.json.calledWith({
        error: "Job ID not found"
      })).to.be.true;
    });

    it('Should send 500 and JSON with error information', async()=>{
      sinon.stub(dataLayer, "getJobStatus").resolves({
        error: "Error fetching job status"
      });

      await jobStatus(req, res);

      expect(res.status.calledWith(500)).to.be.true;
      expect(res.json.calledWith({
        error: "Error fetching job status"
      })).to.be.true;
    });
    
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
    sinon.restore();
  })
    /** -------- START NEW PROCESSING JOB -------- */
  describe('startNewProcessingJob', ()=>{
    // set up sinon expressions
    const fakeFs = {
      openSync: sinon.stub().returns(42)
    }

    const fakePath = {
      join: (...args) => args.join('/'),
      resolve: (...args) => args.join('/')
    }

    const fakeUuid = sinon.stub().returns('fake-uuid')

    const processingJobs = new Map()

    const fakeSpawn = sinon.stub().returns({
      pid: 1234,
      unref: sinon.stub()
    })

    afterEach(() => {
      processingJobs.clear();
    })

    it('Return job id if child process starts successfully', ()=>{
      const jobId = baseStartNewProcessingJob(
        'video.mp4',
        'ff00aa',
        '10',
        {
          fs: fakeFs,
          path: fakePath,
          uuidv4: fakeUuid,
          spawn: fakeSpawn,
          processingJobs
        });

        expect(jobId).to.equal('fake-uuid')
        expect(processingJobs.has('fake-uuid')).to.be.true;
    });

    it('Should return null if child process doesnt exist', ()=>{
      const jobId = baseStartNewProcessingJob(
        'video.mp4',
        'ff00aa',
        '10',
        {
          fs: fakeFs,
          path: fakePath,
          uuidv4: fakeUuid,
          spawn: sinon.stub().returns({unref: sinon.stub()}),
          processingJobs
        });

        expect(jobId).to.be.null;
    });
    it('Should return null if error occurs', ()=>{
      const jobId = baseStartNewProcessingJob(
        'video.mp4',
        'ff00aa',
        '10',
        {
          fs: fakeFs,
          path: fakePath,
          uuidv4: fakeUuid,
          spawn: sinon.stub().throws(new Error('test error')),
          processingJobs
        });

        expect(jobId).to.be.null;
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
      join: (...args) => args.join('/'),
      resolve: (...args) => args.join('/')
    }
    afterEach(() => {
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
        path: {resolve: () => 'csvFilePath'},
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
        path: {resolve: () => 'csvFilePath'},
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
    const makeFakeFfmpeg = ({ succeed = true } = {}) => {
    const cmd = {
      screenshots: sinon.stub().returnsThis(),
      on(event, cb) {
        if (event === (succeed ? 'end' : 'error')) {
          setImmediate(() => {
            succeed ? cb() : cb(new Error('FFmpeg boom'));
          });
        }
        return this;
      },
    };
    return sinon.stub().returns(cmd);
  };
    afterEach(() => {
      sinon.restore();
    });

  it('resolves path on success', async () => {
    const ffmpegStub = makeFakeFfmpeg({ succeed: true });
    const videoPath = path.resolve('../public/videos/test.mp4');
    const fileName = 'test';
    process.env.THUMBNAIL_PATH = path.resolve('/public/thumbnails');
    const expectedOutput = path.join(process.env.THUMBNAIL_PATH, fileName+'-thumbnail.png');



    const result = await baseThumbnailGenerator(videoPath, fileName, {
      path,
      ffmpeg: ffmpegStub
    });

    // Assertions
    expect(result).to.equal(expectedOutput);
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
