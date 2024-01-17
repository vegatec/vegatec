import { IProduct } from 'app/entities/product/product.model';

export interface IBusiness {
  id: number;
  name?: string | null;
  address?: string | null;
  owner?: string | null;
  contact?: string | null;
  phone?: string | null;
  products?: Pick<IProduct, 'id'>[] | null;
}

export type NewBusiness = Omit<IBusiness, 'id'> & { id: null };
