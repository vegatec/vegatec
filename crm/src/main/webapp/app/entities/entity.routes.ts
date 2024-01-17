import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'product-type',
    data: { pageTitle: 'crmApp.productType.home.title' },
    loadChildren: () => import('./product-type/product-type.routes'),
  },
  {
    path: 'business',
    data: { pageTitle: 'crmApp.business.home.title' },
    loadChildren: () => import('./business/business.routes'),
  },
  {
    path: 'product',
    data: { pageTitle: 'crmApp.product.home.title' },
    loadChildren: () => import('./product/product.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
