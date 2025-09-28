import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  id: 19056,
};

export const sampleWithPartialData: IAuthority = {
  id: 11811,
};

export const sampleWithFullData: IAuthority = {
  id: 27982,
};

export const sampleWithNewData: NewAuthority = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
