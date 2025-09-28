import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBlogPost, NewBlogPost } from '../blog-post.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBlogPost for edit and NewBlogPostFormGroupInput for create.
 */
type BlogPostFormGroupInput = IBlogPost | PartialWithRequiredKeyOf<NewBlogPost>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBlogPost | NewBlogPost> = Omit<T, 'publishedDate'> & {
  publishedDate?: string | null;
};

type BlogPostFormRawValue = FormValueOf<IBlogPost>;

type NewBlogPostFormRawValue = FormValueOf<NewBlogPost>;

type BlogPostFormDefaults = Pick<NewBlogPost, 'id' | 'publishedDate'>;

type BlogPostFormGroupContent = {
  id: FormControl<BlogPostFormRawValue['id'] | NewBlogPost['id']>;
  title: FormControl<BlogPostFormRawValue['title']>;
  contentHTML: FormControl<BlogPostFormRawValue['contentHTML']>;
  imageUrl: FormControl<BlogPostFormRawValue['imageUrl']>;
  publishedDate: FormControl<BlogPostFormRawValue['publishedDate']>;
};

export type BlogPostFormGroup = FormGroup<BlogPostFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BlogPostFormService {
  createBlogPostFormGroup(blogPost: BlogPostFormGroupInput = { id: null }): BlogPostFormGroup {
    const blogPostRawValue = this.convertBlogPostToBlogPostRawValue({
      ...this.getFormDefaults(),
      ...blogPost,
    });
    return new FormGroup<BlogPostFormGroupContent>({
      id: new FormControl(
        { value: blogPostRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(blogPostRawValue.title, {
        validators: [Validators.required],
      }),
      contentHTML: new FormControl(blogPostRawValue.contentHTML, {
        validators: [Validators.required],
      }),
      imageUrl: new FormControl(blogPostRawValue.imageUrl),
      publishedDate: new FormControl(blogPostRawValue.publishedDate, {
        validators: [Validators.required],
      }),
    });
  }

  getBlogPost(form: BlogPostFormGroup): IBlogPost | NewBlogPost {
    return this.convertBlogPostRawValueToBlogPost(form.getRawValue() as BlogPostFormRawValue | NewBlogPostFormRawValue);
  }

  resetForm(form: BlogPostFormGroup, blogPost: BlogPostFormGroupInput): void {
    const blogPostRawValue = this.convertBlogPostToBlogPostRawValue({ ...this.getFormDefaults(), ...blogPost });
    form.reset(
      {
        ...blogPostRawValue,
        id: { value: blogPostRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BlogPostFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      publishedDate: currentTime,
    };
  }

  private convertBlogPostRawValueToBlogPost(rawBlogPost: BlogPostFormRawValue | NewBlogPostFormRawValue): IBlogPost | NewBlogPost {
    return {
      ...rawBlogPost,
      publishedDate: dayjs(rawBlogPost.publishedDate, DATE_TIME_FORMAT),
    };
  }

  private convertBlogPostToBlogPostRawValue(
    blogPost: IBlogPost | (Partial<NewBlogPost> & BlogPostFormDefaults),
  ): BlogPostFormRawValue | PartialWithRequiredKeyOf<NewBlogPostFormRawValue> {
    return {
      ...blogPost,
      publishedDate: blogPost.publishedDate ? blogPost.publishedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
