import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MusicRequestService } from '../service/music-request.service';
import { IMusicRequest } from '../music-request.model';
import { MusicRequestFormService } from './music-request-form.service';

import { MusicRequestUpdateComponent } from './music-request-update.component';

describe('MusicRequest Management Update Component', () => {
  let comp: MusicRequestUpdateComponent;
  let fixture: ComponentFixture<MusicRequestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let musicRequestFormService: MusicRequestFormService;
  let musicRequestService: MusicRequestService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MusicRequestUpdateComponent],
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
      .overrideTemplate(MusicRequestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MusicRequestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    musicRequestFormService = TestBed.inject(MusicRequestFormService);
    musicRequestService = TestBed.inject(MusicRequestService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const musicRequest: IMusicRequest = { id: 456 };

      activatedRoute.data = of({ musicRequest });
      comp.ngOnInit();

      expect(comp.musicRequest).toEqual(musicRequest);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMusicRequest>>();
      const musicRequest = { id: 123 };
      jest.spyOn(musicRequestFormService, 'getMusicRequest').mockReturnValue(musicRequest);
      jest.spyOn(musicRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ musicRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: musicRequest }));
      saveSubject.complete();

      // THEN
      expect(musicRequestFormService.getMusicRequest).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(musicRequestService.update).toHaveBeenCalledWith(expect.objectContaining(musicRequest));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMusicRequest>>();
      const musicRequest = { id: 123 };
      jest.spyOn(musicRequestFormService, 'getMusicRequest').mockReturnValue({ id: null });
      jest.spyOn(musicRequestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ musicRequest: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: musicRequest }));
      saveSubject.complete();

      // THEN
      expect(musicRequestFormService.getMusicRequest).toHaveBeenCalled();
      expect(musicRequestService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMusicRequest>>();
      const musicRequest = { id: 123 };
      jest.spyOn(musicRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ musicRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(musicRequestService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
