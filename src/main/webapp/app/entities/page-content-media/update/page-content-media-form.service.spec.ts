import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../page-content-media.test-samples';

import { PageContentMediaFormService } from './page-content-media-form.service';

describe('PageContentMedia Form Service', () => {
  let service: PageContentMediaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PageContentMediaFormService);
  });

  describe('Service methods', () => {
    describe('createPageContentMediaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPageContentMediaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mediaUrl: expect.any(Object),
            pageContentMediaType: expect.any(Object),
            caption: expect.any(Object),
            pagecontent: expect.any(Object),
          }),
        );
      });

      it('passing IPageContentMedia should create a new form with FormGroup', () => {
        const formGroup = service.createPageContentMediaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mediaUrl: expect.any(Object),
            pageContentMediaType: expect.any(Object),
            caption: expect.any(Object),
            pagecontent: expect.any(Object),
          }),
        );
      });
    });

    describe('getPageContentMedia', () => {
      it('should return NewPageContentMedia for default PageContentMedia initial value', () => {
        const formGroup = service.createPageContentMediaFormGroup(sampleWithNewData);

        const pageContentMedia = service.getPageContentMedia(formGroup) as any;

        expect(pageContentMedia).toMatchObject(sampleWithNewData);
      });

      it('should return NewPageContentMedia for empty PageContentMedia initial value', () => {
        const formGroup = service.createPageContentMediaFormGroup();

        const pageContentMedia = service.getPageContentMedia(formGroup) as any;

        expect(pageContentMedia).toMatchObject({});
      });

      it('should return IPageContentMedia', () => {
        const formGroup = service.createPageContentMediaFormGroup(sampleWithRequiredData);

        const pageContentMedia = service.getPageContentMedia(formGroup) as any;

        expect(pageContentMedia).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPageContentMedia should not enable id FormControl', () => {
        const formGroup = service.createPageContentMediaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPageContentMedia should disable id FormControl', () => {
        const formGroup = service.createPageContentMediaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
