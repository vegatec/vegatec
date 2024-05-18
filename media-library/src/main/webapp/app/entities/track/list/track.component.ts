import { Component, OnInit, inject } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Params, Router, RouterModule } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortDirective, SortByDirective } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { FormsModule } from '@angular/forms';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { EntityArrayResponseType, TrackService } from '../service/track.service';
import { TrackDeleteDialogComponent } from '../delete/track-delete-dialog.component';

import { AutoHeightDirective } from 'app/shared/util/auto-height.directive';
import { AudioPlayerService } from 'app/shared/audio-player/audio-player.service';
import { TracksStore } from 'app/store/tracks-store';
import { ITrack } from 'app/store/models';
import { NavbarService } from 'app/layouts/navbar/navbar.service';

@Component({
  standalone: true,
  selector: 'jhi-track',
  templateUrl: './track.component.html',
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    InfiniteScrollModule,
    AutoHeightDirective,
  ],
})
export class TrackComponent implements OnInit {
  store = inject(TracksStore);

  constructor(
    protected activatedRoute: ActivatedRoute,
    public router: Router,

    protected modalService: NgbModal,
    protected audioPlayerService: AudioPlayerService,
    protected navbarService: NavbarService,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      const temp = { ...params };
      Object.keys(temp).forEach(key => temp[key] === 'undefined' && delete temp[key]);
      this.store.updateCriteria(temp);
    });

    this.navbarService.searhTerm$.subscribe(term => this.store.updateCriteria({ ...this.store.searchCriteria(), filter: term }));
  }

  loadPage(page: number): void {
    this.store.load(page);
  }

  //trackId = (_index: number, item: Track): number => this.trackService.getTrackIdentifier(item);
  trackId = (_index: number, item: ITrack): number => item.id!;

  // moveToTrash(): void {
  //   this.store.selected().forEach(t=> this.store.delete(t));
  // }

  delete(track: ITrack): void {
    const modalRef = this.modalService.open(TrackDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.track = track;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        //switchMap(() => this.loadFromBackendWithRouteInformations()),
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          //    this.onResponseSuccess(res);
        },
      });
  }

  play(track: ITrack): void {
    //const url= `media/${track.filePath!}`
    this.audioPlayerService.play(track).subscribe(e => {});
  }
}
