import { Routes } from '@angular/router';

const routes: Routes = [
  // {
  //   path: 'track-type',
  //   data: { pageTitle: 'mediaLibraryApp.trackType.home.title' },
  //   loadChildren: () => import('./track-type/track-type.routes'),
  // },
  {
    path: 'music-request',
    data: { pageTitle: 'mediaLibraryApp.musicRequest.home.title' },
    loadChildren: () => import('./music-request/music-request.routes'),
  },
  // {
  //   path: 'track',
  //   data: { pageTitle: 'mediaLibraryApp.track.home.title' },
  //   loadChildren: () => import('./track/track.routes'),

  // },

  // {
  //   path: 'album',
  //   data: { pageTitle: 'mediaLibraryApp.album.home.title' },
  //   loadChildren: () => import('./album/album.routes'),
  // },
  {
    path: 'library',
    loadChildren: () => import('../library/library.routes'),
  },

  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
