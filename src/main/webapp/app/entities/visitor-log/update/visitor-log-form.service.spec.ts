import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../visitor-log.test-samples';

import { VisitorLogFormService } from './visitor-log-form.service';

describe('VisitorLog Form Service', () => {
  let service: VisitorLogFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VisitorLogFormService);
  });

  describe('Service methods', () => {
    describe('createVisitorLogFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVisitorLogFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ipAddress: expect.any(Object),
            pageVisited: expect.any(Object),
            userAgent: expect.any(Object),
            visitTimestamp: expect.any(Object),
          }),
        );
      });

      it('passing IVisitorLog should create a new form with FormGroup', () => {
        const formGroup = service.createVisitorLogFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ipAddress: expect.any(Object),
            pageVisited: expect.any(Object),
            userAgent: expect.any(Object),
            visitTimestamp: expect.any(Object),
          }),
        );
      });
    });

    describe('getVisitorLog', () => {
      it('should return NewVisitorLog for default VisitorLog initial value', () => {
        const formGroup = service.createVisitorLogFormGroup(sampleWithNewData);

        const visitorLog = service.getVisitorLog(formGroup) as any;

        expect(visitorLog).toMatchObject(sampleWithNewData);
      });

      it('should return NewVisitorLog for empty VisitorLog initial value', () => {
        const formGroup = service.createVisitorLogFormGroup();

        const visitorLog = service.getVisitorLog(formGroup) as any;

        expect(visitorLog).toMatchObject({});
      });

      it('should return IVisitorLog', () => {
        const formGroup = service.createVisitorLogFormGroup(sampleWithRequiredData);

        const visitorLog = service.getVisitorLog(formGroup) as any;

        expect(visitorLog).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVisitorLog should not enable id FormControl', () => {
        const formGroup = service.createVisitorLogFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVisitorLog should disable id FormControl', () => {
        const formGroup = service.createVisitorLogFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
