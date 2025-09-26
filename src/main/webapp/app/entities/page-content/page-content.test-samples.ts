import { IPageContent, NewPageContent } from './page-content.model';

export const sampleWithRequiredData: IPageContent = {
  id: 7878,
  sectionKey: 'exhilarate consequently',
  contentHtml: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: IPageContent = {
  id: 22677,
  sectionKey: 'armchair violate',
  contentHtml: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IPageContent = {
  id: 23641,
  sectionKey: 'how however till',
  contentHtml: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewPageContent = {
  sectionKey: 'grain ick where',
  contentHtml: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
