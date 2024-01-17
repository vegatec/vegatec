import { IProductType, NewProductType } from './product-type.model';

export const sampleWithRequiredData: IProductType = {
  id: 5879,
  name: 'keenly questioningly',
};

export const sampleWithPartialData: IProductType = {
  id: 18084,
  name: 'madly within',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
};

export const sampleWithFullData: IProductType = {
  id: 9206,
  name: 'accomplished talkative',
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
};

export const sampleWithNewData: NewProductType = {
  name: 'legitimise phew twig',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
