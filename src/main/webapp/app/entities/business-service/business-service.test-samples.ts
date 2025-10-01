import { IBusinessService, NewBusinessService } from './business-service.model';

export const sampleWithRequiredData: IBusinessService = {
  id: 19023,
  title: 'talent verifiable',
  descriptionHTML: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: IBusinessService = {
  id: 28096,
  title: 'whereas substitute',
  descriptionHTML: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IBusinessService = {
  id: 5907,
  title: 'lazily',
  descriptionHTML: '../fake-data/blob/hipster.txt',
  icon: 'dreamily badly zowie',
};

export const sampleWithNewData: NewBusinessService = {
  title: 'careless until frantically',
  descriptionHTML: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
