import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'stripHtml',
  standalone: true,
})
export class StripHtmlPipe implements PipeTransform {
  transform(value: string | null | undefined, maxLength: number = 150): string {
    if (!value) {
      return '';
    }

    const stringWithRegularSpaces = value.replace(/&nbsp;|\u00A0/g, ' ');

    let text = stringWithRegularSpaces.replace(/<[^>]*>/g, ' ');

    text = text.replace(/\s\s+/g, ' ').trim();

    if (text.length <= maxLength) {
      return text;
    }

    let truncatedText = text.substring(0, maxLength);
    const lastSpaceIndex = truncatedText.lastIndexOf(' ');

    if (lastSpaceIndex > 0) {
      truncatedText = truncatedText.substring(0, lastSpaceIndex);
    }

    return truncatedText + '...';
  }
}
