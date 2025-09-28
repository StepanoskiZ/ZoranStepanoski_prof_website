import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IContactMessage, NewContactMessage } from '../contact-message.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContactMessage for edit and NewContactMessageFormGroupInput for create.
 */
type ContactMessageFormGroupInput = IContactMessage | PartialWithRequiredKeyOf<NewContactMessage>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IContactMessage | NewContactMessage> = Omit<T, 'submittedDate'> & {
  submittedDate?: string | null;
};

type ContactMessageFormRawValue = FormValueOf<IContactMessage>;

type NewContactMessageFormRawValue = FormValueOf<NewContactMessage>;

type ContactMessageFormDefaults = Pick<NewContactMessage, 'id' | 'submittedDate'>;

type ContactMessageFormGroupContent = {
  id: FormControl<ContactMessageFormRawValue['id'] | NewContactMessage['id']>;
  visitorName: FormControl<ContactMessageFormRawValue['visitorName']>;
  visitorEmail: FormControl<ContactMessageFormRawValue['visitorEmail']>;
  message: FormControl<ContactMessageFormRawValue['message']>;
  submittedDate: FormControl<ContactMessageFormRawValue['submittedDate']>;
};

export type ContactMessageFormGroup = FormGroup<ContactMessageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContactMessageFormService {
  createContactMessageFormGroup(contactMessage: ContactMessageFormGroupInput = { id: null }): ContactMessageFormGroup {
    const contactMessageRawValue = this.convertContactMessageToContactMessageRawValue({
      ...this.getFormDefaults(),
      ...contactMessage,
    });
    return new FormGroup<ContactMessageFormGroupContent>({
      id: new FormControl(
        { value: contactMessageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      visitorName: new FormControl(contactMessageRawValue.visitorName, {
        validators: [Validators.required],
      }),
      visitorEmail: new FormControl(contactMessageRawValue.visitorEmail, {
        validators: [Validators.required],
      }),
      message: new FormControl(contactMessageRawValue.message, {
        validators: [Validators.required],
      }),
      submittedDate: new FormControl(contactMessageRawValue.submittedDate, {
        validators: [Validators.required],
      }),
    });
  }

  getContactMessage(form: ContactMessageFormGroup): IContactMessage | NewContactMessage {
    return this.convertContactMessageRawValueToContactMessage(
      form.getRawValue() as ContactMessageFormRawValue | NewContactMessageFormRawValue,
    );
  }

  resetForm(form: ContactMessageFormGroup, contactMessage: ContactMessageFormGroupInput): void {
    const contactMessageRawValue = this.convertContactMessageToContactMessageRawValue({ ...this.getFormDefaults(), ...contactMessage });
    form.reset(
      {
        ...contactMessageRawValue,
        id: { value: contactMessageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ContactMessageFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      submittedDate: currentTime,
    };
  }

  private convertContactMessageRawValueToContactMessage(
    rawContactMessage: ContactMessageFormRawValue | NewContactMessageFormRawValue,
  ): IContactMessage | NewContactMessage {
    return {
      ...rawContactMessage,
      submittedDate: dayjs(rawContactMessage.submittedDate, DATE_TIME_FORMAT),
    };
  }

  private convertContactMessageToContactMessageRawValue(
    contactMessage: IContactMessage | (Partial<NewContactMessage> & ContactMessageFormDefaults),
  ): ContactMessageFormRawValue | PartialWithRequiredKeyOf<NewContactMessageFormRawValue> {
    return {
      ...contactMessage,
      submittedDate: contactMessage.submittedDate ? contactMessage.submittedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
