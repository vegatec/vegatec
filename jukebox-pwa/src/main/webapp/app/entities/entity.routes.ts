import { Routes } from '@angular/router';
import {UrlReceiverComponent} from "../shared/url-receiver/url-receiver.component";

const routes: Routes = [
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
  {
    path: "receive-url",
    component: UrlReceiverComponent
  }
];

export default routes;
