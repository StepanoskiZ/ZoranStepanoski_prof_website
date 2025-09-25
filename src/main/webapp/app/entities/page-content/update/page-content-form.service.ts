import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPageContent, NewPageContent } from '../page-content.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPageContent for edit and NewPageContentFormGroupInput for create.
 */
type PageContentFormGroupInput = IPageContent | PartialWithRequiredKeyOf<NewPageContent>;

type PageContentFormDefaults = Pick<NewPageContent, 'id'>;

type PageContentFormGroupContent = {
  id: FormControl<IPageContent['id'] | NewPageContent['id']>;
  sectionKey: FormControl<IPageContent['sectionKey']>;
  contentHtml: FormControl<IPageContent['contentHtml']>;
};

export type PageContentFormGroup = FormGroup<PageContentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PageContentFormService {
  createPageContentFormGroup(pageContent: PageContentFormGroupInput = { id: null }): PageContentFormGroup {
    const pageContentRawValue = {
      ...this.getFormDefaults(),
      ...pageContent,
    };
    return new FormGroup<PageContentFormGroupContent>({
      id: new FormControl(
        { value: pageContentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sectionKey: new FormControl(pageContentRawValue.sectionKey, {
        validators: [Validators.required],
      }),
      contentHtml: new FormControl(pageContentRawValue.contentHtml, {
        validators: [Validators.required],
      }),
    });
  }

  getPageContent(form: PageContentFormGroup): IPageContent | NewPageContent {
    return form.getRawValue() as IPageContent | NewPageContent;
  }

  resetForm(form: PageContentFormGroup, pageContent: PageContentFormGroupInput): void {
    const pageContentRawValue = { ...this.getFormDefaults(), ...pageContent };
    form.reset(
      {
        ...pageContentRawValue,
        id: { value: pageContentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PageContentFormDefaults {
    return {
      id: null,
    };
  }
}
