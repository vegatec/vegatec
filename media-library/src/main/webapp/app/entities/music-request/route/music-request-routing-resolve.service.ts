import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMusicRequest } from '../music-request.model';
import { MusicRequestService } from '../service/music-request.service';

export const musicRequestResolve = (route: ActivatedRouteSnapshot): Observable<null | IMusicRequest> => {
  const id = route.params['id'];
  if (id) {
    return inject(MusicRequestService)
      .find(id)
      .pipe(
        mergeMap((musicRequest: HttpResponse<IMusicRequest>) => {
          if (musicRequest.body) {
            return of(musicRequest.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default musicRequestResolve;
