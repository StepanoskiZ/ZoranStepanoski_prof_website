import { IProject } from 'app/entities/project/project.model';
import { UnifiedMediaType } from 'app/entities/enumerations/unified-media-type.model';

export interface IProjectMedia {
  id: number;
  mediaUrl?: string | null;
  projectMediaType?: keyof typeof UnifiedMediaType | null;
  caption?: string | null;
  project?: Pick<IProject, 'id' | 'title'> | null;
}

export type NewProjectMedia = Omit<IProjectMedia, 'id'> & { id: null };
