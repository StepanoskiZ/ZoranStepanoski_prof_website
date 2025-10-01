import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IBusinessService } from 'app/entities/business-service/business-service.model';
import { BusinessServiceService } from 'app/entities/business-service/service/business-service.service';
import { BusinessServiceMediaService } from '../service/business-service-media.service';
import { IBusinessServiceMedia } from '../business-service-media.model';
import { BusinessServiceMediaFormService } from './business-service-media-form.service';

import { BusinessServiceMediaUpdateComponent } from './business-service-media-update.component';

describe('BusinessServiceMedia Management Update Component', () => {
  let comp: BusinessServiceMediaUpdateComponent;
  let fixture: ComponentFixture<BusinessServiceMediaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let businessServiceMediaFormService: BusinessServiceMediaFormService;
  let businessServiceMediaService: BusinessServiceMediaService;
  let businessServiceService: BusinessServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), BusinessServiceMediaUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(BusinessServiceMediaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BusinessServiceMediaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    businessServiceMediaFormService = TestBed.inject(BusinessServiceMediaFormService);
    businessServiceMediaService = TestBed.inject(BusinessServiceMediaService);
    businessServiceService = TestBed.inject(BusinessServiceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call BusinessService query and add missing value', () => {
      const businessServiceMedia: IBusinessServiceMedia = { id: 456 };
      const businessService: IBusinessService = { id: 21834 };
      businessServiceMedia.businessService = businessService;

      const businessServiceCollection: IBusinessService[] = [{ id: 4231 }];
      jest.spyOn(businessServiceService, 'query').mockReturnValue(of(new HttpResponse({ body: businessServiceCollection })));
      const additionalBusinessServices = [businessService];
      const expectedCollection: IBusinessService[] = [...additionalBusinessServices, ...businessServiceCollection];
      jest.spyOn(businessServiceService, 'addBusinessServiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ businessServiceMedia });
      comp.ngOnInit();

      expect(businessServiceService.query).toHaveBeenCalled();
      expect(businessServiceService.addBusinessServiceToCollectionIfMissing).toHaveBeenCalledWith(
        businessServiceCollection,
        ...additionalBusinessServices.map(expect.objectContaining),
      );
      expect(comp.businessServicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const businessServiceMedia: IBusinessServiceMedia = { id: 456 };
      const businessService: IBusinessService = { id: 72 };
      businessServiceMedia.businessService = businessService;

      activatedRoute.data = of({ businessServiceMedia });
      comp.ngOnInit();

      expect(comp.businessServicesSharedCollection).toContain(businessService);
      expect(comp.businessServiceMedia).toEqual(businessServiceMedia);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBusinessServiceMedia>>();
      const businessServiceMedia = { id: 123 };
      jest.spyOn(businessServiceMediaFormService, 'getBusinessServiceMedia').mockReturnValue(businessServiceMedia);
      jest.spyOn(businessServiceMediaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessServiceMedia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: businessServiceMedia }));
      saveSubject.complete();

      // THEN
      expect(businessServiceMediaFormService.getBusinessServiceMedia).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(businessServiceMediaService.update).toHaveBeenCalledWith(expect.objectContaining(businessServiceMedia));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBusinessServiceMedia>>();
      const businessServiceMedia = { id: 123 };
      jest.spyOn(businessServiceMediaFormService, 'getBusinessServiceMedia').mockReturnValue({ id: null });
      jest.spyOn(businessServiceMediaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessServiceMedia: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: businessServiceMedia }));
      saveSubject.complete();

      // THEN
      expect(businessServiceMediaFormService.getBusinessServiceMedia).toHaveBeenCalled();
      expect(businessServiceMediaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBusinessServiceMedia>>();
      const businessServiceMedia = { id: 123 };
      jest.spyOn(businessServiceMediaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessServiceMedia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(businessServiceMediaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBusinessService', () => {
      it('Should forward to businessServiceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(businessServiceService, 'compareBusinessService');
        comp.compareBusinessService(entity, entity2);
        expect(businessServiceService.compareBusinessService).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
