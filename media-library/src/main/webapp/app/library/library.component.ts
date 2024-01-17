import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  standalone: true,
  selector: 'jhi-library',
  templateUrl: './library.component.html',
  styleUrls: ['./library.component.scss'],
  imports: [RouterModule],
})
export class LibraryComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
}
