import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

import { TrackService } from '../service/track.service';
import { Track } from 'app/store/models';

@Component({
  standalone: true,
  templateUrl: './track-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TrackDeleteDialogComponent {
  track?: Track;

  constructor(
    protected trackService: TrackService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trackService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
