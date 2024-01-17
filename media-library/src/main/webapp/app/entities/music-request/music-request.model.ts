import dayjs from 'dayjs/esm';

export interface IMusicRequest {
  id: number;
  song?: string | null;
  artist?: string | null;
  album?: string | null;
  genre?: string | null;
  requestedBy?: string | null;
  requestedOn?: dayjs.Dayjs | null;
  url?: string | null;
  done?: boolean | null;
}

export type NewMusicRequest = Omit<IMusicRequest, 'id'> & { id: null };
