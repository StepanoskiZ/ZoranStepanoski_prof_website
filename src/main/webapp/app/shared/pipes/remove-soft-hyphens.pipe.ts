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

    // Chain all necessary replacements to fully sanitize the text for proper wrapping.
    return (
      value
        // 1. Remove soft hyphens (&shy;), which cause the unwanted hyphenated breaks.
        .replace(/&shy;|\u00AD/g, '')

        // 2. Replace non-breaking spaces (&nbsp;) with regular spaces to allow normal wrapping.
        .replace(/&nbsp;|\u00A0/g, ' ')

        // 3. Remove zero-width spaces, which can cause other invisible wrapping issues.
        .replace(/[\u200B-\u200D\uFEFF]/g, '')
    );
  }
}
