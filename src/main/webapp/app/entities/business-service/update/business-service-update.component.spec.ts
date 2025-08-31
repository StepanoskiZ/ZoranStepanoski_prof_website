import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { BusinessServiceService } from '../service/business-service.service';
import { IBusinessService } from '../business-service.model';
import { BusinessServiceFormService } from './business-service-form.service';

import { BusinessServiceUpdateComponent } from './business-service-update.component';

describe('BusinessService Management Update Component', () => {
  let comp: BusinessServiceUpdateComponent;
  let fixture: ComponentFixture<BusinessServiceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let businessServiceFormService: BusinessServiceFormService;
  let businessServiceService: BusinessServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BusinessServiceUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(BusinessServiceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BusinessServiceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    businessServiceFormService = TestBed.inject(BusinessServiceFormService);
    businessServiceService = TestBed.inject(BusinessServiceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const businessService: IBusinessService = { id: 13532 };

      activatedRoute.data = of({ businessService });
      comp.ngOnInit();

      expect(comp.businessService).toEqual(businessService);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBusinessService>>();
      const businessService = { id: 917 };
      jest.spyOn(businessServiceFormService, 'getBusinessService').mockReturnValue(businessService);
      jest.spyOn(businessServiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: businessService }));
      saveSubject.complete();

      // THEN
      expect(businessServiceFormService.getBusinessService).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(businessServiceService.update).toHaveBeenCalledWith(expect.objectContaining(businessService));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBusinessService>>();
      const businessService = { id: 917 };
      jest.spyOn(businessServiceFormService, 'getBusinessService').mockReturnValue({ id: null });
      jest.spyOn(businessServiceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessService: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: businessService }));
      saveSubject.complete();

      // THEN
      expect(businessServiceFormService.getBusinessService).toHaveBeenCalled();
      expect(businessServiceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBusinessService>>();
      const businessService = { id: 917 };
      jest.spyOn(businessServiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ businessService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(businessServiceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
