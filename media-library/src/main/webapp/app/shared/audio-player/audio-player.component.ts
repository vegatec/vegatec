//import { ThrowStmt } from '@angular/compiler/src/output/output_ast';
import { Component, OnInit } from '@angular/core';

import { AudioPlayerService } from './audio-player.service';
import { StreamState } from './stream-state';
import { NgbProgressbar } from '@ng-bootstrap/ng-bootstrap';
import { Track } from 'app/store/models';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'jhi-audio-player',
  templateUrl: './audio-player.component.html',
  styleUrls: ['./audio-player.component.scss'],
  imports: [NgbProgressbar, CommonModule],
})
export class AudioPlayerComponent implements OnInit {
  state?: StreamState;
  track?: Track;
  constructor(
    public audioPlayerService: AudioPlayerService,

    // private logger: NGXLogger,
    // private hotkeysService: HotkeysService
  ) {}
  ngOnInit(): void {
    this.audioPlayerService.getState().subscribe(state => {
      this.state = state;
    });

    this.audioPlayerService.getCurrentTrack().subscribe(track => {
      this.track = track;
    });
  }
}
