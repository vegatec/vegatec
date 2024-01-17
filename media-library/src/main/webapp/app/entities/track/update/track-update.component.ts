import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITrackType } from 'app/entities/track-type/track-type.model';
import { TrackTypeService } from 'app/entities/track-type/service/track-type.service';
import { ITrack } from '../track.model';
import { TrackService } from '../service/track.service';
import { TrackFormService, TrackFormGroup } from './track-form.service';

@Component({
  standalone: true,
  selector: 'jhi-track-update',
  templateUrl: './track-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TrackUpdateComponent implements OnInit {
  isSaving = false;
  track: ITrack | null = null;

  trackTypesSharedCollection: ITrackType[] = [];

  editForm: TrackFormGroup = this.trackFormService.createTrackFormGroup();

  constructor(
    protected trackService: TrackService,
    protected trackFormService: TrackFormService,
    protected trackTypeService: TrackTypeService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareTrackType = (o1: ITrackType | null, o2: ITrackType | null): boolean => this.trackTypeService.compareTrackType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ track }) => {
      this.track = track;
      if (track) {
        this.updateForm(track);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const track = this.trackFormService.getTrack(this.editForm);
    if (track.id !== null) {
      this.subscribeToSaveResponse(this.trackService.update(track));
    } else {
      this.subscribeToSaveResponse(this.trackService.create(track));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrack>>): void {
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

  protected updateForm(track: ITrack): void {
    this.track = track;
    this.trackFormService.resetForm(this.editForm, track);

    this.trackTypesSharedCollection = this.trackTypeService.addTrackTypeToCollectionIfMissing<ITrackType>(
      this.trackTypesSharedCollection,
      track.type,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.trackTypeService
      .query()
      .pipe(map((res: HttpResponse<ITrackType[]>) => res.body ?? []))
      .pipe(
        map((trackTypes: ITrackType[]) =>
          this.trackTypeService.addTrackTypeToCollectionIfMissing<ITrackType>(trackTypes, this.track?.type),
        ),
      )
      .subscribe((trackTypes: ITrackType[]) => (this.trackTypesSharedCollection = trackTypes));
  }
}
