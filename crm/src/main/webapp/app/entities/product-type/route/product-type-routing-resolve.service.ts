import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductType } from '../product-type.model';
import { ProductTypeService } from '../service/product-type.service';

export const productTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductType> => {
  const id = route.params['id'];
  if (id) {
    return inject(ProductTypeService)
      .find(id)
      .pipe(
        mergeMap((productType: HttpResponse<IProductType>) => {
          if (productType.body) {
            return of(productType.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default productTypeResolve;
