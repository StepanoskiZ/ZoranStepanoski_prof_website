import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../curriculum-vitae-media.test-samples';

import { CurriculumVitaeMediaFormService } from './curriculum-vitae-media-form.service';

describe('CurriculumVitaeMedia Form Service', () => {
  let service: CurriculumVitaeMediaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CurriculumVitaeMediaFormService);
  });

  describe('Service methods', () => {
    describe('createCurriculumVitaeMediaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCurriculumVitaeMediaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mediaUrl: expect.any(Object),
            curriculumVitaeMediaType: expect.any(Object),
            caption: expect.any(Object),
            curriculumVitae: expect.any(Object),
          }),
        );
      });

      it('passing ICurriculumVitaeMedia should create a new form with FormGroup', () => {
        const formGroup = service.createCurriculumVitaeMediaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mediaUrl: expect.any(Object),
            curriculumVitaeMediaType: expect.any(Object),
            caption: expect.any(Object),
            curriculumVitae: expect.any(Object),
          }),
        );
      });
    });

    describe('getCurriculumVitaeMedia', () => {
      it('should return NewCurriculumVitaeMedia for default CurriculumVitaeMedia initial value', () => {
        const formGroup = service.createCurriculumVitaeMediaFormGroup(sampleWithNewData);

        const curriculumVitaeMedia = service.getCurriculumVitaeMedia(formGroup) as any;

        expect(curriculumVitaeMedia).toMatchObject(sampleWithNewData);
      });

      it('should return NewCurriculumVitaeMedia for empty CurriculumVitaeMedia initial value', () => {
        const formGroup = service.createCurriculumVitaeMediaFormGroup();

        const curriculumVitaeMedia = service.getCurriculumVitaeMedia(formGroup) as any;

        expect(curriculumVitaeMedia).toMatchObject({});
      });

      it('should return ICurriculumVitaeMedia', () => {
        const formGroup = service.createCurriculumVitaeMediaFormGroup(sampleWithRequiredData);

        const curriculumVitaeMedia = service.getCurriculumVitaeMedia(formGroup) as any;

        expect(curriculumVitaeMedia).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICurriculumVitaeMedia should not enable id FormControl', () => {
        const formGroup = service.createCurriculumVitaeMediaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCurriculumVitaeMedia should disable id FormControl', () => {
        const formGroup = service.createCurriculumVitaeMediaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
