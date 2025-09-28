import dayjs from 'dayjs/esm';
import { IProjectMedia } from 'app/entities/project-media/project-media.model';
import { ProjectStatus } from 'app/entities/enumerations/project-status.model';
import { Language } from 'app/entities/enumerations/language.model';

export interface IProject {
  id: number;
  title?: string | null;
  descriptionHTML?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  status?: keyof typeof ProjectStatus | null;
  projectUrl?: string | null;
  category?: string | null;
  language?: keyof typeof Language | null;
  media?: Pick<IProjectMedia, 'id' | 'mediaUrl'>[] | null;
}

export type NewProject = Omit<IProject, 'id'> & { id: null };
