import { IBusinessService, NewBusinessService } from './business-service.model';

export const sampleWithRequiredData: IBusinessService = {
  id: 25911,
  title: 'whoa afore orange',
};

export const sampleWithPartialData: IBusinessService = {
  id: 27941,
  title: 'teleport midst',
  icon: 'freely',
};

export const sampleWithFullData: IBusinessService = {
  id: 12914,
  title: 'duh over quickly',
  descriptionHTML: 'although which',
  icon: 'how',
};

export const sampleWithNewData: NewBusinessService = {
  title: 'yowza',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
