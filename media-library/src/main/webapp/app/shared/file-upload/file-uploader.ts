import {
    Directive,
    HostBinding,
    HostListener,
    Output,
    EventEmitter
  } from "@angular/core";
  import { DomSanitizer } from "@angular/platform-browser";
  import { IFile } from "./file";
  
  enum DropColor {
    Default = "#C6E4F100",
    Over = "#ACADAD"
  }
  
  @Directive({
    selector: "[jhiImageUpload]",
    standalone: true
  })
  export class ImageUploaderDirective {
    @Output() dropFiles: EventEmitter<IFile[]> = new EventEmitter();
      @HostBinding("style.background") backgroundColor = DropColor.Default;
  
    constructor(private sanitizer: DomSanitizer) {}
  
    @HostListener("dragover", ["$event"]) public dragOver(event: DragEvent) {
      event.preventDefault();
      event.stopPropagation();
       this.backgroundColor = DropColor.Over;
    }
  
    @HostListener("dragleave", ["$event"]) public dragLeave(event: DragEvent) {
      event.preventDefault();
      event.stopPropagation();
       this.backgroundColor = DropColor.Default;
    }
  
    @HostListener("drop", ["$event"]) public drop(event: DragEvent) {
      event.preventDefault();
      event.stopPropagation();
       this.backgroundColor = DropColor.Default;
  
      let fileList = event.dataTransfer?.files;
      let files: IFile[] = [];
  
      for (let i = 0; i < fileList!.length; i++) {
        const file = fileList![i];
        const url = this.sanitizer.bypassSecurityTrustUrl(
          window.URL.createObjectURL(file)
        );
        files.push({ file, url });
      }
      if (files.length > 0) {
        this.dropFiles.emit(files);
      }
    }
  }
  