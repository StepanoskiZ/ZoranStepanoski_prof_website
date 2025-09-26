import { IPageContentMedia } from 'app/entities/page-content-media/page-content-media.model';

export interface IPageContent {
  id: number;
  sectionKey?: string | null;
  contentHtml?: string | null;
  media?: Pick<IPageContentMedia, 'id' | 'mediaUrl'>[] | null;
}

export type NewPageContent = Omit<IPageContent, 'id'> & { id: null };
