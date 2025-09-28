import { IBusinessService, NewBusinessService } from './business-service.model';

export const sampleWithRequiredData: IBusinessService = {
  id: 4463,
  title: 'aboard admired',
};

export const sampleWithPartialData: IBusinessService = {
  id: 6807,
  title: 'than',
  icon: 'expostulate drafty wholly',
};

export const sampleWithFullData: IBusinessService = {
  id: 9752,
  title: 'incidentally normalisation',
  descriptionHTML: 'for unlawful',
  icon: 'languish which',
};

export const sampleWithNewData: NewBusinessService = {
  title: 'certainly intensely',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
