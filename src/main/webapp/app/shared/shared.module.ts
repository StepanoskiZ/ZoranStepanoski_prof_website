import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';

// We no longer import the standalone components here.
// They will be imported directly where they are needed.

@NgModule({
  // We only export other NgModules.
  exports: [
    CommonModule,
    NgbModule,
    FontAwesomeModule,
    TranslateModule,
  ],
})
export default class SharedModule {}
