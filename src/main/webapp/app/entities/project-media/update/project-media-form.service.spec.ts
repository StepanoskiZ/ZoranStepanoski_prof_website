import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../project-media.test-samples';

import { ProjectMediaFormService } from './project-media-form.service';

describe('ProjectMedia Form Service', () => {
  let service: ProjectMediaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProjectMediaFormService);
  });

  describe('Service methods', () => {
    describe('createProjectMediaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProjectMediaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mediaUrl: expect.any(Object),
            projectMediaType: expect.any(Object),
            caption: expect.any(Object),
            project: expect.any(Object),
          }),
        );
      });

      it('passing IProjectMedia should create a new form with FormGroup', () => {
        const formGroup = service.createProjectMediaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mediaUrl: expect.any(Object),
            projectMediaType: expect.any(Object),
            caption: expect.any(Object),
            project: expect.any(Object),
          }),
        );
      });
    });

    describe('getProjectMedia', () => {
      it('should return NewProjectMedia for default ProjectMedia initial value', () => {
        const formGroup = service.createProjectMediaFormGroup(sampleWithNewData);

        const projectMedia = service.getProjectMedia(formGroup) as any;

        expect(projectMedia).toMatchObject(sampleWithNewData);
      });

      it('should return NewProjectMedia for empty ProjectMedia initial value', () => {
        const formGroup = service.createProjectMediaFormGroup();

        const projectMedia = service.getProjectMedia(formGroup) as any;

        expect(projectMedia).toMatchObject({});
      });

      it('should return IProjectMedia', () => {
        const formGroup = service.createProjectMediaFormGroup(sampleWithRequiredData);

        const projectMedia = service.getProjectMedia(formGroup) as any;

        expect(projectMedia).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProjectMedia should not enable id FormControl', () => {
        const formGroup = service.createProjectMediaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProjectMedia should disable id FormControl', () => {
        const formGroup = service.createProjectMediaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
