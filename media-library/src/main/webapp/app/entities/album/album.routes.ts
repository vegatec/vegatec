import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';

import { AlbumComponent } from './list/album.component';

const albumRoute: Routes = [
  {
    path: '',
    component: AlbumComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default albumRoute;
