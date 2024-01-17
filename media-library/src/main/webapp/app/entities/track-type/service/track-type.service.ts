import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { ITrackType, NewTrackType } from '../track-type.model';

export type PartialUpdateTrackType = Partial<ITrackType> & Pick<ITrackType, 'id'>;

export type EntityResponseType = HttpResponse<ITrackType>;
export type EntityArrayResponseType = HttpResponse<ITrackType[]>;

@Injectable({ providedIn: 'root' })
export class TrackTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/track-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/track-types/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(trackType: NewTrackType): Observable<EntityResponseType> {
    return this.http.post<ITrackType>(this.resourceUrl, trackType, { observe: 'response' });
  }

  update(trackType: ITrackType): Observable<EntityResponseType> {
    return this.http.put<ITrackType>(`${this.resourceUrl}/${this.getTrackTypeIdentifier(trackType)}`, trackType, { observe: 'response' });
  }

  partialUpdate(trackType: PartialUpdateTrackType): Observable<EntityResponseType> {
    return this.http.patch<ITrackType>(`${this.resourceUrl}/${this.getTrackTypeIdentifier(trackType)}`, trackType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITrackType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITrackType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITrackType[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<ITrackType[]>()], asapScheduler)));
  }

  getTrackTypeIdentifier(trackType: Pick<ITrackType, 'id'>): number {
    return trackType.id;
  }

  compareTrackType(o1: Pick<ITrackType, 'id'> | null, o2: Pick<ITrackType, 'id'> | null): boolean {
    return o1 && o2 ? this.getTrackTypeIdentifier(o1) === this.getTrackTypeIdentifier(o2) : o1 === o2;
  }

  addTrackTypeToCollectionIfMissing<Type extends Pick<ITrackType, 'id'>>(
    trackTypeCollection: Type[],
    ...trackTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const trackTypes: Type[] = trackTypesToCheck.filter(isPresent);
    if (trackTypes.length > 0) {
      const trackTypeCollectionIdentifiers = trackTypeCollection.map(trackTypeItem => this.getTrackTypeIdentifier(trackTypeItem)!);
      const trackTypesToAdd = trackTypes.filter(trackTypeItem => {
        const trackTypeIdentifier = this.getTrackTypeIdentifier(trackTypeItem);
        if (trackTypeCollectionIdentifiers.includes(trackTypeIdentifier)) {
          return false;
        }
        trackTypeCollectionIdentifiers.push(trackTypeIdentifier);
        return true;
      });
      return [...trackTypesToAdd, ...trackTypeCollection];
    }
    return trackTypeCollection;
  }
}
