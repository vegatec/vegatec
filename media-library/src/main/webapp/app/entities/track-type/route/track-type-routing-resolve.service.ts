import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrackType } from '../track-type.model';
import { TrackTypeService } from '../service/track-type.service';

export const trackTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | ITrackType> => {
  const id = route.params['id'];
  if (id) {
    return inject(TrackTypeService)
      .find(id)
      .pipe(
        mergeMap((trackType: HttpResponse<ITrackType>) => {
          if (trackType.body) {
            return of(trackType.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default trackTypeResolve;
