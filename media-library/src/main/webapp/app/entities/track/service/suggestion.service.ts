import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITrack } from 'app/store/models';



export type EntityArrayResponseType = HttpResponse<string[]>;

@Injectable({ providedIn: 'root' })
export class SuggestionService {


  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/suggestions');


  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}


  artists(term: string): Observable<EntityArrayResponseType> {
    return this.http
      .get<string[]>(`${this.resourceUrl}/artists`, { params: {term}, observe: 'response' })
  }

  genres(term: string): Observable<EntityArrayResponseType> {
  
    return this.http
      .get<string[]>(`${this.resourceUrl}/genres`, { params: {term}, observe: 'response' })
  }


  
}
