import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../about-me-media.test-samples';

import { AboutMeMediaFormService } from './about-me-media-form.service';

describe('AboutMeMedia Form Service', () => {
  let service: AboutMeMediaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AboutMeMediaFormService);
  });

  describe('Service methods', () => {
    describe('createAboutMeMediaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAboutMeMediaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mediaUrl: expect.any(Object),
            aboutMeMediaType: expect.any(Object),
            caption: expect.any(Object),
            aboutMe: expect.any(Object),
          }),
        );
      });

      it('passing IAboutMeMedia should create a new form with FormGroup', () => {
        const formGroup = service.createAboutMeMediaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mediaUrl: expect.any(Object),
            aboutMeMediaType: expect.any(Object),
            caption: expect.any(Object),
            aboutMe: expect.any(Object),
          }),
        );
      });
    });

    describe('getAboutMeMedia', () => {
      it('should return NewAboutMeMedia for default AboutMeMedia initial value', () => {
        const formGroup = service.createAboutMeMediaFormGroup(sampleWithNewData);

        const aboutMeMedia = service.getAboutMeMedia(formGroup) as any;

        expect(aboutMeMedia).toMatchObject(sampleWithNewData);
      });

      it('should return NewAboutMeMedia for empty AboutMeMedia initial value', () => {
        const formGroup = service.createAboutMeMediaFormGroup();

        const aboutMeMedia = service.getAboutMeMedia(formGroup) as any;

        expect(aboutMeMedia).toMatchObject({});
      });

      it('should return IAboutMeMedia', () => {
        const formGroup = service.createAboutMeMediaFormGroup(sampleWithRequiredData);

        const aboutMeMedia = service.getAboutMeMedia(formGroup) as any;

        expect(aboutMeMedia).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAboutMeMedia should not enable id FormControl', () => {
        const formGroup = service.createAboutMeMediaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAboutMeMedia should disable id FormControl', () => {
        const formGroup = service.createAboutMeMediaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
