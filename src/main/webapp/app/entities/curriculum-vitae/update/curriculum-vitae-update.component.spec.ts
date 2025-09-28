import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CurriculumVitaeService } from '../service/curriculum-vitae.service';
import { ICurriculumVitae } from '../curriculum-vitae.model';
import { CurriculumVitaeFormService } from './curriculum-vitae-form.service';

import { CurriculumVitaeUpdateComponent } from './curriculum-vitae-update.component';

describe('CurriculumVitae Management Update Component', () => {
  let comp: CurriculumVitaeUpdateComponent;
  let fixture: ComponentFixture<CurriculumVitaeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let curriculumVitaeFormService: CurriculumVitaeFormService;
  let curriculumVitaeService: CurriculumVitaeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CurriculumVitaeUpdateComponent],
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
      .overrideTemplate(CurriculumVitaeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CurriculumVitaeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    curriculumVitaeFormService = TestBed.inject(CurriculumVitaeFormService);
    curriculumVitaeService = TestBed.inject(CurriculumVitaeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const curriculumVitae: ICurriculumVitae = { id: 456 };

      activatedRoute.data = of({ curriculumVitae });
      comp.ngOnInit();

      expect(comp.curriculumVitae).toEqual(curriculumVitae);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICurriculumVitae>>();
      const curriculumVitae = { id: 123 };
      jest.spyOn(curriculumVitaeFormService, 'getCurriculumVitae').mockReturnValue(curriculumVitae);
      jest.spyOn(curriculumVitaeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ curriculumVitae });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: curriculumVitae }));
      saveSubject.complete();

      // THEN
      expect(curriculumVitaeFormService.getCurriculumVitae).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(curriculumVitaeService.update).toHaveBeenCalledWith(expect.objectContaining(curriculumVitae));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICurriculumVitae>>();
      const curriculumVitae = { id: 123 };
      jest.spyOn(curriculumVitaeFormService, 'getCurriculumVitae').mockReturnValue({ id: null });
      jest.spyOn(curriculumVitaeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ curriculumVitae: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: curriculumVitae }));
      saveSubject.complete();

      // THEN
      expect(curriculumVitaeFormService.getCurriculumVitae).toHaveBeenCalled();
      expect(curriculumVitaeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICurriculumVitae>>();
      const curriculumVitae = { id: 123 };
      jest.spyOn(curriculumVitaeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ curriculumVitae });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(curriculumVitaeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
