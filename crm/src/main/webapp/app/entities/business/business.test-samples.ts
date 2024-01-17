import { IBusiness, NewBusiness } from './business.model';

export const sampleWithRequiredData: IBusiness = {
  id: 25084,
  name: 'supposing instead dumb',
  address: 'mortgage ack valiantly',
  owner: 'miniature frosty hoarse',
};

export const sampleWithPartialData: IBusiness = {
  id: 10273,
  name: 'sans fingerprint tedious',
  address: 'without',
  owner: 'obvious fairly blah',
};

export const sampleWithFullData: IBusiness = {
  id: 12829,
  name: 'anti naturally hourly',
  address: 'geez referendum because',
  owner: 'yum frightened',
  contact: 'besmirch likewise out',
  phone: '941.241.5233 x286',
};

export const sampleWithNewData: NewBusiness = {
  name: 'spirited',
  address: 'less yet',
  owner: 'shrilly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
