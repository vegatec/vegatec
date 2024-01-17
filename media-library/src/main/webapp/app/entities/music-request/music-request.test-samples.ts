import dayjs from 'dayjs/esm';

import { IMusicRequest, NewMusicRequest } from './music-request.model';

export const sampleWithRequiredData: IMusicRequest = {
  id: 16071,
};

export const sampleWithPartialData: IMusicRequest = {
  id: 27585,
  genre: 'knavishly before',
};

export const sampleWithFullData: IMusicRequest = {
  id: 7091,
  song: 'briskly curly bah',
  artist: 'verbalize unless',
  album: 'daring excluding anger',
  genre: 'astride commonly',
  requestedBy: 'unto anti',
  requestedOn: dayjs('2021-06-25T19:51'),
  url: 'https://belated-extremist.name',
  done: true,
};

export const sampleWithNewData: NewMusicRequest = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
