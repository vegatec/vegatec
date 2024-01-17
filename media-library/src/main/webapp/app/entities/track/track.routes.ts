import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TrackComponent } from './list/track.component';
import { TrackDetailComponent } from './detail/track-detail.component';
import { TrackUpdateComponent } from './update/track-update.component';
import TrackResolve from './route/track-routing-resolve.service';

const trackRoute: Routes = [
  {
    path: '',
    component: TrackComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TrackDetailComponent,
    resolve: {
      track: TrackResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TrackUpdateComponent,
    resolve: {
      track: TrackResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TrackUpdateComponent,
    resolve: {
      track: TrackResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default trackRoute;
