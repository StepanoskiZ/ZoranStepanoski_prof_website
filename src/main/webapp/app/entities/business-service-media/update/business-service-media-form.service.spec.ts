import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../business-service-media.test-samples';

import { BusinessServiceMediaFormService } from './business-service-media-form.service';

describe('BusinessServiceMedia Form Service', () => {
  let service: BusinessServiceMediaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BusinessServiceMediaFormService);
  });

  describe('Service methods', () => {
    describe('createBusinessServiceMediaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBusinessServiceMediaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mediaUrl: expect.any(Object),
            businessServiceMediaType: expect.any(Object),
            caption: expect.any(Object),
            businessService: expect.any(Object),
          }),
        );
      });

      it('passing IBusinessServiceMedia should create a new form with FormGroup', () => {
        const formGroup = service.createBusinessServiceMediaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mediaUrl: expect.any(Object),
            businessServiceMediaType: expect.any(Object),
            caption: expect.any(Object),
            businessService: expect.any(Object),
          }),
        );
      });
    });

    describe('getBusinessServiceMedia', () => {
      it('should return NewBusinessServiceMedia for default BusinessServiceMedia initial value', () => {
        const formGroup = service.createBusinessServiceMediaFormGroup(sampleWithNewData);

        const businessServiceMedia = service.getBusinessServiceMedia(formGroup) as any;

        expect(businessServiceMedia).toMatchObject(sampleWithNewData);
      });

      it('should return NewBusinessServiceMedia for empty BusinessServiceMedia initial value', () => {
        const formGroup = service.createBusinessServiceMediaFormGroup();

        const businessServiceMedia = service.getBusinessServiceMedia(formGroup) as any;

        expect(businessServiceMedia).toMatchObject({});
      });

      it('should return IBusinessServiceMedia', () => {
        const formGroup = service.createBusinessServiceMediaFormGroup(sampleWithRequiredData);

        const businessServiceMedia = service.getBusinessServiceMedia(formGroup) as any;

        expect(businessServiceMedia).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBusinessServiceMedia should not enable id FormControl', () => {
        const formGroup = service.createBusinessServiceMediaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBusinessServiceMedia should disable id FormControl', () => {
        const formGroup = service.createBusinessServiceMediaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
