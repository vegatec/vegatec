import dayjs from 'dayjs/esm';

import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 10660,
  name: 'disgusting airbrush well-made',
  model: 'underneath',
  serialNumber: 'beguile gosh cheek',
  createdOn: dayjs('2021-08-05'),
};

export const sampleWithPartialData: IProduct = {
  id: 9481,
  name: 'upon',
  model: 'courageously sans',
  serialNumber: 'far-flung miss',
  createdOn: dayjs('2021-08-06'),
};

export const sampleWithFullData: IProduct = {
  id: 2749,
  name: 'spangle snare',
  model: 'meek bet underpants',
  serialNumber: 'pfft',
  manufacturer: 'beak',
  createdOn: dayjs('2021-08-06'),
};

export const sampleWithNewData: NewProduct = {
  name: 'pfft finally overproduce',
  model: 'thorny greatly',
  serialNumber: 'even tough uncurl',
  createdOn: dayjs('2021-08-06'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
