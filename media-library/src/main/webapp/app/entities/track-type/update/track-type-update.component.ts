import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITrackType } from '../track-type.model';
import { TrackTypeService } from '../service/track-type.service';
import { TrackTypeFormService, TrackTypeFormGroup } from './track-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-track-type-update',
  templateUrl: './track-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TrackTypeUpdateComponent implements OnInit {
  isSaving = false;
  trackType: ITrackType | null = null;

  editForm: TrackTypeFormGroup = this.trackTypeFormService.createTrackTypeFormGroup();

  constructor(
    protected trackTypeService: TrackTypeService,
    protected trackTypeFormService: TrackTypeFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trackType }) => {
      this.trackType = trackType;
      if (trackType) {
        this.updateForm(trackType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trackType = this.trackTypeFormService.getTrackType(this.editForm);
    if (trackType.id !== null) {
      this.subscribeToSaveResponse(this.trackTypeService.update(trackType));
    } else {
      this.subscribeToSaveResponse(this.trackTypeService.create(trackType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrackType>>): void {
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

  protected updateForm(trackType: ITrackType): void {
    this.trackType = trackType;
    this.trackTypeFormService.resetForm(this.editForm, trackType);
  }
}
