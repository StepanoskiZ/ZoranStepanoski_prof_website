import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../blog-post.test-samples';

import { BlogPostFormService } from './blog-post-form.service';

describe('BlogPost Form Service', () => {
  let service: BlogPostFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BlogPostFormService);
  });

  describe('Service methods', () => {
    describe('createBlogPostFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBlogPostFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            contentHTML: expect.any(Object),
            imageUrl: expect.any(Object),
            publishedDate: expect.any(Object),
          }),
        );
      });

      it('passing IBlogPost should create a new form with FormGroup', () => {
        const formGroup = service.createBlogPostFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            contentHTML: expect.any(Object),
            imageUrl: expect.any(Object),
            publishedDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getBlogPost', () => {
      it('should return NewBlogPost for default BlogPost initial value', () => {
        const formGroup = service.createBlogPostFormGroup(sampleWithNewData);

        const blogPost = service.getBlogPost(formGroup) as any;

        expect(blogPost).toMatchObject(sampleWithNewData);
      });

      it('should return NewBlogPost for empty BlogPost initial value', () => {
        const formGroup = service.createBlogPostFormGroup();

        const blogPost = service.getBlogPost(formGroup) as any;

        expect(blogPost).toMatchObject({});
      });

      it('should return IBlogPost', () => {
        const formGroup = service.createBlogPostFormGroup(sampleWithRequiredData);

        const blogPost = service.getBlogPost(formGroup) as any;

        expect(blogPost).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBlogPost should not enable id FormControl', () => {
        const formGroup = service.createBlogPostFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBlogPost should disable id FormControl', () => {
        const formGroup = service.createBlogPostFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
