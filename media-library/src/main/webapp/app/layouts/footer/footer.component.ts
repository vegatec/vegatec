import { Component } from '@angular/core';
import { AudioPlayerComponent } from 'app/shared/audio-player/audio-player.component';

@Component({
  standalone: true,
  selector: 'jhi-footer',
  templateUrl: './footer.component.html',
  imports: [AudioPlayerComponent],
})
export default class FooterComponent {}
