import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { PageContentService } from '../service/page-content.service';
import { IPageContent } from '../page-content.model';
import { PageContentFormService } from './page-content-form.service';

import { PageContentUpdateComponent } from './page-content-update.component';

describe('PageContent Management Update Component', () => {
  let comp: PageContentUpdateComponent;
  let fixture: ComponentFixture<PageContentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pageContentFormService: PageContentFormService;
  let pageContentService: PageContentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PageContentUpdateComponent],
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
      .overrideTemplate(PageContentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PageContentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pageContentFormService = TestBed.inject(PageContentFormService);
    pageContentService = TestBed.inject(PageContentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const pageContent: IPageContent = { id: 7034 };

      activatedRoute.data = of({ pageContent });
      comp.ngOnInit();

      expect(comp.pageContent).toEqual(pageContent);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPageContent>>();
      const pageContent = { id: 27977 };
      jest.spyOn(pageContentFormService, 'getPageContent').mockReturnValue(pageContent);
      jest.spyOn(pageContentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pageContent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pageContent }));
      saveSubject.complete();

      // THEN
      expect(pageContentFormService.getPageContent).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pageContentService.update).toHaveBeenCalledWith(expect.objectContaining(pageContent));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPageContent>>();
      const pageContent = { id: 27977 };
      jest.spyOn(pageContentFormService, 'getPageContent').mockReturnValue({ id: null });
      jest.spyOn(pageContentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pageContent: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pageContent }));
      saveSubject.complete();

      // THEN
      expect(pageContentFormService.getPageContent).toHaveBeenCalled();
      expect(pageContentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPageContent>>();
      const pageContent = { id: 27977 };
      jest.spyOn(pageContentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pageContent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pageContentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
