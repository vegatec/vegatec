import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrack } from '../track.model';
import { TrackService } from '../service/track.service';

export const trackResolve = (route: ActivatedRouteSnapshot): Observable<null | ITrack> => {
  const id = route.params['id'];
  if (id) {
    return inject(TrackService)
      .find(id)
      .pipe(
        mergeMap((track: HttpResponse<ITrack>) => {
          if (track.body) {
            return of(track.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default trackResolve;
