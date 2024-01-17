import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../music-request.test-samples';

import { MusicRequestFormService } from './music-request-form.service';

describe('MusicRequest Form Service', () => {
  let service: MusicRequestFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MusicRequestFormService);
  });

  describe('Service methods', () => {
    describe('createMusicRequestFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMusicRequestFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            song: expect.any(Object),
            artist: expect.any(Object),
            album: expect.any(Object),
            genre: expect.any(Object),
            requestedBy: expect.any(Object),
            requestedOn: expect.any(Object),
            url: expect.any(Object),
            done: expect.any(Object),
          }),
        );
      });

      it('passing IMusicRequest should create a new form with FormGroup', () => {
        const formGroup = service.createMusicRequestFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            song: expect.any(Object),
            artist: expect.any(Object),
            album: expect.any(Object),
            genre: expect.any(Object),
            requestedBy: expect.any(Object),
            requestedOn: expect.any(Object),
            url: expect.any(Object),
            done: expect.any(Object),
          }),
        );
      });
    });

    describe('getMusicRequest', () => {
      it('should return NewMusicRequest for default MusicRequest initial value', () => {
        const formGroup = service.createMusicRequestFormGroup(sampleWithNewData);

        const musicRequest = service.getMusicRequest(formGroup) as any;

        expect(musicRequest).toMatchObject(sampleWithNewData);
      });

      it('should return NewMusicRequest for empty MusicRequest initial value', () => {
        const formGroup = service.createMusicRequestFormGroup();

        const musicRequest = service.getMusicRequest(formGroup) as any;

        expect(musicRequest).toMatchObject({});
      });

      it('should return IMusicRequest', () => {
        const formGroup = service.createMusicRequestFormGroup(sampleWithRequiredData);

        const musicRequest = service.getMusicRequest(formGroup) as any;

        expect(musicRequest).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMusicRequest should not enable id FormControl', () => {
        const formGroup = service.createMusicRequestFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMusicRequest should disable id FormControl', () => {
        const formGroup = service.createMusicRequestFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
