import dayjs from 'dayjs/esm';

import { IContactMessage, NewContactMessage } from './contact-message.model';

export const sampleWithRequiredData: IContactMessage = {
  id: 12916,
  visitorName: 'ah after yet',
  visitorEmail: 'copywriter',
  message: '../fake-data/blob/hipster.txt',
  submittedDate: dayjs('2025-08-30T17:12'),
};

export const sampleWithPartialData: IContactMessage = {
  id: 27660,
  visitorName: 'dependent',
  visitorEmail: 'atop barring ack',
  message: '../fake-data/blob/hipster.txt',
  submittedDate: dayjs('2025-08-30T01:41'),
};

export const sampleWithFullData: IContactMessage = {
  id: 14551,
  visitorName: 'musty particularise',
  visitorEmail: 'metro shakedown',
  message: '../fake-data/blob/hipster.txt',
  submittedDate: dayjs('2025-08-30T11:16'),
};

export const sampleWithNewData: NewContactMessage = {
  visitorName: 'justify listing',
  visitorEmail: 'lubricate stable finally',
  message: '../fake-data/blob/hipster.txt',
  submittedDate: dayjs('2025-08-30T09:36'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
