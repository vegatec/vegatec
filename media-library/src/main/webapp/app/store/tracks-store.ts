import { patchState, signalStore, withComputed, withHooks, withMethods, withState } from '@ngrx/signals';
import { PagingOptions, SearchCriteria, Track } from './models';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { TrackService } from 'app/entities/track/service/track.service';
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

export interface TracksState {
  tracks: Track[];
  selected: Track | string | null;

  pagingOptions: PagingOptions;
  searchCriteria: SearchCriteria;

  loading: boolean;
  error: string;
}

export interface AppState {
  tracksState: TracksState;
}

export const TracksStore = signalStore(
  { providedIn: 'root' },
  withState<TracksState>({
    tracks: [],

    selected: null,
    pagingOptions: {
      itemsPerPage: ITEMS_PER_PAGE,
      page: 0,
      sort: ['name'],
    },
    searchCriteria: {
      filter: '',
    },
    loading: false,
    error: '',
  }),

  withComputed(({ tracks }) => ({
    groupedTracks: computed(() =>
      of(tracks()).pipe(
        concatMap(tracks => tracks),
        groupBy(item => item.artist!.name),
        mergeMap(group => zip(of(group.key), group.pipe(toArray()))),
        toArray(),
      ),
    ),
  })),

  withMethods(store => {
    const trackService = inject(TrackService);
    return {
      updateCriteria: rxMethod<SearchCriteria>(
        pipe(
          tap(criteria => {
            patchState(store, {
              pagingOptions: { ...store.pagingOptions(), page: 0 },
              searchCriteria: criteria,
              tracks: [],
            });
          }),
          debounceTime(500),
          distinctUntilChanged(),
          switchMap(() =>
            trackService.query({ ...store.pagingOptions(), ...store.searchCriteria() }).pipe(
              map(response => response.body ?? []),
              tap(tracks => {
                patchState(store, { tracks: [...store.tracks()].concat(tracks) });
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
            trackService.query({ ...store.pagingOptions(), ...store.searchCriteria() }).pipe(
              map(response => response.body ?? []),
              tap(tracks => {
                patchState(store, { tracks: [...store.tracks()].concat(tracks) });
              }),
              catchError(error => of({ error: error.message })),
            ),
          ),
        ),
      ),
      select: (item: Track | string | null) => {
        const result = store.selected() === item ? null : item;

        patchState(store, { selected: result });
      },

      isSelected: (item: Track | string) => store.selected() === item,
    };
  }),
  withHooks({
    onInit(store) {
      // store.load(0);
    },
  }),
);
