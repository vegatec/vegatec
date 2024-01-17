import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MusicRequestComponent } from './list/music-request.component';
import { MusicRequestDetailComponent } from './detail/music-request-detail.component';
import { MusicRequestUpdateComponent } from './update/music-request-update.component';
import MusicRequestResolve from './route/music-request-routing-resolve.service';

const musicRequestRoute: Routes = [
  {
    path: '',
    component: MusicRequestComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MusicRequestDetailComponent,
    resolve: {
      musicRequest: MusicRequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MusicRequestUpdateComponent,
    resolve: {
      musicRequest: MusicRequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MusicRequestUpdateComponent,
    resolve: {
      musicRequest: MusicRequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default musicRequestRoute;
