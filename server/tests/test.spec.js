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

