import dayjs from 'dayjs/esm';

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

export type NewAlbum = Omit<IAlbum, 'id'> & { id: null };
