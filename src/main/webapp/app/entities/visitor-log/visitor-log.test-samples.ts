import dayjs from 'dayjs/esm';

import { IVisitorLog, NewVisitorLog } from './visitor-log.model';

export const sampleWithRequiredData: IVisitorLog = {
  id: 1363,
  visitTimestamp: dayjs('2025-09-18T09:53'),
};

export const sampleWithPartialData: IVisitorLog = {
  id: 16722,
  ipAddress: 'straight',
  pageVisited: 'stall but victoriously',
  visitTimestamp: dayjs('2025-09-18T15:39'),
};

export const sampleWithFullData: IVisitorLog = {
  id: 8826,
  ipAddress: 'whereas',
  pageVisited: 'more',
  userAgent: 'upon woot',
  visitTimestamp: dayjs('2025-09-18T16:34'),
};

export const sampleWithNewData: NewVisitorLog = {
  visitTimestamp: dayjs('2025-09-18T12:25'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
