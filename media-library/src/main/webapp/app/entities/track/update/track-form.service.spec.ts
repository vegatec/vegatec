import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../track.test-samples';

import { TrackFormService } from './track-form.service';

describe('Track Form Service', () => {
  let service: TrackFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrackFormService);
  });

  describe('Service methods', () => {
    describe('createTrackFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTrackFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            filePath: expect.any(Object),
            subfolder: expect.any(Object),
            name: expect.any(Object),
            sortName: expect.any(Object),
            artistName: expect.any(Object),
            artistSortName: expect.any(Object),
            albumName: expect.any(Object),
            albumSortName: expect.any(Object),
            albumArtistName: expect.any(Object),
            albumArtistSortName: expect.any(Object),
            albumReleasedYear: expect.any(Object),
            genreName: expect.any(Object),
            genreSortName: expect.any(Object),
            trackNumber: expect.any(Object),
            playbackLength: expect.any(Object),
            bitRate: expect.any(Object),
            createdOn: expect.any(Object),
            tagVersion1: expect.any(Object),
            tagVersion2: expect.any(Object),
            type: expect.any(Object),
          }),
        );
      });

      it('passing ITrack should create a new form with FormGroup', () => {
        const formGroup = service.createTrackFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            filePath: expect.any(Object),
            subfolder: expect.any(Object),
            name: expect.any(Object),
            sortName: expect.any(Object),
            artistName: expect.any(Object),
            artistSortName: expect.any(Object),
            albumName: expect.any(Object),
            albumSortName: expect.any(Object),
            albumArtistName: expect.any(Object),
            albumArtistSortName: expect.any(Object),
            albumReleasedYear: expect.any(Object),
            genreName: expect.any(Object),
            genreSortName: expect.any(Object),
            trackNumber: expect.any(Object),
            playbackLength: expect.any(Object),
            bitRate: expect.any(Object),
            createdOn: expect.any(Object),
            tagVersion1: expect.any(Object),
            tagVersion2: expect.any(Object),
            type: expect.any(Object),
          }),
        );
      });
    });

    describe('getTrack', () => {
      it('should return NewTrack for default Track initial value', () => {
        const formGroup = service.createTrackFormGroup(sampleWithNewData);

        const track = service.getTrack(formGroup) as any;

        expect(track).toMatchObject(sampleWithNewData);
      });

      it('should return NewTrack for empty Track initial value', () => {
        const formGroup = service.createTrackFormGroup();

        const track = service.getTrack(formGroup) as any;

        expect(track).toMatchObject({});
      });

      it('should return ITrack', () => {
        const formGroup = service.createTrackFormGroup(sampleWithRequiredData);

        const track = service.getTrack(formGroup) as any;

        expect(track).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITrack should not enable id FormControl', () => {
        const formGroup = service.createTrackFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTrack should disable id FormControl', () => {
        const formGroup = service.createTrackFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
