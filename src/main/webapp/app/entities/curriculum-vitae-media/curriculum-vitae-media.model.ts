import { ICurriculumVitae } from 'app/entities/curriculum-vitae/curriculum-vitae.model';
import { UnifiedMediaType } from 'app/entities/enumerations/unified-media-type.model';

export interface ICurriculumVitaeMedia {
  id: number;
  mediaUrl?: string | null;
  curriculumVitaeMediaType?: keyof typeof UnifiedMediaType | null;
  caption?: string | null;
  curriculumVitae?: Pick<ICurriculumVitae, 'id' | 'companyName'> | null;}

export type NewCurriculumVitaeMedia = Omit<ICurriculumVitaeMedia, 'id'> & { id: null };
