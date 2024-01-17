import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { BusinessComponent } from './list/business.component';
import { BusinessDetailComponent } from './detail/business-detail.component';
import { BusinessUpdateComponent } from './update/business-update.component';
import BusinessResolve from './route/business-routing-resolve.service';
import { BusinessProductsComponent } from './products/business-products.component';
import { BusinessEventsComponent } from './events/business-events.component';

const businessRoute: Routes = [
  {
    path: '',
    component: BusinessComponent,
    data: {
      defaultSort: 'name,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BusinessDetailComponent,
    resolve: {
      business: BusinessResolve,
    },
    canActivate: [UserRouteAccessService],
    children: [
      {
        path: 'products',
        component: BusinessProductsComponent,
        data: {
          defaultSort: 'id,' + ASC,
        },
        canActivate: [UserRouteAccessService],
      },
      {
        path: 'events',
        component: BusinessEventsComponent,
        data: {
          defaultSort: 'id,' + ASC,
        },
        canActivate: [UserRouteAccessService],
      },
    ],
  },
  {
    path: 'new',
    component: BusinessUpdateComponent,
    resolve: {
      business: BusinessResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BusinessUpdateComponent,
    resolve: {
      business: BusinessResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default businessRoute;
