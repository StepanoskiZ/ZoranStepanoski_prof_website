import { IAboutMeMedia } from 'app/entities/about-me-media/about-me-media.model';
import { Language } from 'app/entities/enumerations/language.model';

export interface IAboutMe {
  id: number;
  name?: string | null;
  contentHtml?: string | null;
  language?: keyof typeof Language | null;
  media?: Pick<IAboutMeMedia, 'id' | 'mediaUrl'>[] | null;
}

export type NewAboutMe = Omit<IAboutMe, 'id'> & { id: null };
