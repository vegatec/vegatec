import { patchState, signalStore, withComputed, withHooks, withMethods, withState } from '@ngrx/signals';
import { Album, Artist, PagingOptions, SearchCriteria } from './models';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { AlbumService } from 'app/entities/album/service/album.service';
import { computed, inject } from '@angular/core';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import {
  pipe,
  tap,
  debounceTime,
  distinctUntilChanged,
  switchMap,
  catchError,
  map,
  of,
  concatMap,
  groupBy,
  mergeMap,
  toArray,
  zip,
  concat,
  Observable,
  first,
} from 'rxjs';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';

export interface AlbumsState {
  albums: Album[];
  selected: Album | string | null;

  pagingOptions: PagingOptions;
  searchCriteria: SearchCriteria;

  loading: boolean;
  error: string;
}

export interface AppState {
  albumsState: AlbumsState;
}

export const AlbumsStore = signalStore(
  { providedIn: 'root' },
  withState<AlbumsState>({
    albums: [],

    selected: null,
    pagingOptions: {
      itemsPerPage: ITEMS_PER_PAGE,
      page: 0,
      sort: ['album.artist.name', 'album.releasedYear,desc'],
    },
    searchCriteria: {
      filter: '',
    },
    loading: false,
    error: '',
  }),

  withComputed(({ albums }) => ({
    groupedAlbums: computed(() =>
      of(albums()).pipe(
        concatMap(albums => albums),
        groupBy(item => item.artist!.name),
        mergeMap(group => zip(of(group.key), group.pipe(toArray()))),
        toArray(),
      ),
    ),
  })),

  withMethods(store => {
    const albumService = inject(AlbumService);
    return {
      // updateCriteria: (criteria: SearchCriteria) => {
      //     patchState(
      //         store, {
      //             searchCriteria: criteria
      //         }
      //     );
      // },

      updateCriteria: rxMethod<SearchCriteria>(
        pipe(
          tap(criteria => {
            patchState(store, {
              pagingOptions: { ...store.pagingOptions(), page: 0 },
              searchCriteria: criteria,
              albums: [],
            });
          }),
          debounceTime(500),
          distinctUntilChanged(),
          switchMap(() =>
            albumService.query({ ...store.pagingOptions(), ...store.searchCriteria() }).pipe(
              map(response => response.body ?? []),
              tap(albums => {
                patchState(store, { albums: [...store.albums()].concat(albums) });
              }),
              catchError(error => of({ error: error.message })),
            ),
          ),
        ),
      ),

      load: rxMethod<number>(
        pipe(
          tap(page => {
            patchState(store, { pagingOptions: { ...store.pagingOptions(), page } });
          }),
          debounceTime(500),
          distinctUntilChanged(),
          switchMap(() =>
            albumService.query({ ...store.pagingOptions(), ...store.searchCriteria() }).pipe(
              map(response => response.body ?? []),
              tap(albums => {
                patchState(store, { albums: [...store.albums()].concat(albums) });
              }),
              catchError(error => of({ error: error.message })),
            ),
          ),
        ),
      ),
      select: (item: Album | string | null) => {
        const result = store.selected() === item ? null : item;

        patchState(store, { selected: result });
      },

      isSelected: (item: Album | string) => store.selected() === item,
    };
  }),
  withHooks({
    onInit(store) {
      // store.load(0);
    },
  }),
);
