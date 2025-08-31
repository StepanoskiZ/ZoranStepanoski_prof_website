import dayjs from 'dayjs/esm';

import { IContactMessage, NewContactMessage } from './contact-message.model';

export const sampleWithRequiredData: IContactMessage = {
  id: 9760,
  visitorName: 'criminal yum',
  visitorEmail: 'perfection',
  message: '../fake-data/blob/hipster.txt',
  submittedDate: dayjs('2025-08-30T06:26'),
};

export const sampleWithPartialData: IContactMessage = {
  id: 14807,
  visitorName: 'jealously',
  visitorEmail: 'for hmph ha',
  message: '../fake-data/blob/hipster.txt',
  submittedDate: dayjs('2025-08-29T21:04'),
};

export const sampleWithFullData: IContactMessage = {
  id: 5510,
  visitorName: 'almost along judgementally',
  visitorEmail: 'behind on',
  message: '../fake-data/blob/hipster.txt',
  submittedDate: dayjs('2025-08-29T20:31'),
};

export const sampleWithNewData: NewContactMessage = {
  visitorName: 'snuggle',
  visitorEmail: 'community',
  message: '../fake-data/blob/hipster.txt',
  submittedDate: dayjs('2025-08-29T21:06'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
