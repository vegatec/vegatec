import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITrackType } from '../track-type.model';
import { TrackTypeService } from '../service/track-type.service';

@Component({
  standalone: true,
  templateUrl: './track-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TrackTypeDeleteDialogComponent {
  trackType?: ITrackType;

  constructor(
    protected trackTypeService: TrackTypeService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trackTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
