import { Directive, ElementRef, AfterViewChecked, Input, HostListener } from '@angular/core';

@Directive({
  selector: '[jhiAutoHeight]',
  standalone: true,
})
export class AutoHeightDirective implements AfterViewChecked {
  @Input() jhiAutoHeight!: string;
  private heigth = 0;

  constructor(private el: ElementRef) {}

  ngAfterViewChecked(): void {
    // call our matchHeight function here later
    this.heigth = parseInt(this.jhiAutoHeight, 10);
    const h = window.innerHeight - this.heigth;
    this.adjustHeight(this.el.nativeElement, h);
  }

  adjustHeight(parent: HTMLElement, height: number): void {
    const htmlEl = this.el.nativeElement as HTMLElement;
    htmlEl.style.height = `${height}px`;
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any): void {
    const h = event.target.innerHeight - this.heigth;
    this.adjustHeight(this.el.nativeElement, h);
  }
}
