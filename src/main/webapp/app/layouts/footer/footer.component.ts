import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TranslateDirective } from 'app/shared/language';

@Component({
  selector: 'jhi-footer',
  standalone: true,
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
  imports: [TranslateDirective, RouterLink],
})
export default class FooterComponent {}
