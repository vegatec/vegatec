import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBusiness } from '../business.model';
import { BusinessService } from '../service/business.service';

export const businessResolve = (route: ActivatedRouteSnapshot): Observable<null | IBusiness> => {
  const id = route.params['id'];
  if (id) {
    return inject(BusinessService)
      .find(id)
      .pipe(
        mergeMap((business: HttpResponse<IBusiness>) => {
          if (business.body) {
            return of(business.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default businessResolve;
