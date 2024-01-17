import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAlbum } from '../album.model';
import { AlbumService } from '../service/album.service';

export const albumResolve = (route: ActivatedRouteSnapshot): Observable<null | IAlbum> => {
  const id = route.params['id'];
  if (id) {
    return inject(AlbumService)
      .find(id)
      .pipe(
        mergeMap((album: HttpResponse<IAlbum>) => {
          if (album.body) {
            return of(album.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default albumResolve;
