import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMusicRequest } from '../music-request.model';
import { MusicRequestService } from '../service/music-request.service';
import { MusicRequestFormService, MusicRequestFormGroup } from './music-request-form.service';

@Component({
  standalone: true,
  selector: 'jhi-music-request-update',
  templateUrl: './music-request-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MusicRequestUpdateComponent implements OnInit {
  isSaving = false;
  musicRequest: IMusicRequest | null = null;

  editForm: MusicRequestFormGroup = this.musicRequestFormService.createMusicRequestFormGroup();

  constructor(
    protected musicRequestService: MusicRequestService,
    protected musicRequestFormService: MusicRequestFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ musicRequest }) => {
      this.musicRequest = musicRequest;
      if (musicRequest) {
        this.updateForm(musicRequest);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const musicRequest = this.musicRequestFormService.getMusicRequest(this.editForm);
    if (musicRequest.id !== null) {
      this.subscribeToSaveResponse(this.musicRequestService.update(musicRequest));
    } else {
      this.subscribeToSaveResponse(this.musicRequestService.create(musicRequest));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMusicRequest>>): void {
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

  protected updateForm(musicRequest: IMusicRequest): void {
    this.musicRequest = musicRequest;
    this.musicRequestFormService.resetForm(this.editForm, musicRequest);
  }
}
