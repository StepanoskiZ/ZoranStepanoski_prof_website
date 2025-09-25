import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { VisitorLogService } from '../service/visitor-log.service';
import { IVisitorLog } from '../visitor-log.model';
import { VisitorLogFormService } from './visitor-log-form.service';

import { VisitorLogUpdateComponent } from './visitor-log-update.component';

describe('VisitorLog Management Update Component', () => {
  let comp: VisitorLogUpdateComponent;
  let fixture: ComponentFixture<VisitorLogUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let visitorLogFormService: VisitorLogFormService;
  let visitorLogService: VisitorLogService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VisitorLogUpdateComponent],
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
      .overrideTemplate(VisitorLogUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VisitorLogUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    visitorLogFormService = TestBed.inject(VisitorLogFormService);
    visitorLogService = TestBed.inject(VisitorLogService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const visitorLog: IVisitorLog = { id: 31041 };

      activatedRoute.data = of({ visitorLog });
      comp.ngOnInit();

      expect(comp.visitorLog).toEqual(visitorLog);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVisitorLog>>();
      const visitorLog = { id: 18305 };
      jest.spyOn(visitorLogFormService, 'getVisitorLog').mockReturnValue(visitorLog);
      jest.spyOn(visitorLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ visitorLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: visitorLog }));
      saveSubject.complete();

      // THEN
      expect(visitorLogFormService.getVisitorLog).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(visitorLogService.update).toHaveBeenCalledWith(expect.objectContaining(visitorLog));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVisitorLog>>();
      const visitorLog = { id: 18305 };
      jest.spyOn(visitorLogFormService, 'getVisitorLog').mockReturnValue({ id: null });
      jest.spyOn(visitorLogService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ visitorLog: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: visitorLog }));
      saveSubject.complete();

      // THEN
      expect(visitorLogFormService.getVisitorLog).toHaveBeenCalled();
      expect(visitorLogService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVisitorLog>>();
      const visitorLog = { id: 18305 };
      jest.spyOn(visitorLogService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ visitorLog });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(visitorLogService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
