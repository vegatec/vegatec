export interface IProductType {
  id: number;
  name?: string | null;
  logo?: string | null;
  logoContentType?: string | null;
}

export type NewProductType = Omit<IProductType, 'id'> & { id: null };
