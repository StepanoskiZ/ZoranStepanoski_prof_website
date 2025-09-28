import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BlogPostService } from '../service/blog-post.service';
import { IBlogPost } from '../blog-post.model';
import { BlogPostFormService } from './blog-post-form.service';

import { BlogPostUpdateComponent } from './blog-post-update.component';

describe('BlogPost Management Update Component', () => {
  let comp: BlogPostUpdateComponent;
  let fixture: ComponentFixture<BlogPostUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let blogPostFormService: BlogPostFormService;
  let blogPostService: BlogPostService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), BlogPostUpdateComponent],
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
      .overrideTemplate(BlogPostUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BlogPostUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    blogPostFormService = TestBed.inject(BlogPostFormService);
    blogPostService = TestBed.inject(BlogPostService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const blogPost: IBlogPost = { id: 456 };

      activatedRoute.data = of({ blogPost });
      comp.ngOnInit();

      expect(comp.blogPost).toEqual(blogPost);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBlogPost>>();
      const blogPost = { id: 123 };
      jest.spyOn(blogPostFormService, 'getBlogPost').mockReturnValue(blogPost);
      jest.spyOn(blogPostService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ blogPost });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: blogPost }));
      saveSubject.complete();

      // THEN
      expect(blogPostFormService.getBlogPost).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(blogPostService.update).toHaveBeenCalledWith(expect.objectContaining(blogPost));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBlogPost>>();
      const blogPost = { id: 123 };
      jest.spyOn(blogPostFormService, 'getBlogPost').mockReturnValue({ id: null });
      jest.spyOn(blogPostService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ blogPost: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: blogPost }));
      saveSubject.complete();

      // THEN
      expect(blogPostFormService.getBlogPost).toHaveBeenCalled();
      expect(blogPostService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBlogPost>>();
      const blogPost = { id: 123 };
      jest.spyOn(blogPostService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ blogPost });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(blogPostService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
