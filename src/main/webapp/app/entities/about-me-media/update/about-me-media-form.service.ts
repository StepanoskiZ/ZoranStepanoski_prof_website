import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAboutMeMedia, NewAboutMeMedia } from '../about-me-media.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAboutMeMedia for edit and NewAboutMeMediaFormGroupInput for create.
 */
type AboutMeMediaFormGroupInput = IAboutMeMedia | PartialWithRequiredKeyOf<NewAboutMeMedia>;

type AboutMeMediaFormDefaults = Pick<NewAboutMeMedia, 'id'>;

type AboutMeMediaFormGroupContent = {
  id: FormControl<IAboutMeMedia['id'] | NewAboutMeMedia['id']>;
  mediaUrl: FormControl<IAboutMeMedia['mediaUrl']>;
  aboutMeMediaType: FormControl<IAboutMeMedia['aboutMeMediaType']>;
  caption: FormControl<IAboutMeMedia['caption']>;
  aboutMe: FormControl<IAboutMeMedia['aboutMe']>;
};

export type AboutMeMediaFormGroup = FormGroup<AboutMeMediaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AboutMeMediaFormService {
  createAboutMeMediaFormGroup(aboutMeMedia: AboutMeMediaFormGroupInput = { id: null }): AboutMeMediaFormGroup {
    const aboutMeMediaRawValue = {
      ...this.getFormDefaults(),
      ...aboutMeMedia,
    };
    return new FormGroup<AboutMeMediaFormGroupContent>({
      id: new FormControl(
        { value: aboutMeMediaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      mediaUrl: new FormControl(aboutMeMediaRawValue.mediaUrl, {
        validators: [Validators.required],
      }),
      aboutMeMediaType: new FormControl(aboutMeMediaRawValue.aboutMeMediaType, {
        validators: [Validators.required],
      }),
      caption: new FormControl(aboutMeMediaRawValue.caption),
      aboutMe: new FormControl(aboutMeMediaRawValue.aboutMe),
    });
  }

  getAboutMeMedia(form: AboutMeMediaFormGroup): IAboutMeMedia | NewAboutMeMedia {
    return form.getRawValue() as IAboutMeMedia | NewAboutMeMedia;
  }

  resetForm(form: AboutMeMediaFormGroup, aboutMeMedia: AboutMeMediaFormGroupInput): void {
    const aboutMeMediaRawValue = { ...this.getFormDefaults(), ...aboutMeMedia };
    form.reset(
      {
        ...aboutMeMediaRawValue,
        id: { value: aboutMeMediaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AboutMeMediaFormDefaults {
    return {
      id: null,
    };
  }
}
