import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BusinessService } from '../service/business.service';
import { IBusiness } from '../business.model';
import { BusinessFormService } from './business-form.service';

import { BusinessUpdateComponent } from './business-update.component';

describe('Business Management Update Component', () => {
  let comp: BusinessUpdateComponent;
  let fixture: ComponentFixture<BusinessUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let businessFormService: BusinessFormService;
  let businessService: BusinessService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), BusinessUpdateComponent],
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
      .overrideTemplate(BusinessUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BusinessUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    businessFormService = TestBed.inject(BusinessFormService);
    businessService = TestBed.inject(BusinessService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const business: IBusiness = { id: 456 };

      activatedRoute.data = of({ business });
      comp.ngOnInit();

      expect(comp.business).toEqual(business);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBusiness>>();
      const business = { id: 123 };
      jest.spyOn(businessFormService, 'getBusiness').mockReturnValue(business);
      jest.spyOn(businessService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ business });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: business }));
      saveSubject.complete();

      // THEN
      expect(businessFormService.getBusiness).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(businessService.update).toHaveBeenCalledWith(expect.objectContaining(business));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBusiness>>();
      const business = { id: 123 };
      jest.spyOn(businessFormService, 'getBusiness').mockReturnValue({ id: null });
      jest.spyOn(businessService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ business: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: business }));
      saveSubject.complete();

      // THEN
      expect(businessFormService.getBusiness).toHaveBeenCalled();
      expect(businessService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBusiness>>();
      const business = { id: 123 };
      jest.spyOn(businessService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ business });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(businessService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
