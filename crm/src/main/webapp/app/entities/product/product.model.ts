import dayjs from 'dayjs/esm';
import { IProductType } from 'app/entities/product-type/product-type.model';
import { IBusiness } from 'app/entities/business/business.model';

export interface IProduct {
  id: number;
  name?: string | null;
  model?: string | null;
  serialNumber?: string | null;
  manufacturer?: string | null;
  createdOn?: dayjs.Dayjs | null;
  type?: Pick<IProductType, 'id' | 'name' | 'logoContentType' | 'logo'> | null;
  // components?: Pick<IProduct, 'id'>[] | null;
  locatedAt?: Pick<IBusiness, 'id' | 'name'> | null;
  componentOf?: Pick<IProduct, 'id'> | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
