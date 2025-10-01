import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ICurriculumVitae } from 'app/entities/curriculum-vitae/curriculum-vitae.model';
import { CurriculumVitaeService } from 'app/entities/curriculum-vitae/service/curriculum-vitae.service';
import { CurriculumVitaeMediaService } from '../service/curriculum-vitae-media.service';
import { ICurriculumVitaeMedia } from '../curriculum-vitae-media.model';
import { CurriculumVitaeMediaFormService } from './curriculum-vitae-media-form.service';

import { CurriculumVitaeMediaUpdateComponent } from './curriculum-vitae-media-update.component';

describe('CurriculumVitaeMedia Management Update Component', () => {
  let comp: CurriculumVitaeMediaUpdateComponent;
  let fixture: ComponentFixture<CurriculumVitaeMediaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let curriculumVitaeMediaFormService: CurriculumVitaeMediaFormService;
  let curriculumVitaeMediaService: CurriculumVitaeMediaService;
  let curriculumVitaeService: CurriculumVitaeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CurriculumVitaeMediaUpdateComponent],
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
      .overrideTemplate(CurriculumVitaeMediaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CurriculumVitaeMediaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    curriculumVitaeMediaFormService = TestBed.inject(CurriculumVitaeMediaFormService);
    curriculumVitaeMediaService = TestBed.inject(CurriculumVitaeMediaService);
    curriculumVitaeService = TestBed.inject(CurriculumVitaeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CurriculumVitae query and add missing value', () => {
      const curriculumVitaeMedia: ICurriculumVitaeMedia = { id: 456 };
      const curriculumVitae: ICurriculumVitae = { id: 15449 };
      curriculumVitaeMedia.curriculumVitae = curriculumVitae;

      const curriculumVitaeCollection: ICurriculumVitae[] = [{ id: 10986 }];
      jest.spyOn(curriculumVitaeService, 'query').mockReturnValue(of(new HttpResponse({ body: curriculumVitaeCollection })));
      const additionalCurriculumVitaes = [curriculumVitae];
      const expectedCollection: ICurriculumVitae[] = [...additionalCurriculumVitaes, ...curriculumVitaeCollection];
      jest.spyOn(curriculumVitaeService, 'addCurriculumVitaeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ curriculumVitaeMedia });
      comp.ngOnInit();

      expect(curriculumVitaeService.query).toHaveBeenCalled();
      expect(curriculumVitaeService.addCurriculumVitaeToCollectionIfMissing).toHaveBeenCalledWith(
        curriculumVitaeCollection,
        ...additionalCurriculumVitaes.map(expect.objectContaining),
      );
      expect(comp.curriculumVitaesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const curriculumVitaeMedia: ICurriculumVitaeMedia = { id: 456 };
      const curriculumVitae: ICurriculumVitae = { id: 23928 };
      curriculumVitaeMedia.curriculumVitae = curriculumVitae;

      activatedRoute.data = of({ curriculumVitaeMedia });
      comp.ngOnInit();

      expect(comp.curriculumVitaesSharedCollection).toContain(curriculumVitae);
      expect(comp.curriculumVitaeMedia).toEqual(curriculumVitaeMedia);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICurriculumVitaeMedia>>();
      const curriculumVitaeMedia = { id: 123 };
      jest.spyOn(curriculumVitaeMediaFormService, 'getCurriculumVitaeMedia').mockReturnValue(curriculumVitaeMedia);
      jest.spyOn(curriculumVitaeMediaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ curriculumVitaeMedia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: curriculumVitaeMedia }));
      saveSubject.complete();

      // THEN
      expect(curriculumVitaeMediaFormService.getCurriculumVitaeMedia).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(curriculumVitaeMediaService.update).toHaveBeenCalledWith(expect.objectContaining(curriculumVitaeMedia));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICurriculumVitaeMedia>>();
      const curriculumVitaeMedia = { id: 123 };
      jest.spyOn(curriculumVitaeMediaFormService, 'getCurriculumVitaeMedia').mockReturnValue({ id: null });
      jest.spyOn(curriculumVitaeMediaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ curriculumVitaeMedia: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: curriculumVitaeMedia }));
      saveSubject.complete();

      // THEN
      expect(curriculumVitaeMediaFormService.getCurriculumVitaeMedia).toHaveBeenCalled();
      expect(curriculumVitaeMediaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICurriculumVitaeMedia>>();
      const curriculumVitaeMedia = { id: 123 };
      jest.spyOn(curriculumVitaeMediaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ curriculumVitaeMedia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(curriculumVitaeMediaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCurriculumVitae', () => {
      it('Should forward to curriculumVitaeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(curriculumVitaeService, 'compareCurriculumVitae');
        comp.compareCurriculumVitae(entity, entity2);
        expect(curriculumVitaeService.compareCurriculumVitae).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
