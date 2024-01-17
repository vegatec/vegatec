import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IMusicRequest, NewMusicRequest } from '../music-request.model';

export type PartialUpdateMusicRequest = Partial<IMusicRequest> & Pick<IMusicRequest, 'id'>;

type RestOf<T extends IMusicRequest | NewMusicRequest> = Omit<T, 'requestedOn'> & {
  requestedOn?: string | null;
};

export type RestMusicRequest = RestOf<IMusicRequest>;

export type NewRestMusicRequest = RestOf<NewMusicRequest>;

export type PartialUpdateRestMusicRequest = RestOf<PartialUpdateMusicRequest>;

export type EntityResponseType = HttpResponse<IMusicRequest>;
export type EntityArrayResponseType = HttpResponse<IMusicRequest[]>;

@Injectable({ providedIn: 'root' })
export class MusicRequestService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/music-requests');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/music-requests/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(musicRequest: NewMusicRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(musicRequest);
    return this.http
      .post<RestMusicRequest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(musicRequest: IMusicRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(musicRequest);
    return this.http
      .put<RestMusicRequest>(`${this.resourceUrl}/${this.getMusicRequestIdentifier(musicRequest)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(musicRequest: PartialUpdateMusicRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(musicRequest);
    return this.http
      .patch<RestMusicRequest>(`${this.resourceUrl}/${this.getMusicRequestIdentifier(musicRequest)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMusicRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMusicRequest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestMusicRequest[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IMusicRequest[]>()], asapScheduler)),
    );
  }

  getMusicRequestIdentifier(musicRequest: Pick<IMusicRequest, 'id'>): number {
    return musicRequest.id;
  }

  compareMusicRequest(o1: Pick<IMusicRequest, 'id'> | null, o2: Pick<IMusicRequest, 'id'> | null): boolean {
    return o1 && o2 ? this.getMusicRequestIdentifier(o1) === this.getMusicRequestIdentifier(o2) : o1 === o2;
  }

  addMusicRequestToCollectionIfMissing<Type extends Pick<IMusicRequest, 'id'>>(
    musicRequestCollection: Type[],
    ...musicRequestsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const musicRequests: Type[] = musicRequestsToCheck.filter(isPresent);
    if (musicRequests.length > 0) {
      const musicRequestCollectionIdentifiers = musicRequestCollection.map(
        musicRequestItem => this.getMusicRequestIdentifier(musicRequestItem)!,
      );
      const musicRequestsToAdd = musicRequests.filter(musicRequestItem => {
        const musicRequestIdentifier = this.getMusicRequestIdentifier(musicRequestItem);
        if (musicRequestCollectionIdentifiers.includes(musicRequestIdentifier)) {
          return false;
        }
        musicRequestCollectionIdentifiers.push(musicRequestIdentifier);
        return true;
      });
      return [...musicRequestsToAdd, ...musicRequestCollection];
    }
    return musicRequestCollection;
  }

  protected convertDateFromClient<T extends IMusicRequest | NewMusicRequest | PartialUpdateMusicRequest>(musicRequest: T): RestOf<T> {
    return {
      ...musicRequest,
      requestedOn: musicRequest.requestedOn?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMusicRequest: RestMusicRequest): IMusicRequest {
    return {
      ...restMusicRequest,
      requestedOn: restMusicRequest.requestedOn ? dayjs(restMusicRequest.requestedOn) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMusicRequest>): HttpResponse<IMusicRequest> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMusicRequest[]>): HttpResponse<IMusicRequest[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
