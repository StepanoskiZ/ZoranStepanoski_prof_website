import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../about-me.test-samples';

import { AboutMeFormService } from './about-me-form.service';

describe('AboutMe Form Service', () => {
  let service: AboutMeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AboutMeFormService);
  });

  describe('Service methods', () => {
    describe('createAboutMeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAboutMeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            contentHtml: expect.any(Object),
            language: expect.any(Object),
          }),
        );
      });

      it('passing IAboutMe should create a new form with FormGroup', () => {
        const formGroup = service.createAboutMeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            contentHtml: expect.any(Object),
            language: expect.any(Object),
          }),
        );
      });
    });

    describe('getAboutMe', () => {
      it('should return NewAboutMe for default AboutMe initial value', () => {
        const formGroup = service.createAboutMeFormGroup(sampleWithNewData);

        const aboutMe = service.getAboutMe(formGroup) as any;

        expect(aboutMe).toMatchObject(sampleWithNewData);
      });

      it('should return NewAboutMe for empty AboutMe initial value', () => {
        const formGroup = service.createAboutMeFormGroup();

        const aboutMe = service.getAboutMe(formGroup) as any;

        expect(aboutMe).toMatchObject({});
      });

      it('should return IAboutMe', () => {
        const formGroup = service.createAboutMeFormGroup(sampleWithRequiredData);

        const aboutMe = service.getAboutMe(formGroup) as any;

        expect(aboutMe).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAboutMe should not enable id FormControl', () => {
        const formGroup = service.createAboutMeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAboutMe should disable id FormControl', () => {
        const formGroup = service.createAboutMeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
