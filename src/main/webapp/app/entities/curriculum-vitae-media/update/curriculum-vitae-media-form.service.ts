import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICurriculumVitaeMedia, NewCurriculumVitaeMedia } from '../curriculum-vitae-media.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICurriculumVitaeMedia for edit and NewCurriculumVitaeMediaFormGroupInput for create.
 */
type CurriculumVitaeMediaFormGroupInput = ICurriculumVitaeMedia | PartialWithRequiredKeyOf<NewCurriculumVitaeMedia>;

type CurriculumVitaeMediaFormDefaults = Pick<NewCurriculumVitaeMedia, 'id'>;

type CurriculumVitaeMediaFormGroupContent = {
  id: FormControl<ICurriculumVitaeMedia['id'] | NewCurriculumVitaeMedia['id']>;
  mediaUrl: FormControl<ICurriculumVitaeMedia['mediaUrl']>;
  curriculumVitaeMediaType: FormControl<ICurriculumVitaeMedia['curriculumVitaeMediaType']>;
  caption: FormControl<ICurriculumVitaeMedia['caption']>;
  curriculumVitae: FormControl<ICurriculumVitaeMedia['curriculumVitae']>;
};

export type CurriculumVitaeMediaFormGroup = FormGroup<CurriculumVitaeMediaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CurriculumVitaeMediaFormService {
  createCurriculumVitaeMediaFormGroup(
    curriculumVitaeMedia: CurriculumVitaeMediaFormGroupInput = { id: null },
  ): CurriculumVitaeMediaFormGroup {
    const curriculumVitaeMediaRawValue = {
      ...this.getFormDefaults(),
      ...curriculumVitaeMedia,
    };
    return new FormGroup<CurriculumVitaeMediaFormGroupContent>({
      id: new FormControl(
        { value: curriculumVitaeMediaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      mediaUrl: new FormControl(curriculumVitaeMediaRawValue.mediaUrl, {
        validators: [Validators.required],
      }),
      curriculumVitaeMediaType: new FormControl(curriculumVitaeMediaRawValue.curriculumVitaeMediaType, {
        validators: [Validators.required],
      }),
      caption: new FormControl(curriculumVitaeMediaRawValue.caption),
      curriculumVitae: new FormControl(curriculumVitaeMediaRawValue.curriculumVitae),
    });
  }

  getCurriculumVitaeMedia(form: CurriculumVitaeMediaFormGroup): ICurriculumVitaeMedia | NewCurriculumVitaeMedia {
    return form.getRawValue() as ICurriculumVitaeMedia | NewCurriculumVitaeMedia;
  }

  resetForm(form: CurriculumVitaeMediaFormGroup, curriculumVitaeMedia: CurriculumVitaeMediaFormGroupInput): void {
    const curriculumVitaeMediaRawValue = { ...this.getFormDefaults(), ...curriculumVitaeMedia };
    form.reset(
      {
        ...curriculumVitaeMediaRawValue,
        id: { value: curriculumVitaeMediaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CurriculumVitaeMediaFormDefaults {
    return {
      id: null,
    };
  }
}
