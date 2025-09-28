import dayjs from 'dayjs/esm';

import { IVisitorLog, NewVisitorLog } from './visitor-log.model';

export const sampleWithRequiredData: IVisitorLog = {
  id: 23313,
  visitTimestamp: dayjs('2025-09-18T16:20'),
};

export const sampleWithPartialData: IVisitorLog = {
  id: 1216,
  ipAddress: 'afore',
  userAgent: 'above scratchy',
  visitTimestamp: dayjs('2025-09-18T09:49'),
};

export const sampleWithFullData: IVisitorLog = {
  id: 12227,
  ipAddress: 'like',
  pageVisited: 'even brr pfft',
  userAgent: 'ha trim',
  visitTimestamp: dayjs('2025-09-18T19:20'),
};

export const sampleWithNewData: NewVisitorLog = {
  visitTimestamp: dayjs('2025-09-18T21:18'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
