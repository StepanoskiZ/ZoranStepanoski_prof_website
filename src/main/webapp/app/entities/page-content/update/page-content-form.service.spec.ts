import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../page-content.test-samples';

import { PageContentFormService } from './page-content-form.service';

describe('PageContent Form Service', () => {
  let service: PageContentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PageContentFormService);
  });

  describe('Service methods', () => {
    describe('createPageContentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPageContentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sectionKey: expect.any(Object),
            contentHtml: expect.any(Object),
          }),
        );
      });

      it('passing IPageContent should create a new form with FormGroup', () => {
        const formGroup = service.createPageContentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            sectionKey: expect.any(Object),
            contentHtml: expect.any(Object),
          }),
        );
      });
    });

    describe('getPageContent', () => {
      it('should return NewPageContent for default PageContent initial value', () => {
        const formGroup = service.createPageContentFormGroup(sampleWithNewData);

        const pageContent = service.getPageContent(formGroup) as any;

        expect(pageContent).toMatchObject(sampleWithNewData);
      });

      it('should return NewPageContent for empty PageContent initial value', () => {
        const formGroup = service.createPageContentFormGroup();

        const pageContent = service.getPageContent(formGroup) as any;

        expect(pageContent).toMatchObject({});
      });

      it('should return IPageContent', () => {
        const formGroup = service.createPageContentFormGroup(sampleWithRequiredData);

        const pageContent = service.getPageContent(formGroup) as any;

        expect(pageContent).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPageContent should not enable id FormControl', () => {
        const formGroup = service.createPageContentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPageContent should disable id FormControl', () => {
        const formGroup = service.createPageContentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
