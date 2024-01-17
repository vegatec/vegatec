import dayjs from 'dayjs/esm';

import { ITrack, NewTrack } from './track.model';

export const sampleWithRequiredData: ITrack = {
  id: 19419,
  filePath: 'furthermore',
  subfolder: 'lovingly whose',
  name: 'smoothly compact',
  sortName: 'silver',
  artistName: 'than meanwhile',
  artistSortName: 'blacken',
  albumName: 'inside',
  albumSortName: 'eek dangerous per',
  albumArtistName: 'dust rapidly willing',
  genreName: 'deformation blushing wherever',
  genreSortName: 'hype fabricate',
  createdOn: dayjs('2021-06-30T15:26'),
};

export const sampleWithPartialData: ITrack = {
  id: 26616,
  filePath: 'frankly nearer infect',
  subfolder: 'aha before',
  name: 'coordinated fertilize cenotaph',
  sortName: 'rundown uh-huh',
  artistName: 'ventilate adventurously where',
  artistSortName: 'yuck far-off',
  albumName: 'to er dramatic',
  albumSortName: 'calculator however traumatic',
  albumArtistName: 'fooey stealthily hmph',
  albumArtistSortName: 'since',
  albumReleasedYear: 18053,
  genreName: 'before punctually',
  genreSortName: 'even embarrassed meanwhile',
  bitRate: 10093,
  createdOn: dayjs('2021-07-01T01:46'),
};

export const sampleWithFullData: ITrack = {
  id: 16593,
  filePath: 'slug amid triumphantly',
  subfolder: 'afterlife',
  name: 'except pinot',
  sortName: 'justly enraged',
  artistName: 'nor',
  artistSortName: 'wherever aha',
  albumName: 'phooey so separate',
  albumSortName: 'er',
  albumArtistName: 'woot passionate upset',
  albumArtistSortName: 'canvas yowza',
  albumReleasedYear: 1110,
  genreName: 'crest',
  genreSortName: 'planter',
  trackNumber: 28803,
  playbackLength: 32716,
  bitRate: 619,
  createdOn: dayjs('2021-07-01T01:31'),
  tagVersion1: false,
  tagVersion2: true,
};

export const sampleWithNewData: NewTrack = {
  filePath: 'almost',
  subfolder: 'through carjack',
  name: 'ack',
  sortName: 'duh alongside',
  artistName: 'bad',
  artistSortName: 'for remote scour',
  albumName: 'softly when amidst',
  albumSortName: 'throughout in',
  albumArtistName: 'ideology phooey',
  genreName: 'whereas um tricky',
  genreSortName: 'irritating lest',
  createdOn: dayjs('2021-06-30T23:57'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
