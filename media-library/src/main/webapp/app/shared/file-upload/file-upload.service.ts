import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { ApplicationConfigService } from "app/core/config/application-config.service";
import { Observable, map } from "rxjs";

@Injectable({ providedIn: 'root' })
export class FileUploadService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/files');


  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}


uploadImageFile(file: File, folder: string): Observable<boolean> {

    const formData: FormData = new FormData();
    formData.append('file', file, file.name);
    formData.append('folder', folder);
    return this.http
      .post(`${this.resourceUrl}/image`, formData)
      .pipe(
       map(() => { return true; }),
      );

  }


uploadMediaFile(file: File): Observable<boolean> {

    const formData: FormData = new FormData();
    formData.append('file', file, file.name);

    return this.http
      .post(`${this.resourceUrl}/media`, formData)
      .pipe(
       map(() => { return true; }),
      );

  }

}