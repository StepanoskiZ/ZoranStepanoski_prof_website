import dayjs from 'dayjs/esm';

export interface IVisitorLog {
  id: number;
  ipAddress?: string | null;
  city?: string | null;
  country?: string | null;
  pageVisited?: string | null;
  userAgent?: string | null;
  visitTimestamp?: dayjs.Dayjs | null;
}

export type NewVisitorLog = Omit<IVisitorLog, 'id'> & { id: null };
