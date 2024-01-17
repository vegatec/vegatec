import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject, Subject, ReplaySubject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

//import * as moment from 'moment';
import { StreamState } from './stream-state';
import { Track } from 'app/store/models';

@Injectable({
  providedIn: 'root',
})
export class AudioPlayerService {
  audioEvents = ['ended', 'error', 'play', 'playing', 'pause', 'timeupdate', 'canplay', 'loadedmetadata', 'loadstart'];

  private stop$: Subject<boolean>;

  private currentTrack$: Subject<Track>;

  private audioObj: HTMLAudioElement;

  private state: StreamState = {
    playing: false,
    duration: 100,
    currentTime: 0,
    progress: 0,
    canPlay: false,
    ended: false,
    error: false,
  };

  private stateChange: BehaviorSubject<StreamState> = new BehaviorSubject(this.state);

  constructor() {
    this.stop$ = new Subject();
    this.currentTrack$ = new Subject();
    this.audioObj = new Audio();
  }

  pause(): void {
    this.audioObj.pause();
  }

  stop(): void {
    this.stop$.next(true);
  }

  seekTo(seconds: number): void {
    this.audioObj.currentTime = seconds;
  }

  volume(level: number): void {
    this.audioObj.volume = level / 100;
  }

  getState(): Observable<StreamState> {
    return this.stateChange.asObservable();
  }

  getCurrentTrack(): Observable<Track> {
    return this.currentTrack$.asObservable();
  }

  play(track: Track): Observable<any> {
    this.stop();
    this.currentTrack$.next(track);
    const url = `media/${track.filePath!}`;
    return this.streamObservable(url).pipe(takeUntil(this.stop$));
  }

  private playStream(url: string): Observable<any> {
    return this.streamObservable(url).pipe(takeUntil(this.stop$));
  }

  // play(): void {
  //   this.audioObj.play();
  // }

  private updateStateEvents(event: Event): void {
    //  console.log(event);
    switch (event.type) {
      case 'canplay':
        this.state.duration = this.audioObj.duration;
        this.state.canPlay = true;
        this.state.progress = 0;
        break;
      case 'playing':
        this.state.playing = true;
        break;
      case 'pause':
        this.state.playing = false;
        break;
      case 'timeupdate':
        this.state.currentTime = this.audioObj.currentTime;
        this.state.progress = this.state.duration === 0 ? 0 : (this.state.currentTime / this.state.duration!) * 100;
        break;
      case 'ended':
        this.state.ended = true;
        break;
      case 'error':
        this.resetState();
        this.state.error = true;
        break;
    }
    this.stateChange.next(this.state);
  }

  private resetState(): void {
    this.state = {
      playing: false,
      duration: 100,
      currentTime: 0,
      progress: 0,
      canPlay: false,
      ended: false,
      error: false,
    };
  }

  private streamObservable(url: string): Observable<any> {
    return new Observable(observer => {
      // Play audio
      this.audioObj.src = url;
      this.audioObj.load();
      this.audioObj.play();

      const handler = (event: Event): void => {
        this.updateStateEvents(event);
        observer.next(event);
      };

      this.addEvents(this.audioObj, this.audioEvents, handler);
      return () => {
        // Stop Playing
        this.audioObj.pause();
        this.audioObj.currentTime = 0;
        // remove event listeners
        this.removeEvents(this.audioObj, this.audioEvents, handler);
        // reset state
        this.resetState();
      };
    });
  }

  private addEvents(obj: any, events: string[], handler: any): void {
    events.forEach(event => {
      obj.addEventListener(event, handler);
    });
  }

  private removeEvents(obj: any, events: string[], handler: any): void {
    events.forEach(event => {
      obj.removeEventListener(event, handler);
    });
  }
}
