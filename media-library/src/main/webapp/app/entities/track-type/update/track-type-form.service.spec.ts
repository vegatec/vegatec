import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../track-type.test-samples';

import { TrackTypeFormService } from './track-type-form.service';

describe('TrackType Form Service', () => {
  let service: TrackTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrackTypeFormService);
  });

  describe('Service methods', () => {
    describe('createTrackTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTrackTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            creditsNeeded: expect.any(Object),
            vipCreditsNeeded: expect.any(Object),
          }),
        );
      });

      it('passing ITrackType should create a new form with FormGroup', () => {
        const formGroup = service.createTrackTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            creditsNeeded: expect.any(Object),
            vipCreditsNeeded: expect.any(Object),
          }),
        );
      });
    });

    describe('getTrackType', () => {
      it('should return NewTrackType for default TrackType initial value', () => {
        const formGroup = service.createTrackTypeFormGroup(sampleWithNewData);

        const trackType = service.getTrackType(formGroup) as any;

        expect(trackType).toMatchObject(sampleWithNewData);
      });

      it('should return NewTrackType for empty TrackType initial value', () => {
        const formGroup = service.createTrackTypeFormGroup();

        const trackType = service.getTrackType(formGroup) as any;

        expect(trackType).toMatchObject({});
      });

      it('should return ITrackType', () => {
        const formGroup = service.createTrackTypeFormGroup(sampleWithRequiredData);

        const trackType = service.getTrackType(formGroup) as any;

        expect(trackType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITrackType should not enable id FormControl', () => {
        const formGroup = service.createTrackTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTrackType should disable id FormControl', () => {
        const formGroup = service.createTrackTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
