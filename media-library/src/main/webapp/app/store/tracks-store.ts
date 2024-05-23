import { patchState, signalStore, withComputed, withHooks, withMethods, withState } from '@ngrx/signals';
import { PagingOptions, SearchCriteria, ITrack } from './models';
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
  tracks: ITrack[];
  total: number;
  selected: ITrack[];


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
    total:0,
    selected: [],
    pagingOptions: {
      itemsPerPage: ITEMS_PER_PAGE,
      page: 0,
      sort: ['name'],
    },
    searchCriteria: {
      filter: '',
      update: false
    },
    loading: false,
    error: '',
  }),

  withComputed(({ tracks, selected }) => ({
    groupedTracks: computed(() =>
      of(tracks()).pipe(
        concatMap(tracks => tracks),
        groupBy(item => item.artist!.name),
        mergeMap(group => zip(of(group.key), group.pipe(toArray()))),
        toArray(),
      ),
    ),
    hasSelections: computed(() => selected().length > 0)
  })),

  withMethods(store => {
    const trackService = inject(TrackService);
    return {
      updateCriteria: rxMethod<SearchCriteria>(
        pipe(
          tap(criteria => {
            patchState(store, {
              pagingOptions: { ...store.pagingOptions(), page: 0 },
              searchCriteria: { ...criteria, update:false},
              tracks: [],
              selected: []
            });
          }),
          debounceTime(500),
          distinctUntilChanged(),
          switchMap(() =>
            trackService.query({ ...store.pagingOptions(), ...store.searchCriteria() }).pipe(
  
              tap(response => {
                patchState(store, { tracks: [...store.tracks(), ...response.body!], total:  +response.headers.get('x-total-count')! });
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

              tap(response => {
                patchState(store, { tracks: [...store.tracks(), ...response.body!], total:  +response.headers.get('x-total-count')! });
              }),
              catchError(error => of({ error: error.message })),
            ),
          ),
        ),
      ),


      select: (item: ITrack) => {
        const isSelected = store.selected().findIndex( i => i === item) !== -1

        if (isSelected)
          patchState(store, { selected: store.selected().filter(i=> i !== item) });
        else 
         patchState(store, { selected: [...store.selected(), item] });
      },

      isSelected: (item: ITrack | string) => store.selected().findIndex( i => i === item) !== -1,


      selectAll: () =>     
          patchState(store, {selected: store.hasSelections()? []: store.tracks()}),





      publish() {
        store.selected().forEach(track=> {
          trackService.publish(track.id).pipe(
            map(response => response.body ),
            tap((res) => {

            const index= store.tracks().findIndex(s=> s.id === track.id);
            const updatedTracks= store.tracks()[index]= res!;

            patchState(store, { tracks: [...store.tracks()] });

            }),
            catchError(error => of({ error: error.message })),
          ).subscribe()
        });

      },


      moveToTrash() {
        store.selected().forEach(track=> {
          trackService.moveToTrash(track.id).pipe(
            map(response => response.body ),
            tap((res) => {

              const index= store.tracks().findIndex(s=> s.id === track.id);
              const updatedTracks= store.tracks()[index]= res!;
  
              patchState(store, { tracks: [...store.tracks()] });

            }),
            catchError(error => of({ error: error.message })),
          ).subscribe()
        });

      },



 
      emptyTrash() {
        trackService.emptyTrash().pipe(
          map(response => response.body ?? null),
          tap(() => {
            patchState(store, { searchCriteria: {...store.searchCriteria(), update: !store.searchCriteria.update }});
            // patchState(store, { tracks: [...store.tracks().filter(t=> t.id !== track.id)]});
          }),
          catchError(error => of({ error: error.message })),
        ).subscribe();       
      }

   
  
    };
  }),
  withHooks({
    onInit(store) {
      // store.load(0);
    },
  }),
);


