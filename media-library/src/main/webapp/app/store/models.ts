import dayjs from 'dayjs';

export interface IArtist {
  name?: string;
}

export interface IGenre {
  name?: string;
}

export interface IAlbum {
  id?: number;
  name?: string;
  artist?: IArtist;
  releasedYear?: number | null;
  artworkPath?: string;
}

export interface TrackType {
  id: number;
  name?: string | null;
  creditsNeeded?: number | null;
  vipCreditsNeeded?: number | null;
}

export interface ITrack {
  id: number;
  filePath?: string | null;
  artworkPath?: string;
  subfolder?: string | null;
  name?: string | null;
  artist?: IArtist | null;
  album?: IAlbum | null;
  genre?: IGenre | null;
  trackNumber?: number | null;
  playbackLength?: number | null;
  bitRate?: number | null;
  createdOn?: dayjs.Dayjs | null;
  tagVersion1?: boolean | null;
  tagVersion2?: boolean | null;
  type?: Pick<TrackType, 'id' | 'name'> | null;
}


export type NewTrack = Omit<ITrack, 'id'> & { id: null };

// export class Track implements ITrack {


// }



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
  subfolder?: string;
  update?:boolean;
}


export class UpdatetableAlbum {
  name?: string ;
  artist?: string;
  releasedYear?: number;

  constructor( name?: string, artist?: string, releasedYear?: number) {

  }
}

export type NewMusicRequest = Omit<ITrack, 'id'> & { id: null };