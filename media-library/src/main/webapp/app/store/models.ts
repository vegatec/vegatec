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

export class UpdatetableTrack implements Track {

  // id: number;
  // name?: string;
  // artist?: string;
  // genre?: string;

  // album?: UpdatetableAlbum;
  // album?: string ;
  // albumArtist?: string;
  // releasedYear?: number;

  constructor( id: number, name?: string, artist?: string, album?: string, albumArtist?: string, genre?: string, releasedYear?: number) {
    this.id= id;
    this.name= name;
    // this.artist= artist;
    // this.genre= genre;
    // this.album= new UpdatetableAlbum(album, albumArtist, releasedYear)

    // this.album= album;
    // this.albumArtist= albumArtist;
    // this.releasedYear= releasedYear;
  }
  id: number;
  filePath?: string | null | undefined;
  artworkPath?: string | undefined;
  subfolder?: string | null | undefined;
  name?: string | null | undefined;
  sortName?: string | null | undefined;
  artist?: Artist | null | undefined;
  album?: Album | null | undefined;
  genre?: Genre | null | undefined;
  trackNumber?: number | null | undefined;
  playbackLength?: number | null | undefined;
  bitRate?: number | null | undefined;
  createdOn?: dayjs.Dayjs | null | undefined;
  tagVersion1?: boolean | null | undefined;
  tagVersion2?: boolean | null | undefined;
  type?: Pick<TrackType, 'id' | 'name'> | null | undefined;
}