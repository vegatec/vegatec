import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'jhi-url-receiver',
  standalone: true,
  imports: [],
  templateUrl: './url-receiver.component.html',
  styleUrl: './url-receiver.component.scss'
})
export class UrlReceiverComponent implements OnInit  {
  title!: string;
  url!: string;
  text!: string;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.title = params['title'];
      this.url = params['url'];
      this.text = params['text'];
    });
  }
}
