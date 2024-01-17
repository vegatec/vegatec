import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMusicRequest } from '../music-request.model';
import { MusicRequestService } from '../service/music-request.service';

@Component({
  standalone: true,
  templateUrl: './music-request-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MusicRequestDeleteDialogComponent {
  musicRequest?: IMusicRequest;

  constructor(
    protected musicRequestService: MusicRequestService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.musicRequestService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
