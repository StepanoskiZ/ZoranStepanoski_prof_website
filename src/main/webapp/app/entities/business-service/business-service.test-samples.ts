import { IBusinessService, NewBusinessService } from './business-service.model';

export const sampleWithRequiredData: IBusinessService = {
  id: 14761,
  title: 'fearless which',
};

export const sampleWithPartialData: IBusinessService = {
  id: 17659,
  title: 'trim present heartfelt',
  description: 'pollution yet ordinary',
};

export const sampleWithFullData: IBusinessService = {
  id: 8138,
  title: 'since penicillin',
  description: 'bitterly however',
  icon: 'regarding sundial outside',
};

export const sampleWithNewData: NewBusinessService = {
  title: 'likewise off childbirth',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
