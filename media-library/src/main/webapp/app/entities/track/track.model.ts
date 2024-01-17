import dayjs from 'dayjs/esm';
import { ITrackType } from 'app/entities/track-type/track-type.model';
import { IAlbum, IArtist, IGenre } from '../album/album.model';

// export interface ITrack {
//   id: number;
//   filePath?: string | null;
//   artworkPath?: string;
//   subfolder?: string | null;
//   name?: string | null;
//   sortName?: string | null;
//   artist?: IArtist | null;
//   album?: IAlbum | null;
//   genre?: IGenre | null;
//   trackNumber?: number | null;
//   playbackLength?: number | null;
//   bitRate?: number | null;
//   createdOn?: dayjs.Dayjs | null;
//   tagVersion1?: boolean | null;
//   tagVersion2?: boolean | null;
//   type?: Pick<ITrackType, 'id' | 'name'> | null;
// }

// export type NewTrack = Omit<ITrack, 'id'> & { id: null };
