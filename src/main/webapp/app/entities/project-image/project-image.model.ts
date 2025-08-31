import { IProject } from 'app/entities/project/project.model';

export interface IProjectImage {
  id: number;
  imageUrl?: string | null;
  caption?: string | null;
  project?: Pick<IProject, 'id'> | null;
}

export type NewProjectImage = Omit<IProjectImage, 'id'> & { id: null };
