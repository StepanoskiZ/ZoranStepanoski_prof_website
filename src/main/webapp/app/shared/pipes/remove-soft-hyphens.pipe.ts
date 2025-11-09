import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'removeSoftHyphens',
  standalone: true,
})
export class RemoveSoftHyphensPipe implements PipeTransform {
  transform(value: string | null | undefined): string {
    if (!value) {
      return '';
    }
    // This regex finds and removes all soft hyphen HTML entities (&shy;)
    // and their Unicode character equivalent (\u00AD).
    return value
      .replace(/(­|\u00AD)/g, '') // soft hyphens
      .replace(/[\u200B-\u200D\uFEFF]/g, ''); // zero-width spaces
  }
}
