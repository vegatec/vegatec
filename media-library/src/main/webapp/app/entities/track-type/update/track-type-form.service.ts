import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITrackType, NewTrackType } from '../track-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITrackType for edit and NewTrackTypeFormGroupInput for create.
 */
type TrackTypeFormGroupInput = ITrackType | PartialWithRequiredKeyOf<NewTrackType>;

type TrackTypeFormDefaults = Pick<NewTrackType, 'id'>;

type TrackTypeFormGroupContent = {
  id: FormControl<ITrackType['id'] | NewTrackType['id']>;
  name: FormControl<ITrackType['name']>;
  creditsNeeded: FormControl<ITrackType['creditsNeeded']>;
  vipCreditsNeeded: FormControl<ITrackType['vipCreditsNeeded']>;
};

export type TrackTypeFormGroup = FormGroup<TrackTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TrackTypeFormService {
  createTrackTypeFormGroup(trackType: TrackTypeFormGroupInput = { id: null }): TrackTypeFormGroup {
    const trackTypeRawValue = {
      ...this.getFormDefaults(),
      ...trackType,
    };
    return new FormGroup<TrackTypeFormGroupContent>({
      id: new FormControl(
        { value: trackTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(trackTypeRawValue.name, {
        validators: [Validators.required],
      }),
      creditsNeeded: new FormControl(trackTypeRawValue.creditsNeeded),
      vipCreditsNeeded: new FormControl(trackTypeRawValue.vipCreditsNeeded),
    });
  }

  getTrackType(form: TrackTypeFormGroup): ITrackType | NewTrackType {
    return form.getRawValue() as ITrackType | NewTrackType;
  }

  resetForm(form: TrackTypeFormGroup, trackType: TrackTypeFormGroupInput): void {
    const trackTypeRawValue = { ...this.getFormDefaults(), ...trackType };
    form.reset(
      {
        ...trackTypeRawValue,
        id: { value: trackTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TrackTypeFormDefaults {
    return {
      id: null,
    };
  }
}
