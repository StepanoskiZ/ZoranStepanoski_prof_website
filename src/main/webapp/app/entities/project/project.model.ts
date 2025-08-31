import dayjs from 'dayjs/esm';
import { ProjectStatus } from 'app/entities/enumerations/project-status.model';

export interface IProject {
  id: number;
  title?: string | null;
  description?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  status?: keyof typeof ProjectStatus | null;
  projectUrl?: string | null;
  category?: string | null;
}

export type NewProject = Omit<IProject, 'id'> & { id: null };
