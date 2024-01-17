import { Component, OnInit, inject } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import {
  combineLatest,
  concatMap,
  debounceTime,
  filter,
  groupBy,
  mergeMap,
  Observable,
  of,
  Subject,
  switchMap,
  tap,
  toArray,
  zip,
} from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortDirective, SortByDirective } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { FormsModule } from '@angular/forms';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { EntityArrayResponseType, AlbumService } from '../service/album.service';

import { IAlbum, IArtist } from '../album.model';
import dayjs from 'dayjs';
import { AutoHeightDirective } from 'app/shared/util/auto-height.directive';
import { AlbumsStore } from 'app/store/albums-store';
import { NavbarService } from 'app/layouts/navbar/navbar.service';

@Component({
  standalone: true,
  selector: 'jhi-album',
  templateUrl: './album.component.html',
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
export class AlbumComponent implements OnInit {
  store = inject(AlbumsStore);

  links: { [key: string]: number } = {
    last: 0,
  };

  constructor(
    protected activatedRoute: ActivatedRoute,
    public router: Router,

    protected modalService: NgbModal,
    protected navbarService: NavbarService,
  ) {}

  //albumId = (_index: number, item: IAlbum): number => this.albumService.getAlbumIdentifier(item);
  albumId = (_index: number, item: IAlbum): number => item.id!;

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      const temp = { ...params };
      Object.keys(temp).forEach(key => temp[key] === 'undefined' && delete temp[key]);
      this.store.updateCriteria(temp);
    });

    this.navbarService.searhTerm$.subscribe(term => this.store.updateCriteria({ ...this.store.searchCriteria(), filter: term }));
  }
}
