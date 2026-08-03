import { Component } from '@angular/core';
import SharedModule from 'app/shared/shared.module';

@Component({
  selector: 'jhi-vram-section',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './vram-section.component.html',
  styleUrl: './vram-section.component.scss',
})
export class VramSectionComponent {}
