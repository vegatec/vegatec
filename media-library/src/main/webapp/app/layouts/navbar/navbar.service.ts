import { ThisReceiver } from '@angular/compiler';
import { Injectable } from '@angular/core';
import { Observable, Subject, merge } from 'rxjs';
import { debounceTime, distinctUntilChanged, filter, map } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class NavbarService {
  public readonly searhTerm$: Observable<string>;
  private readonly _searchTerm = new Subject<string>();
  private readonly _clear= new Subject<string>();
  constructor() {
    this.searhTerm$ = this._searchTerm.pipe(
      // eslint-disable-next-line @typescript-eslint/no-unsafe-return
     // map((e: any) => e.target.value),
      debounceTime(400),
      distinctUntilChanged(),
      

      // filter(term => term.length > 0)
    );
  }

  search(term: string ): void {
    this._searchTerm.next(term);
  }




  
}
