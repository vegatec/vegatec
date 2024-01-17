import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../business.test-samples';

import { BusinessFormService } from './business-form.service';

describe('Business Form Service', () => {
  let service: BusinessFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BusinessFormService);
  });

  describe('Service methods', () => {
    describe('createBusinessFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBusinessFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            address: expect.any(Object),
            owner: expect.any(Object),
            contact: expect.any(Object),
            phone: expect.any(Object),
          }),
        );
      });

      it('passing IBusiness should create a new form with FormGroup', () => {
        const formGroup = service.createBusinessFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            address: expect.any(Object),
            owner: expect.any(Object),
            contact: expect.any(Object),
            phone: expect.any(Object),
          }),
        );
      });
    });

    describe('getBusiness', () => {
      it('should return NewBusiness for default Business initial value', () => {
        const formGroup = service.createBusinessFormGroup(sampleWithNewData);

        const business = service.getBusiness(formGroup) as any;

        expect(business).toMatchObject(sampleWithNewData);
      });

      it('should return NewBusiness for empty Business initial value', () => {
        const formGroup = service.createBusinessFormGroup();

        const business = service.getBusiness(formGroup) as any;

        expect(business).toMatchObject({});
      });

      it('should return IBusiness', () => {
        const formGroup = service.createBusinessFormGroup(sampleWithRequiredData);

        const business = service.getBusiness(formGroup) as any;

        expect(business).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBusiness should not enable id FormControl', () => {
        const formGroup = service.createBusinessFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBusiness should disable id FormControl', () => {
        const formGroup = service.createBusinessFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
