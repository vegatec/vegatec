import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TrackTypeComponent } from './list/track-type.component';
import { TrackTypeDetailComponent } from './detail/track-type-detail.component';
import { TrackTypeUpdateComponent } from './update/track-type-update.component';
import TrackTypeResolve from './route/track-type-routing-resolve.service';

const trackTypeRoute: Routes = [
  {
    path: '',
    component: TrackTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TrackTypeDetailComponent,
    resolve: {
      trackType: TrackTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TrackTypeUpdateComponent,
    resolve: {
      trackType: TrackTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TrackTypeUpdateComponent,
    resolve: {
      trackType: TrackTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default trackTypeRoute;
