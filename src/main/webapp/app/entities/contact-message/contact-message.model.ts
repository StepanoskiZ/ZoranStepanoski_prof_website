import dayjs from 'dayjs/esm';

export interface IContactMessage {
  id: number;
  visitorName?: string | null;
  visitorEmail?: string | null;
  message?: string | null;
  submittedDate?: dayjs.Dayjs | null;
}

export type NewContactMessage = Omit<IContactMessage, 'id'> & { id: null };
