import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import SharedModule from 'app/shared/shared.module';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'jhi-privacy-policy',
  standalone: true,
  imports: [CommonModule, SharedModule, TranslateModule],
  templateUrl: './privacy-policy.component.html',
  styleUrls: ['./privacy-policy.component.scss'],
})
export class PrivacyPolicyComponent {}
