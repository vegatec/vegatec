import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProductType } from '../product-type.model';
import { ProductTypeService } from '../service/product-type.service';

@Component({
  standalone: true,
  templateUrl: './product-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProductTypeDeleteDialogComponent {
  productType?: IProductType;

  constructor(
    protected productTypeService: ProductTypeService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
