import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPageContentMedia, NewPageContentMedia } from '../page-content-media.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPageContentMedia for edit and NewPageContentMediaFormGroupInput for create.
 */
type PageContentMediaFormGroupInput = IPageContentMedia | PartialWithRequiredKeyOf<NewPageContentMedia>;

type PageContentMediaFormDefaults = Pick<NewPageContentMedia, 'id'>;

type PageContentMediaFormGroupContent = {
  id: FormControl<IPageContentMedia['id'] | NewPageContentMedia['id']>;
  mediaUrl: FormControl<IPageContentMedia['mediaUrl']>;
  pageContentMediaType: FormControl<IPageContentMedia['pageContentMediaType']>;
  caption: FormControl<IPageContentMedia['caption']>;
  pagecontent: FormControl<IPageContentMedia['pagecontent']>;
};

export type PageContentMediaFormGroup = FormGroup<PageContentMediaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PageContentMediaFormService {
  createPageContentMediaFormGroup(pageContentMedia: PageContentMediaFormGroupInput = { id: null }): PageContentMediaFormGroup {
    const pageContentMediaRawValue = {
      ...this.getFormDefaults(),
      ...pageContentMedia,
    };
    return new FormGroup<PageContentMediaFormGroupContent>({
      id: new FormControl(
        { value: pageContentMediaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      mediaUrl: new FormControl(pageContentMediaRawValue.mediaUrl, {
        validators: [Validators.required],
      }),
      pageContentMediaType: new FormControl(pageContentMediaRawValue.pageContentMediaType, {
        validators: [Validators.required],
      }),
      caption: new FormControl(pageContentMediaRawValue.caption),
      pagecontent: new FormControl(pageContentMediaRawValue.pagecontent),
    });
  }

  getPageContentMedia(form: PageContentMediaFormGroup): IPageContentMedia | NewPageContentMedia {
    return form.getRawValue() as IPageContentMedia | NewPageContentMedia;
  }

  resetForm(form: PageContentMediaFormGroup, pageContentMedia: PageContentMediaFormGroupInput): void {
    const pageContentMediaRawValue = { ...this.getFormDefaults(), ...pageContentMedia };
    form.reset(
      {
        ...pageContentMediaRawValue,
        id: { value: pageContentMediaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PageContentMediaFormDefaults {
    return {
      id: null,
    };
  }
}
