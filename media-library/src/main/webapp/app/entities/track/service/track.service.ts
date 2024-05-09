import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { Track, NewTrack } from 'app/store/models';

export type PartialUpdateTrack = Partial<Track> & Pick<Track, 'id'>;

type RestOf<T extends Track | NewTrack> = Omit<T, 'createdOn'> & {
  createdOn?: string | null;
};

export type RestTrack = RestOf<Track>;

export type NewRestTrack = RestOf<NewTrack>;

export type PartialUpdateRestTrack = RestOf<PartialUpdateTrack>;

export type EntityResponseType = HttpResponse<Track>;
export type EntityArrayResponseType = HttpResponse<Track[]>;

@Injectable({ providedIn: 'root' })
export class TrackService {

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tracks');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/tracks/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(track: NewTrack): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(track);
    return this.http.post<RestTrack>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(track: Track): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(track);
    return this.http
      .put<RestTrack>(`${this.resourceUrl}/${this.getTrackIdentifier(track)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(track: PartialUpdateTrack): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(track);
    return this.http
      .patch<RestTrack>(`${this.resourceUrl}/${this.getTrackIdentifier(track)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  publish(id: number) : Observable<EntityResponseType> {
    return this.http
      .put<RestTrack>(`${this.resourceUrl}/${id}/publish`,  {} , { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
    
  }

  unpublish(id: number) : Observable<EntityResponseType> {
    return this.http
      .put<RestTrack>(`${this.resourceUrl}/${id}/unpublish`,  {} , { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
    
  }



  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTrack>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTrack[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }


  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestTrack[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<Track[]>()], asapScheduler)),
    );
  }

  getTrackIdentifier(track: Pick<Track, 'id'>): number {
    return track.id;
  }

  compareTrack(o1: Pick<Track, 'id'> | null, o2: Pick<Track, 'id'> | null): boolean {
    return o1 && o2 ? this.getTrackIdentifier(o1) === this.getTrackIdentifier(o2) : o1 === o2;
  }

  addTrackToCollectionIfMissing<Type extends Pick<Track, 'id'>>(
    trackCollection: Type[],
    ...tracksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tracks: Type[] = tracksToCheck.filter(isPresent);
    if (tracks.length > 0) {
      const trackCollectionIdentifiers = trackCollection.map(trackItem => this.getTrackIdentifier(trackItem)!);
      const tracksToAdd = tracks.filter(trackItem => {
        const trackIdentifier = this.getTrackIdentifier(trackItem);
        if (trackCollectionIdentifiers.includes(trackIdentifier)) {
          return false;
        }
        trackCollectionIdentifiers.push(trackIdentifier);
        return true;
      });
      return [...tracksToAdd, ...trackCollection];
    }
    return trackCollection;
  }

  protected convertDateFromClient<T extends Track | NewTrack | PartialUpdateTrack>(track: T): RestOf<T> {
    return {
      ...track,
      createdOn: track.createdOn?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTrack: RestTrack): Track {
    return {
      ...restTrack,
      createdOn: restTrack.createdOn ? dayjs(restTrack.createdOn) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTrack>): HttpResponse<Track> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTrack[]>): HttpResponse<Track[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
