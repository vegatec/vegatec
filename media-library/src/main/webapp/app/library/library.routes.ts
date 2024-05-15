import { Routes } from '@angular/router';

import { TrackComponent } from 'app/entities/track/list/track.component';
import { LibraryComponent } from './library.component';
import { AlbumComponent } from 'app/entities/album/list/album.component';
import { ASC } from 'app/config/navigation.constants';
import { TrackUpdateComponent } from 'app/entities/track/update/track-update.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import trackResolve from 'app/entities/track/route/track-routing-resolve.service';


const albumRoute: Routes = [
  {
    path: '',
    component: LibraryComponent,
    data: {
      // authorities: [Authority.USER],
      pageTitle: 'JukeboxApp.library.home.title',
    },
    // canActivate: [UserRouteAccessService],
    children: [
      {
        path: 'albums',
        component: AlbumComponent,
        data: {
          defaultSort: 'id,' + ASC,
        },
        // canActivate: [UserRouteAccessService],
        outlet: 'albums',
      },
      {
        path: 'tracks',
        component: TrackComponent,
        data: {
          defaultSort: 'id,' + ASC,
        },
        // canActivate: [UserRouteAccessService],
        outlet: 'tracks',
      },
      {
        path: ':id/edit',
        component: TrackUpdateComponent,
        resolve: {
          track: trackResolve,
          
        },
        canActivate: [UserRouteAccessService],
      },
    ],
  },

];

export default albumRoute;
