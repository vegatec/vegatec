import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

// import SharedModule from '../../../shared/shared.module';
// import { ITEMS_PER_PAGE } from '../../../config/pagination.constants';
// import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from '../../../config/navigation.constants';

import SharedModule from '../../../shared/shared.module';
import { ITEMS_PER_PAGE } from '../../../config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from '../../../config/navigation.constants';

import { EntityArrayResponseType, ProductService } from '../../product/service/product.service';
import { IProduct } from '../../product/product.model';
import { ParseLinks } from '../../../core/util/parse-links.service';
// import { ParseLinks } from '../../../core/util/parse-links.service';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, ParamMap, Router, RouterModule } from '@angular/router';

import { BehaviorSubject, combineLatest, debounceTime, filter, Observable, Subject, switchMap, tap } from 'rxjs';
import { Data } from 'ws';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';

import { SortDirective, SortByDirective } from '../../../shared/sort';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from '../../../shared/date';
// import { SortDirective, SortByDirective } from '../../../shared/sort';
// import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from '../../../shared/date';
import { ITEM_ADDED_EVENT } from '../../../config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './add-products-dialog.component.html',
  imports: [
    SharedModule,
    RouterModule,
    FormsModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    InfiniteScrollModule,
  ],
})
export class AddProductsDialogComponent {
  productId = -1;

  products?: IProduct[];
  selectedProductId!: number;
  isLoading = false;

  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  links: { [key: string]: number } = {
    last: 0,
  };
  page = 1;

  filter$ = new Subject<string>();
  term: string = '';

  constructor(
    protected productService: ProductService,
    protected activeModal: NgbActiveModal,
    protected parseLinks: ParseLinks,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
  ) {}

  reset(): void {
    this.page = 1;
    this.products = [];
    this.load();
  }

  loadPage(page: number): void {
    this.page = page;
    this.load();
  }

  trackId = (_index: number, item: IProduct): number => this.productService.getProductIdentifier(item);

  ngOnInit(): void {
    this.load();

    this.filter$.pipe(debounceTime(500)).subscribe(term => {
      this.term = term;
      this.reset();
    });
  }

  load(): void {
    this.queryBackend(this.page, this.predicate, this.ascending).subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.page, this.predicate, this.ascending);
  }

  navigateToPage(page = this.page): void {
    this.handleNavigation(page, this.predicate, this.ascending);
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmAdd(): void {
    this.productService.addProduct(this.productId!, this.selectedProductId).subscribe(() => {
      this.activeModal.close(ITEM_ADDED_EVENT);
    });
  }

  search(term: any): void {
    this.filter$.next(term);
  }

  toggle(productId: number): void {
    this.selectedProductId = this.isSelected(productId) ? -1 : productId;
  }

  isSelected(productId: number): boolean {
    return this.selectedProductId === productId;
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.products = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IProduct[] | null): IProduct[] {
    // If there is previus link, data is a infinite scroll pagination content.
    if ('prev' in this.links) {
      const productsNew = this.products ?? [];
      if (data) {
        for (const d of data) {
          if (productsNew.map(op => op.id).indexOf(d.id) === -1) {
            productsNew.push(d);
          }
        }
      }
      return productsNew;
    }
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
  }

  protected queryBackend(page?: number, predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const pageToLoad: number = page ?? 1;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      eagerload: true,
      sort: this.getSortQueryParam(predicate, ascending),
    };

    queryObject['locatedAtId.equals'] = 1;
    if (this.term) queryObject['serialNumber.equals'] = +this.term;

    return this.productService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page = this.page, predicate?: string, ascending?: boolean): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }
}
