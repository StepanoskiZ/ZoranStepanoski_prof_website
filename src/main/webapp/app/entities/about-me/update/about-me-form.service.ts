import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAboutMe, NewAboutMe } from '../about-me.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAboutMe for edit and NewAboutMeFormGroupInput for create.
 */
type AboutMeFormGroupInput = IAboutMe | PartialWithRequiredKeyOf<NewAboutMe>;

type AboutMeFormDefaults = Pick<NewAboutMe, 'id'>;

type AboutMeFormGroupContent = {
  id: FormControl<IAboutMe['id'] | NewAboutMe['id']>;
  contentHtml: FormControl<IAboutMe['contentHtml']>;
  language: FormControl<IAboutMe['language']>;
};

export type AboutMeFormGroup = FormGroup<AboutMeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AboutMeFormService {
  createAboutMeFormGroup(aboutMe: AboutMeFormGroupInput = { id: null }): AboutMeFormGroup {
    const aboutMeRawValue = {
      ...this.getFormDefaults(),
      ...aboutMe,
    };
    return new FormGroup<AboutMeFormGroupContent>({
      id: new FormControl(
        { value: aboutMeRawValue.id, disabled: true },
//         {
//           nonNullable: true,
//           validators: [Validators.required],
//         },
      ),
      contentHtml: new FormControl(aboutMeRawValue.contentHtml, {
        validators: [Validators.required],
      }),
      language: new FormControl(aboutMeRawValue.language, {
        validators: [Validators.required],
      }),
    });
  }

  getAboutMe(form: AboutMeFormGroup): IAboutMe | NewAboutMe {
    return form.getRawValue() as IAboutMe | NewAboutMe;
  }

  resetForm(form: AboutMeFormGroup, aboutMe: AboutMeFormGroupInput): void {
    const aboutMeRawValue = { ...this.getFormDefaults(), ...aboutMe };
    form.reset(
      {
        ...aboutMeRawValue,
        id: { value: aboutMeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AboutMeFormDefaults {
    return {
      id: null,
    };
  }
}
