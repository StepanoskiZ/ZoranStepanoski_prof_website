import { IProjectImage, NewProjectImage } from './project-image.model';

export const sampleWithRequiredData: IProjectImage = {
  id: 13294,
  imageUrl: 'meh knottily',
};

export const sampleWithPartialData: IProjectImage = {
  id: 1225,
  imageUrl: 'gee as char',
  caption: 'forgery',
};

export const sampleWithFullData: IProjectImage = {
  id: 7158,
  imageUrl: 'meanwhile ick manipulate',
  caption: 'for ugh',
};

export const sampleWithNewData: NewProjectImage = {
  imageUrl: 'metabolite',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
