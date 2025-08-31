import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../project-image.test-samples';

import { ProjectImageFormService } from './project-image-form.service';

describe('ProjectImage Form Service', () => {
  let service: ProjectImageFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProjectImageFormService);
  });

  describe('Service methods', () => {
    describe('createProjectImageFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProjectImageFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            imageUrl: expect.any(Object),
            caption: expect.any(Object),
            project: expect.any(Object),
          }),
        );
      });

      it('passing IProjectImage should create a new form with FormGroup', () => {
        const formGroup = service.createProjectImageFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            imageUrl: expect.any(Object),
            caption: expect.any(Object),
            project: expect.any(Object),
          }),
        );
      });
    });

    describe('getProjectImage', () => {
      it('should return NewProjectImage for default ProjectImage initial value', () => {
        const formGroup = service.createProjectImageFormGroup(sampleWithNewData);

        const projectImage = service.getProjectImage(formGroup) as any;

        expect(projectImage).toMatchObject(sampleWithNewData);
      });

      it('should return NewProjectImage for empty ProjectImage initial value', () => {
        const formGroup = service.createProjectImageFormGroup();

        const projectImage = service.getProjectImage(formGroup) as any;

        expect(projectImage).toMatchObject({});
      });

      it('should return IProjectImage', () => {
        const formGroup = service.createProjectImageFormGroup(sampleWithRequiredData);

        const projectImage = service.getProjectImage(formGroup) as any;

        expect(projectImage).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProjectImage should not enable id FormControl', () => {
        const formGroup = service.createProjectImageFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProjectImage should disable id FormControl', () => {
        const formGroup = service.createProjectImageFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
