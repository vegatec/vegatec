import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBusiness, NewBusiness } from '../business.model';

export type PartialUpdateBusiness = Partial<IBusiness> & Pick<IBusiness, 'id'>;

export type EntityResponseType = HttpResponse<IBusiness>;
export type EntityArrayResponseType = HttpResponse<IBusiness[]>;

@Injectable({ providedIn: 'root' })
export class BusinessService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/businesses');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(business: NewBusiness): Observable<EntityResponseType> {
    return this.http.post<IBusiness>(this.resourceUrl, business, { observe: 'response' });
  }

  update(business: IBusiness): Observable<EntityResponseType> {
    return this.http.put<IBusiness>(`${this.resourceUrl}/${this.getBusinessIdentifier(business)}`, business, { observe: 'response' });
  }

  partialUpdate(business: PartialUpdateBusiness): Observable<EntityResponseType> {
    return this.http.patch<IBusiness>(`${this.resourceUrl}/${this.getBusinessIdentifier(business)}`, business, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBusiness>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBusiness[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBusinessIdentifier(business: Pick<IBusiness, 'id'>): number {
    return business.id;
  }

  compareBusiness(o1: Pick<IBusiness, 'id'> | null, o2: Pick<IBusiness, 'id'> | null): boolean {
    return o1 && o2 ? this.getBusinessIdentifier(o1) === this.getBusinessIdentifier(o2) : o1 === o2;
  }

  addProduct(businessId: number, selectedProductId: number): Observable<EntityResponseType> {
    return this.http.put<IBusiness>(`${this.resourceUrl}/${businessId}/products`, selectedProductId, { observe: 'response' });
  }

  removeProduct(businessId: number | undefined, productId: number): Observable<HttpResponse<{}>> {
    return this.http.delete<IBusiness>(`${this.resourceUrl}/${businessId}/products/${productId}`, { observe: 'response' });
  }

  addBusinessToCollectionIfMissing<Type extends Pick<IBusiness, 'id'>>(
    businessCollection: Type[],
    ...businessesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const businesses: Type[] = businessesToCheck.filter(isPresent);
    if (businesses.length > 0) {
      const businessCollectionIdentifiers = businessCollection.map(businessItem => this.getBusinessIdentifier(businessItem)!);
      const businessesToAdd = businesses.filter(businessItem => {
        const businessIdentifier = this.getBusinessIdentifier(businessItem);
        if (businessCollectionIdentifiers.includes(businessIdentifier)) {
          return false;
        }
        businessCollectionIdentifiers.push(businessIdentifier);
        return true;
      });
      return [...businessesToAdd, ...businessCollection];
    }
    return businessCollection;
  }
}
