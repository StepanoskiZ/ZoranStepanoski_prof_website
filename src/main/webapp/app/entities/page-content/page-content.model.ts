export interface IPageContent {
  id: number;
  sectionKey?: string | null;
  contentHtml?: string | null;
}

export type NewPageContent = Omit<IPageContent, 'id'> & { id: null };
