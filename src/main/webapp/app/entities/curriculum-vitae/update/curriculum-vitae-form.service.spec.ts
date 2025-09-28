import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../curriculum-vitae.test-samples';

import { CurriculumVitaeFormService } from './curriculum-vitae-form.service';

describe('CurriculumVitae Form Service', () => {
  let service: CurriculumVitaeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CurriculumVitaeFormService);
  });

  describe('Service methods', () => {
    describe('createCurriculumVitaeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCurriculumVitaeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            companyName: expect.any(Object),
            language: expect.any(Object),
            jobDescriptionHTML: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            status: expect.any(Object),
            type: expect.any(Object),
            category: expect.any(Object),
          }),
        );
      });

      it('passing ICurriculumVitae should create a new form with FormGroup', () => {
        const formGroup = service.createCurriculumVitaeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            companyName: expect.any(Object),
            language: expect.any(Object),
            jobDescriptionHTML: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            status: expect.any(Object),
            type: expect.any(Object),
            category: expect.any(Object),
          }),
        );
      });
    });

    describe('getCurriculumVitae', () => {
      it('should return NewCurriculumVitae for default CurriculumVitae initial value', () => {
        const formGroup = service.createCurriculumVitaeFormGroup(sampleWithNewData);

        const curriculumVitae = service.getCurriculumVitae(formGroup) as any;

        expect(curriculumVitae).toMatchObject(sampleWithNewData);
      });

      it('should return NewCurriculumVitae for empty CurriculumVitae initial value', () => {
        const formGroup = service.createCurriculumVitaeFormGroup();

        const curriculumVitae = service.getCurriculumVitae(formGroup) as any;

        expect(curriculumVitae).toMatchObject({});
      });

      it('should return ICurriculumVitae', () => {
        const formGroup = service.createCurriculumVitaeFormGroup(sampleWithRequiredData);

        const curriculumVitae = service.getCurriculumVitae(formGroup) as any;

        expect(curriculumVitae).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICurriculumVitae should not enable id FormControl', () => {
        const formGroup = service.createCurriculumVitaeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCurriculumVitae should disable id FormControl', () => {
        const formGroup = service.createCurriculumVitaeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
