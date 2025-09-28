import dayjs from 'dayjs/esm';
import { ICurriculumVitaeMedia } from 'app/entities/curriculum-vitae-media/curriculum-vitae-media.model';
import { Language } from 'app/entities/enumerations/language.model';
import { WorkingStatus } from 'app/entities/enumerations/working-status.model';
import { WorkingType } from 'app/entities/enumerations/working-type.model';

export interface ICurriculumVitae {
  id: number;
  companyName?: string | null;
  language?: keyof typeof Language | null;
  jobDescriptionHTML?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  status?: keyof typeof WorkingStatus | null;
  type?: keyof typeof WorkingType | null;
  category?: string | null;
  media?: Pick<ICurriculumVitaeMedia, 'id' | 'mediaUrl'>[] | null;
}

export type NewCurriculumVitae = Omit<ICurriculumVitae, 'id'> & { id: null };
