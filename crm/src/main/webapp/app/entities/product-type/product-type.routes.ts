import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ProductTypeComponent } from './list/product-type.component';
import { ProductTypeDetailComponent } from './detail/product-type-detail.component';
import { ProductTypeUpdateComponent } from './update/product-type-update.component';
import ProductTypeResolve from './route/product-type-routing-resolve.service';

const productTypeRoute: Routes = [
  {
    path: '',
    component: ProductTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductTypeDetailComponent,
    resolve: {
      productType: ProductTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductTypeUpdateComponent,
    resolve: {
      productType: ProductTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductTypeUpdateComponent,
    resolve: {
      productType: ProductTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default productTypeRoute;
