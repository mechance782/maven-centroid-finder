// test/add.spec.js
import { expect } from 'chai';
import { add }   from '../model/processorDataLayer.js';

describe('add()', () => {
  it('adds two numbers', () => {
    expect(add(2, 3)).to.equal(5);
  });
});
