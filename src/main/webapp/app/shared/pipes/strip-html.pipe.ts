import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'stripHtml',
  standalone: true,
})
export class StripHtmlPipe implements PipeTransform {
  transform(value: string | null | undefined): string {
    if (!value) {
      return '';
    }
    // Create a temporary DOM element to parse the HTML
    const div = document.createElement('div');
    div.innerHTML = value;
    // Return the text content, which strips all HTML tags
    return div.textContent || div.innerText || '';
  }
}
