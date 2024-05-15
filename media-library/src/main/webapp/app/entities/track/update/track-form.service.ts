import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
// import { ITrack, NewTrack } from '../track.model';

/**
 * A partial Type with required key is used as form input.
 */
// type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

// /**
//  * Type for createFormGroup and resetForm argument.
//  * It accepts ITrack for edit and NewTrackFormGroupInput for create.
//  */
// type TrackFormGroupInput = ITrack | PartialWithRequiredKeyOf<NewTrack>;

// /**
//  * Type that converts some properties for forms.
//  */
// type FormValueOf<T extends ITrack | NewTrack> = Omit<T, 'createdOn'> & {
//   createdOn?: string | null;
// };

// type TrackFormRawValue = FormValueOf<ITrack>;

// type NewTrackFormRawValue = FormValueOf<NewTrack>;

// type TrackFormDefaults = Pick<NewTrack, 'id' | 'createdOn' | 'tagVersion1' | 'tagVersion2'>;

// type TrackFormGroupContent = {
//   id: FormControl<TrackFormRawValue['id'] | NewTrack['id']>;
//   filePath: FormControl<TrackFormRawValue['filePath']>;
//   subfolder: FormControl<TrackFormRawValue['subfolder']>;
//   name: FormControl<TrackFormRawValue['name']>;
//   sortName: FormControl<TrackFormRawValue['sortName']>;
//   artistName: FormControl<TrackFormRawValue['artistName']>;
//   artistSortName: FormControl<TrackFormRawValue['artistSortName']>;
//   albumName: FormControl<TrackFormRawValue['albumName']>;
//   albumSortName: FormControl<TrackFormRawValue['albumSortName']>;
//   albumArtistName: FormControl<TrackFormRawValue['albumArtistName']>;
//   albumArtistSortName: FormControl<TrackFormRawValue['albumArtistSortName']>;
//   albumReleasedYear: FormControl<TrackFormRawValue['albumReleasedYear']>;
//   genreName: FormControl<TrackFormRawValue['genreName']>;
//   genreSortName: FormControl<TrackFormRawValue['genreSortName']>;
//   trackNumber: FormControl<TrackFormRawValue['trackNumber']>;
//   playbackLength: FormControl<TrackFormRawValue['playbackLength']>;
//   bitRate: FormControl<TrackFormRawValue['bitRate']>;
//   createdOn: FormControl<TrackFormRawValue['createdOn']>;
//   tagVersion1: FormControl<TrackFormRawValue['tagVersion1']>;
//   tagVersion2: FormControl<TrackFormRawValue['tagVersion2']>;
//   type: FormControl<TrackFormRawValue['type']>;
// };

// export type TrackFormGroup = FormGroup<TrackFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TrackFormService {
  // createTrackFormGroup(track: TrackFormGroupInput = { id: null }): TrackFormGroup {
  //   const trackRawValue = this.convertTrackToTrackRawValue({
  //     ...this.getFormDefaults(),
  //     ...track,
  //   });
  //   return new FormGroup<TrackFormGroupContent>({
  //     id: new FormControl(
  //       { value: trackRawValue.id, disabled: true },
  //       {
  //         nonNullable: true,
  //         validators: [Validators.required],
  //       },
  //     ),
  //     filePath: new FormControl(trackRawValue.filePath, {
  //       validators: [Validators.required],
  //     }),
  //     subfolder: new FormControl(trackRawValue.subfolder, {
  //       validators: [Validators.required],
  //     }),
  //     name: new FormControl(trackRawValue.name, {
  //       validators: [Validators.required],
  //     }),
  //     sortName: new FormControl(trackRawValue.sortName, {
  //       validators: [Validators.required],
  //     }),
  //     artistName: new FormControl(trackRawValue.artistName, {
  //       validators: [Validators.required],
  //     }),
  //     artistSortName: new FormControl(trackRawValue.artistSortName, {
  //       validators: [Validators.required],
  //     }),
  //     albumName: new FormControl(trackRawValue.albumName, {
  //       validators: [Validators.required],
  //     }),
  //     albumSortName: new FormControl(trackRawValue.albumSortName, {
  //       validators: [Validators.required],
  //     }),
  //     albumArtistName: new FormControl(trackRawValue.albumArtistName, {
  //       validators: [Validators.required],
  //     }),
  //     albumArtistSortName: new FormControl(trackRawValue.albumArtistSortName),
  //     albumReleasedYear: new FormControl(trackRawValue.albumReleasedYear),
  //     genreName: new FormControl(trackRawValue.genreName, {
  //       validators: [Validators.required],
  //     }),
  //     genreSortName: new FormControl(trackRawValue.genreSortName, {
  //       validators: [Validators.required],
  //     }),
  //     trackNumber: new FormControl(trackRawValue.trackNumber),
  //     playbackLength: new FormControl(trackRawValue.playbackLength),
  //     bitRate: new FormControl(trackRawValue.bitRate),
  //     createdOn: new FormControl(trackRawValue.createdOn, {
  //       validators: [Validators.required],
  //     }),
  //     tagVersion1: new FormControl(trackRawValue.tagVersion1),
  //     tagVersion2: new FormControl(trackRawValue.tagVersion2),
  //     type: new FormControl(trackRawValue.type, {
  //       validators: [Validators.required],
  //     }),
  //   });
  // }

  // getTrack(form: TrackFormGroup): ITrack | NewTrack {
  //   return this.convertTrackRawValueToTrack(form.getRawValue() as TrackFormRawValue | NewTrackFormRawValue);
  // }

  // resetForm(form: TrackFormGroup, track: TrackFormGroupInput): void {
  //   const trackRawValue = this.convertTrackToTrackRawValue({ ...this.getFormDefaults(), ...track });
  //   form.reset(
  //     {
  //       ...trackRawValue,
  //       id: { value: trackRawValue.id, disabled: true },
  //     } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
  //   );
  // }

  // private getFormDefaults(): TrackFormDefaults {
  //   const currentTime = dayjs();

  //   return {
  //     id: null,
  //     createdOn: currentTime,
  //     tagVersion1: false,
  //     tagVersion2: false,
  //   };
  // }

  // private convertTrackRawValueToTrack(rawTrack: TrackFormRawValue | NewTrackFormRawValue): ITrack | NewTrack {
  //   return {
  //     ...rawTrack,
  //     createdOn: dayjs(rawTrack.createdOn, DATE_TIME_FORMAT),
  //   };
  // }

  // private convertTrackToTrackRawValue(
  //   track: ITrack | (Partial<NewTrack> & TrackFormDefaults),
  // ): TrackFormRawValue | PartialWithRequiredKeyOf<NewTrackFormRawValue> {
  //   return {
  //     ...track,
  //     createdOn: track.createdOn ? track.createdOn.format(DATE_TIME_FORMAT) : undefined,
  //   };
  // }
}
