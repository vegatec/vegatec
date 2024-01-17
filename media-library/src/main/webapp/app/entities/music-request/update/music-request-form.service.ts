import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMusicRequest, NewMusicRequest } from '../music-request.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMusicRequest for edit and NewMusicRequestFormGroupInput for create.
 */
type MusicRequestFormGroupInput = IMusicRequest | PartialWithRequiredKeyOf<NewMusicRequest>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMusicRequest | NewMusicRequest> = Omit<T, 'requestedOn'> & {
  requestedOn?: string | null;
};

type MusicRequestFormRawValue = FormValueOf<IMusicRequest>;

type NewMusicRequestFormRawValue = FormValueOf<NewMusicRequest>;

type MusicRequestFormDefaults = Pick<NewMusicRequest, 'id' | 'requestedOn' | 'done'>;

type MusicRequestFormGroupContent = {
  id: FormControl<MusicRequestFormRawValue['id'] | NewMusicRequest['id']>;
  song: FormControl<MusicRequestFormRawValue['song']>;
  artist: FormControl<MusicRequestFormRawValue['artist']>;
  album: FormControl<MusicRequestFormRawValue['album']>;
  genre: FormControl<MusicRequestFormRawValue['genre']>;
  requestedBy: FormControl<MusicRequestFormRawValue['requestedBy']>;
  requestedOn: FormControl<MusicRequestFormRawValue['requestedOn']>;
  url: FormControl<MusicRequestFormRawValue['url']>;
  done: FormControl<MusicRequestFormRawValue['done']>;
};

export type MusicRequestFormGroup = FormGroup<MusicRequestFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MusicRequestFormService {
  createMusicRequestFormGroup(musicRequest: MusicRequestFormGroupInput = { id: null }): MusicRequestFormGroup {
    const musicRequestRawValue = this.convertMusicRequestToMusicRequestRawValue({
      ...this.getFormDefaults(),
      ...musicRequest,
    });
    return new FormGroup<MusicRequestFormGroupContent>({
      id: new FormControl(
        { value: musicRequestRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      song: new FormControl(musicRequestRawValue.song),
      artist: new FormControl(musicRequestRawValue.artist),
      album: new FormControl(musicRequestRawValue.album),
      genre: new FormControl(musicRequestRawValue.genre),
      requestedBy: new FormControl(musicRequestRawValue.requestedBy),
      requestedOn: new FormControl(musicRequestRawValue.requestedOn),
      url: new FormControl(musicRequestRawValue.url),
      done: new FormControl(musicRequestRawValue.done),
    });
  }

  getMusicRequest(form: MusicRequestFormGroup): IMusicRequest | NewMusicRequest {
    return this.convertMusicRequestRawValueToMusicRequest(form.getRawValue() as MusicRequestFormRawValue | NewMusicRequestFormRawValue);
  }

  resetForm(form: MusicRequestFormGroup, musicRequest: MusicRequestFormGroupInput): void {
    const musicRequestRawValue = this.convertMusicRequestToMusicRequestRawValue({ ...this.getFormDefaults(), ...musicRequest });
    form.reset(
      {
        ...musicRequestRawValue,
        id: { value: musicRequestRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MusicRequestFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      requestedOn: currentTime,
      done: false,
    };
  }

  private convertMusicRequestRawValueToMusicRequest(
    rawMusicRequest: MusicRequestFormRawValue | NewMusicRequestFormRawValue,
  ): IMusicRequest | NewMusicRequest {
    return {
      ...rawMusicRequest,
      requestedOn: dayjs(rawMusicRequest.requestedOn, DATE_TIME_FORMAT),
    };
  }

  private convertMusicRequestToMusicRequestRawValue(
    musicRequest: IMusicRequest | (Partial<NewMusicRequest> & MusicRequestFormDefaults),
  ): MusicRequestFormRawValue | PartialWithRequiredKeyOf<NewMusicRequestFormRawValue> {
    return {
      ...musicRequest,
      requestedOn: musicRequest.requestedOn ? musicRequest.requestedOn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
