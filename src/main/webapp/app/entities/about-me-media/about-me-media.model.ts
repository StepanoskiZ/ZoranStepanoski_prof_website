import { IAboutMe } from 'app/entities/about-me/about-me.model';
import { UnifiedMediaType } from 'app/entities/enumerations/unified-media-type.model';

export interface IAboutMeMedia {
  id: number;
  mediaUrl?: string | null;
  aboutMeMediaType?: keyof typeof UnifiedMediaType | null;
  caption?: string | null;
  aboutMe?: Pick<IAboutMe, 'id' | 'name'> | null;
}

export type NewAboutMeMedia = Omit<IAboutMeMedia, 'id'> & { id: null };
