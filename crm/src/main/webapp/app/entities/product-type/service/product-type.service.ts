import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductType, NewProductType } from '../product-type.model';

export type PartialUpdateProductType = Partial<IProductType> & Pick<IProductType, 'id'>;

export type EntityResponseType = HttpResponse<IProductType>;
export type EntityArrayResponseType = HttpResponse<IProductType[]>;

@Injectable({ providedIn: 'root' })
export class ProductTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-types');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(productType: NewProductType): Observable<EntityResponseType> {
    return this.http.post<IProductType>(this.resourceUrl, productType, { observe: 'response' });
  }

  update(productType: IProductType): Observable<EntityResponseType> {
    return this.http.put<IProductType>(`${this.resourceUrl}/${this.getProductTypeIdentifier(productType)}`, productType, {
      observe: 'response',
    });
  }

  partialUpdate(productType: PartialUpdateProductType): Observable<EntityResponseType> {
    return this.http.patch<IProductType>(`${this.resourceUrl}/${this.getProductTypeIdentifier(productType)}`, productType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProductType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProductType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProductTypeIdentifier(productType: Pick<IProductType, 'id'>): number {
    return productType.id;
  }

  compareProductType(o1: Pick<IProductType, 'id'> | null, o2: Pick<IProductType, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductTypeIdentifier(o1) === this.getProductTypeIdentifier(o2) : o1 === o2;
  }

  addProductTypeToCollectionIfMissing<Type extends Pick<IProductType, 'id'>>(
    productTypeCollection: Type[],
    ...productTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productTypes: Type[] = productTypesToCheck.filter(isPresent);
    if (productTypes.length > 0) {
      const productTypeCollectionIdentifiers = productTypeCollection.map(
        productTypeItem => this.getProductTypeIdentifier(productTypeItem)!,
      );
      const productTypesToAdd = productTypes.filter(productTypeItem => {
        const productTypeIdentifier = this.getProductTypeIdentifier(productTypeItem);
        if (productTypeCollectionIdentifiers.includes(productTypeIdentifier)) {
          return false;
        }
        productTypeCollectionIdentifiers.push(productTypeIdentifier);
        return true;
      });
      return [...productTypesToAdd, ...productTypeCollection];
    }
    return productTypeCollection;
  }
}
