import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IPageContent } from 'app/entities/page-content/page-content.model';
import { PageContentService } from 'app/entities/page-content/service/page-content.service';
import { PageContentMediaService } from '../service/page-content-media.service';
import { IPageContentMedia } from '../page-content-media.model';
import { PageContentMediaFormService } from './page-content-media-form.service';

import { PageContentMediaUpdateComponent } from './page-content-media-update.component';

describe('PageContentMedia Management Update Component', () => {
  let comp: PageContentMediaUpdateComponent;
  let fixture: ComponentFixture<PageContentMediaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pageContentMediaFormService: PageContentMediaFormService;
  let pageContentMediaService: PageContentMediaService;
  let pageContentService: PageContentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PageContentMediaUpdateComponent],
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
      .overrideTemplate(PageContentMediaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PageContentMediaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pageContentMediaFormService = TestBed.inject(PageContentMediaFormService);
    pageContentMediaService = TestBed.inject(PageContentMediaService);
    pageContentService = TestBed.inject(PageContentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PageContent query and add missing value', () => {
      const pageContentMedia: IPageContentMedia = { id: 456 };
      const pagecontent: IPageContent = { id: 14207 };
      pageContentMedia.pagecontent = pagecontent;

      const pageContentCollection: IPageContent[] = [{ id: 32610 }];
      jest.spyOn(pageContentService, 'query').mockReturnValue(of(new HttpResponse({ body: pageContentCollection })));
      const additionalPageContents = [pagecontent];
      const expectedCollection: IPageContent[] = [...additionalPageContents, ...pageContentCollection];
      jest.spyOn(pageContentService, 'addPageContentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pageContentMedia });
      comp.ngOnInit();

      expect(pageContentService.query).toHaveBeenCalled();
      expect(pageContentService.addPageContentToCollectionIfMissing).toHaveBeenCalledWith(
        pageContentCollection,
        ...additionalPageContents.map(expect.objectContaining),
      );
      expect(comp.pageContentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pageContentMedia: IPageContentMedia = { id: 456 };
      const pagecontent: IPageContent = { id: 21678 };
      pageContentMedia.pagecontent = pagecontent;

      activatedRoute.data = of({ pageContentMedia });
      comp.ngOnInit();

      expect(comp.pageContentsSharedCollection).toContain(pagecontent);
      expect(comp.pageContentMedia).toEqual(pageContentMedia);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPageContentMedia>>();
      const pageContentMedia = { id: 123 };
      jest.spyOn(pageContentMediaFormService, 'getPageContentMedia').mockReturnValue(pageContentMedia);
      jest.spyOn(pageContentMediaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pageContentMedia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pageContentMedia }));
      saveSubject.complete();

      // THEN
      expect(pageContentMediaFormService.getPageContentMedia).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pageContentMediaService.update).toHaveBeenCalledWith(expect.objectContaining(pageContentMedia));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPageContentMedia>>();
      const pageContentMedia = { id: 123 };
      jest.spyOn(pageContentMediaFormService, 'getPageContentMedia').mockReturnValue({ id: null });
      jest.spyOn(pageContentMediaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pageContentMedia: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pageContentMedia }));
      saveSubject.complete();

      // THEN
      expect(pageContentMediaFormService.getPageContentMedia).toHaveBeenCalled();
      expect(pageContentMediaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPageContentMedia>>();
      const pageContentMedia = { id: 123 };
      jest.spyOn(pageContentMediaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pageContentMedia });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pageContentMediaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePageContent', () => {
      it('Should forward to pageContentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(pageContentService, 'comparePageContent');
        comp.comparePageContent(entity, entity2);
        expect(pageContentService.comparePageContent).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
