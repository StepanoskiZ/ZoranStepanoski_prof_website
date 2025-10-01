import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBusinessServiceMedia, NewBusinessServiceMedia } from '../business-service-media.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBusinessServiceMedia for edit and NewBusinessServiceMediaFormGroupInput for create.
 */
type BusinessServiceMediaFormGroupInput = IBusinessServiceMedia | PartialWithRequiredKeyOf<NewBusinessServiceMedia>;

type BusinessServiceMediaFormDefaults = Pick<NewBusinessServiceMedia, 'id'>;

type BusinessServiceMediaFormGroupContent = {
  id: FormControl<IBusinessServiceMedia['id'] | NewBusinessServiceMedia['id']>;
  mediaUrl: FormControl<IBusinessServiceMedia['mediaUrl']>;
  businessServiceMediaType: FormControl<IBusinessServiceMedia['businessServiceMediaType']>;
  caption: FormControl<IBusinessServiceMedia['caption']>;
  businessService: FormControl<IBusinessServiceMedia['businessService']>;
};

export type BusinessServiceMediaFormGroup = FormGroup<BusinessServiceMediaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BusinessServiceMediaFormService {
  createBusinessServiceMediaFormGroup(
    businessServiceMedia: BusinessServiceMediaFormGroupInput = { id: null },
  ): BusinessServiceMediaFormGroup {
    const businessServiceMediaRawValue = {
      ...this.getFormDefaults(),
      ...businessServiceMedia,
    };
    return new FormGroup<BusinessServiceMediaFormGroupContent>({
      id: new FormControl(
        { value: businessServiceMediaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      mediaUrl: new FormControl(businessServiceMediaRawValue.mediaUrl, {
        validators: [Validators.required],
      }),
      businessServiceMediaType: new FormControl(businessServiceMediaRawValue.businessServiceMediaType, {
        validators: [Validators.required],
      }),
      caption: new FormControl(businessServiceMediaRawValue.caption),
      businessService: new FormControl(businessServiceMediaRawValue.businessService),
    });
  }

  getBusinessServiceMedia(form: BusinessServiceMediaFormGroup): IBusinessServiceMedia | NewBusinessServiceMedia {
    return form.getRawValue() as IBusinessServiceMedia | NewBusinessServiceMedia;
  }

  resetForm(form: BusinessServiceMediaFormGroup, businessServiceMedia: BusinessServiceMediaFormGroupInput): void {
    const businessServiceMediaRawValue = { ...this.getFormDefaults(), ...businessServiceMedia };
    form.reset(
      {
        ...businessServiceMediaRawValue,
        id: { value: businessServiceMediaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BusinessServiceMediaFormDefaults {
    return {
      id: null,
    };
  }
}
