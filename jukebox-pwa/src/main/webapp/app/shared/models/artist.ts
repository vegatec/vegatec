export interface Artist {
  name: string;
}

export interface Genre {
  name: string;
}

export interface Album {
  name: string;
  releasedYear: number;
  artist: Artist;
}

export interface Track {
  name: string;
  artist: Artist;
  album: Album;
  genre: Genre;
}
