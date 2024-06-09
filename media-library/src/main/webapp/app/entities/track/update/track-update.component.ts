import { Component, OnInit, computed, effect, inject, signal } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable, OperatorFunction, of } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, finalize, map, switchMap, tap } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITrackType } from 'app/entities/track-type/track-type.model';
import { TrackTypeService } from 'app/entities/track-type/service/track-type.service';
import { TrackService } from '../service/track.service';
// import { TrackFormService, TrackFormGroup } from './track-form.service';
import { ITrack } from 'app/store/models';
import { TrackFormService } from './track-form.service';
import { TracksStore } from 'app/store/tracks-store';
import { SuggestionService } from '../service/suggestion.service';
import { NgbTypeahead } from '@ng-bootstrap/ng-bootstrap';
import { toObservable } from '@angular/core/rxjs-interop';

@Component({
  standalone: true,
  selector: 'jhi-track-update',
  templateUrl: './track-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, NgbTypeahead],
})
export class TrackUpdateComponent implements OnInit {


  trackStore = inject(TracksStore);
  
  isSaving = false;

  tracks= signal<ITrack[]>([]);

  artists= signal<string[]>([]);
  
  albums= signal<string[]>([]);

  albumArtists= signal<string[]>([]);

  genres= signal<string[]>([]);

  years= signal<number[]>([]);


  index= signal(0);



  artist= signal<string>('');
  album= signal<string>('');
  albumArtist= signal<string>('');
  genre= signal<string>('');
  year= signal<number>(0);

  originalArtist= signal<string>('');
  originalAlbum= signal<string>('');
  originalAlbumArtist= signal<string>('');
  originalGenre= signal<string>('');
  originalYear= signal<number>(0);

  artistHasChanged= computed(() => { return this.artist() !== this.originalArtist()});
  albumHasChanged= computed(() =>  { return this.album() !== this.originalAlbum()});
  albumArtistHasChanged= computed(() =>{ return (this.albumArtist() !== this.originalAlbumArtist())});
  genreHasChanged= computed(() => { return this.genre() !== this.originalGenre()});
  yearHasChanged= computed(() =>{ return this.year() !== this.originalYear()});

  hasNext= computed(() =>{ return this.index() < this.tracks().length-1});
  hasPrevious= computed(() =>{ return this.index() > 0});


  
  constructor(
    protected trackService: TrackService,
    protected trackFormService: TrackFormService,
    protected trackTypeService: TrackTypeService,
    protected activatedRoute: ActivatedRoute,
    protected suggestionService: SuggestionService
  ) {
    effect(()=> {
      


    })
  }


  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ track }) => {

      let  mapped: ITrack[] = this.trackStore.selected().map(t =>  {
        
        let track: ITrack= {
          id: t.id,
          name: toCamelCase(t.name!),
          artist: { name:  toCamelCase(t.artist?.name!)},         
           genre: { name:  toCamelCase(t.genre?.name!)},
           album: {
              name: toCamelCase(t.album?.name!),          
              artist: { name:   toCamelCase(t.album?.artist?.name!) },
              releasedYear:  t.album?.releasedYear!,

           }

        };
        return track;
           

      });


      this.tracks.set( mapped );

      // 
      this.artists.set([... new Set(this.tracks().map(t=> t.artist?.name!))]);
      this.genres.set([... new Set(this.tracks().map(t=> t.genre?.name!))]);
      this.albums.set([... new Set(this.tracks().map(t=> t.album?.name!))]);
      this.albumArtists.set([... new Set(this.tracks().map(t=> t.album?.artist?.name!))]);
      this.years.set([... new Set(this.tracks().map(t=> t.album?.releasedYear!))]);

      // record original values
      this.originalArtist.set(this.artist());     
      this.originalAlbum.set(this.album());      
      this.originalAlbumArtist.set(this.albumArtist());     
      this.originalGenre.set(this.genre());
      this.originalYear.set(this.year());


      if (this.artists().length == 1)
        this.artist.set( this.artists()[0]);

      if (this.albums().length == 1)
        this.album.set( this.albums()[0]);

      if (this.albumArtists().length == 1)
        this.albumArtist.set( this.albumArtists()[0]);
      
      if (this.genres().length == 1)
        this.genre.set( this.genres()[0]);
      
      if (this.years().length == 1)
        this.year.set( this.years()[0])

    });
  }

  onArtistChanged(e: any):void {
    console.log(`Artist Changed: ${e}`);

  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    // this.isSaving = true;
    // const track = this.trackFormService.getTrack(this.editForm);
    // if (track.id !== null) {
    //   this.subscribeToSaveResponse(this.trackService.update(track));
    // } else {
    //   this.subscribeToSaveResponse(this.trackService.create(track));
    // }


       this.tracks().forEach(s=>  { 
        if   ( s.artist && this.artistHasChanged())
          s.artist.name = this.artist();

        if (s.genre && this.genreHasChanged())
          s.genre.name = this.genre();


        if  ( s.album &&  this.albumHasChanged())
          s.album.name = this.album();

        if (s.album && s.album.artist  && this.albumArtistHasChanged()) 
          s.album.artist.name = this.albumArtist();
        
        if (s.album && this.yearHasChanged())
          s.album.releasedYear = this.year();

        this.subscribeToSaveResponse(this.trackService.partialUpdate(s));

      });


    

  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrack>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(track: ITrack): void {
    // this.track = track;
    // this.trackFormService.resetForm(this.editForm, track);

    // this.trackTypesSharedCollection = this.trackTypeService.addTrackTypeToCollectionIfMissing<ITrackType>(
    //   this.trackTypesSharedCollection,
    //   track.type,
    // );
  }


  artistSuggestions: OperatorFunction<string, readonly string[]> = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
     
      switchMap(term =>
        this.suggestionService.artists(term).pipe(
          map(res=> res.body!),
          catchError(() => {
  
            return of([]);
          }))
      ),

    );


  genreSuggestions: OperatorFunction<string, readonly string[]> = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      
      switchMap(term =>
        this.suggestionService.genres(term).pipe(
          map(res=> res.body!),
          catchError(() => {
            return of([]);
          }))
      ),

    );

  next(): void {
    if (this.hasNext()) 
        this.index.update(index => index +1)
  } 


  previous(): void {
      if (this.hasPrevious()) 
          this.index.update(index => index-1);

  }

  protected camelCase( input: string ):string  {
    var words = input.toLowerCase().split( ' ' );
    for ( var i = 0, len = words.length; i < len; i++ )
        words[i] = words[i].charAt( 0 ).toUpperCase() + words[i].slice( 1 );
    return words.join( ' ' );
};

}


function toCamelCase(input: string ) {
  var words = input.toLowerCase().split( ' ' );
  for ( var i = 0, len = words.length; i < len; i++ )
      words[i] = words[i].charAt( 0 ).toUpperCase() + words[i].slice( 1 );
  return words.join( ' ' );
}

