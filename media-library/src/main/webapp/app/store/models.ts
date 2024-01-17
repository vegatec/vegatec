import dayjs from 'dayjs';

export interface Artist {
  name?: string;
}

export interface Genre {
  name?: string;
}

export interface Album {
  id?: number;
  name?: string;
  artist?: Artist;
  releasedYear?: number | null;
  artworkPath?: string;
}

export interface TrackType {
  id: number;
  name?: string | null;
  creditsNeeded?: number | null;
  vipCreditsNeeded?: number | null;
}

export interface Track {
  id: number;
  filePath?: string | null;
  artworkPath?: string;
  subfolder?: string | null;
  name?: string | null;
  sortName?: string | null;
  artist?: Artist | null;
  album?: Album | null;
  genre?: Genre | null;
  trackNumber?: number | null;
  playbackLength?: number | null;
  bitRate?: number | null;
  createdOn?: dayjs.Dayjs | null;
  tagVersion1?: boolean | null;
  tagVersion2?: boolean | null;
  type?: Pick<TrackType, 'id' | 'name'> | null;
}

export type NewTrack = Omit<Track, 'id'> & { id: null };

export interface PagingOptions {
  itemsPerPage: number;
  page: number;
  sort: string[];
}

export interface SearchCriteria {
  filter?: string;
  releasedSince?: number;
  addedSince?: number;
  albumArtistName?: string;
  albumName?: string;
  artistName?: string;
  trackName?: string;
}
