import dayjs from 'dayjs/esm';

import { IVisitorLog, NewVisitorLog } from './visitor-log.model';

export const sampleWithRequiredData: IVisitorLog = {
  id: 22038,
  visitTimestamp: dayjs('2025-09-18T15:44'),
};

export const sampleWithPartialData: IVisitorLog = {
  id: 829,
  visitTimestamp: dayjs('2025-09-18T21:44'),
};

export const sampleWithFullData: IVisitorLog = {
  id: 7514,
  ipAddress: 'pish unlike',
  pageVisited: 'woot',
  userAgent: 'instead scientific disarrange',
  visitTimestamp: dayjs('2025-09-18T14:06'),
};

export const sampleWithNewData: NewVisitorLog = {
  visitTimestamp: dayjs('2025-09-18T22:23'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
