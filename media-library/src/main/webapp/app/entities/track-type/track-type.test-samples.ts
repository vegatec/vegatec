import { ITrackType, NewTrackType } from './track-type.model';

export const sampleWithRequiredData: ITrackType = {
  id: 10740,
  name: 'via',
};

export const sampleWithPartialData: ITrackType = {
  id: 7133,
  name: 'file',
  vipCreditsNeeded: 12370,
};

export const sampleWithFullData: ITrackType = {
  id: 30791,
  name: 'woot including',
  creditsNeeded: 11516,
  vipCreditsNeeded: 32214,
};

export const sampleWithNewData: NewTrackType = {
  name: 'yak intimidate gymnastics',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
