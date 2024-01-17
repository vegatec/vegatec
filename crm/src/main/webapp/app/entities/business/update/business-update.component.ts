import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBusiness } from '../business.model';
import { BusinessService } from '../service/business.service';
import { BusinessFormService, BusinessFormGroup } from './business-form.service';

@Component({
  standalone: true,
  selector: 'jhi-business-update',
  templateUrl: './business-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BusinessUpdateComponent implements OnInit {
  isSaving = false;
  business: IBusiness | null = null;

  editForm: BusinessFormGroup = this.businessFormService.createBusinessFormGroup();

  constructor(
    protected businessService: BusinessService,
    protected businessFormService: BusinessFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ business }) => {
      this.business = business;
      if (business) {
        this.updateForm(business);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const business = this.businessFormService.getBusiness(this.editForm);
    if (business.id !== null) {
      this.subscribeToSaveResponse(this.businessService.update(business));
    } else {
      this.subscribeToSaveResponse(this.businessService.create(business));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBusiness>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(business: IBusiness): void {
    this.business = business;
    this.businessFormService.resetForm(this.editForm, business);
  }
}
