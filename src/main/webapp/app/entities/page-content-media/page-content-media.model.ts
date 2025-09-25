import { IPageContent } from 'app/entities/page-content/page-content.model';
import { UnifiedMediaType } from 'app/entities/enumerations/unified-media-type.model';

export interface IPageContentMedia {
  id: number;
  mediaUrl?: string | null;
  pageContentMediaType?: keyof typeof UnifiedMediaType | null;
  caption?: string | null;
  pagecontent?: Pick<IPageContent, 'id' | 'sectionKey'> | null;
}

export type NewPageContentMedia = Omit<IPageContentMedia, 'id'> & { id: null };
