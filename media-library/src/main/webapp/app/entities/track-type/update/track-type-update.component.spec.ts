import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TrackTypeService } from '../service/track-type.service';
import { ITrackType } from '../track-type.model';
import { TrackTypeFormService } from './track-type-form.service';

import { TrackTypeUpdateComponent } from './track-type-update.component';

describe('TrackType Management Update Component', () => {
  let comp: TrackTypeUpdateComponent;
  let fixture: ComponentFixture<TrackTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trackTypeFormService: TrackTypeFormService;
  let trackTypeService: TrackTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TrackTypeUpdateComponent],
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
      .overrideTemplate(TrackTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrackTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trackTypeFormService = TestBed.inject(TrackTypeFormService);
    trackTypeService = TestBed.inject(TrackTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const trackType: ITrackType = { id: 456 };

      activatedRoute.data = of({ trackType });
      comp.ngOnInit();

      expect(comp.trackType).toEqual(trackType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrackType>>();
      const trackType = { id: 123 };
      jest.spyOn(trackTypeFormService, 'getTrackType').mockReturnValue(trackType);
      jest.spyOn(trackTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trackType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trackType }));
      saveSubject.complete();

      // THEN
      expect(trackTypeFormService.getTrackType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(trackTypeService.update).toHaveBeenCalledWith(expect.objectContaining(trackType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrackType>>();
      const trackType = { id: 123 };
      jest.spyOn(trackTypeFormService, 'getTrackType').mockReturnValue({ id: null });
      jest.spyOn(trackTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trackType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trackType }));
      saveSubject.complete();

      // THEN
      expect(trackTypeFormService.getTrackType).toHaveBeenCalled();
      expect(trackTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrackType>>();
      const trackType = { id: 123 };
      jest.spyOn(trackTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trackType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trackTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
