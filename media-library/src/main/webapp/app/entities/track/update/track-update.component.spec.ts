import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ITrackType } from 'app/entities/track-type/track-type.model';
import { TrackTypeService } from 'app/entities/track-type/service/track-type.service';
import { TrackService } from '../service/track.service';
import { ITrack } from '../track.model';
import { TrackFormService } from './track-form.service';

import { TrackUpdateComponent } from './track-update.component';

describe('Track Management Update Component', () => {
  let comp: TrackUpdateComponent;
  let fixture: ComponentFixture<TrackUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trackFormService: TrackFormService;
  let trackService: TrackService;
  let trackTypeService: TrackTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TrackUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TrackUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrackUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trackFormService = TestBed.inject(TrackFormService);
    trackService = TestBed.inject(TrackService);
    trackTypeService = TestBed.inject(TrackTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TrackType query and add missing value', () => {
      const track: ITrack = { id: 456 };
      const type: ITrackType = { id: 19001 };
      track.type = type;

      const trackTypeCollection: ITrackType[] = [{ id: 7904 }];
      jest.spyOn(trackTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: trackTypeCollection })));
      const additionalTrackTypes = [type];
      const expectedCollection: ITrackType[] = [...additionalTrackTypes, ...trackTypeCollection];
      jest.spyOn(trackTypeService, 'addTrackTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ track });
      comp.ngOnInit();

      expect(trackTypeService.query).toHaveBeenCalled();
      expect(trackTypeService.addTrackTypeToCollectionIfMissing).toHaveBeenCalledWith(
        trackTypeCollection,
        ...additionalTrackTypes.map(expect.objectContaining),
      );
      expect(comp.trackTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const track: ITrack = { id: 456 };
      const type: ITrackType = { id: 13593 };
      track.type = type;

      activatedRoute.data = of({ track });
      comp.ngOnInit();

      expect(comp.trackTypesSharedCollection).toContain(type);
      expect(comp.track).toEqual(track);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrack>>();
      const track = { id: 123 };
      jest.spyOn(trackFormService, 'getTrack').mockReturnValue(track);
      jest.spyOn(trackService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ track });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: track }));
      saveSubject.complete();

      // THEN
      expect(trackFormService.getTrack).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(trackService.update).toHaveBeenCalledWith(expect.objectContaining(track));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrack>>();
      const track = { id: 123 };
      jest.spyOn(trackFormService, 'getTrack').mockReturnValue({ id: null });
      jest.spyOn(trackService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ track: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: track }));
      saveSubject.complete();

      // THEN
      expect(trackFormService.getTrack).toHaveBeenCalled();
      expect(trackService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrack>>();
      const track = { id: 123 };
      jest.spyOn(trackService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ track });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trackService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTrackType', () => {
      it('Should forward to trackTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(trackTypeService, 'compareTrackType');
        comp.compareTrackType(entity, entity2);
        expect(trackTypeService.compareTrackType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
