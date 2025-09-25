import { IBusinessService, NewBusinessService } from './business-service.model';

export const sampleWithRequiredData: IBusinessService = {
  id: 18202,
  title: 'until boohoo loose',
};

export const sampleWithPartialData: IBusinessService = {
  id: 21371,
  title: 'ripe but',
  icon: 'reckless now whenever',
};

export const sampleWithFullData: IBusinessService = {
  id: 3184,
  title: 'atrium warlike masquerade',
  description: 'rue pronoun pfft',
  icon: 'where',
};

export const sampleWithNewData: NewBusinessService = {
  title: 'never',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
