import dayjs from 'dayjs/esm';

import { IVisitorLog, NewVisitorLog } from './visitor-log.model';

export const sampleWithRequiredData: IVisitorLog = {
  id: 19213,
  visitTimestamp: dayjs('2025-09-18T09:16'),
};

export const sampleWithPartialData: IVisitorLog = {
  id: 16637,
  userAgent: 'oof',
  visitTimestamp: dayjs('2025-09-18T01:08'),
};

export const sampleWithFullData: IVisitorLog = {
  id: 21324,
  ipAddress: 'instead unimpressively barring',
  pageVisited: 'conceptualise substitute',
  userAgent: 'acquaint supposing abaft',
  visitTimestamp: dayjs('2025-09-18T15:13'),
};

export const sampleWithNewData: NewVisitorLog = {
  visitTimestamp: dayjs('2025-09-18T20:24'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
