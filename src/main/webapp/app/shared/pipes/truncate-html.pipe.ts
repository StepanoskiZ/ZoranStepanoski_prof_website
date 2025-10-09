import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'truncateHtml',
  standalone: true,
})
export class TruncateHtmlPipe implements PipeTransform {
  transform(value: string | null | undefined, maxLength: number = 150): string {
    if (!value) {
      return '';
    }

    const tempElement = document.createElement('div');
    tempElement.innerHTML = value.replace(/&nbsp;|\u00A0/g, ' ');

    if ((tempElement.textContent || '').length <= maxLength) {
      return value;
    }

    let truncatedHtml = '';
    let charCount = 0;
    const ellipsis = '...';

    function walk(node: Node) {
      if (charCount >= maxLength) {
        return;
      }

      if (node.nodeType === Node.TEXT_NODE) {
        const remaining = maxLength - charCount;
        const text = node.textContent || '';

        if (text.length > remaining) {
          truncatedHtml += text.substring(0, remaining);
          charCount = maxLength;
        } else {
          truncatedHtml += text;
          charCount += text.length;
        }
        return;
      }

      if (node.nodeType === Node.ELEMENT_NODE) {
        const element = node as Element;
        truncatedHtml += `<${element.tagName.toLowerCase()}>`;

        for (let i = 0; i < element.childNodes.length; i++) {
          walk(element.childNodes[i]);
          if (charCount >= maxLength) break;
        }

        truncatedHtml += `</${element.tagName.toLowerCase()}>`;
      }
    }

    walk(tempElement);

    return truncatedHtml + ellipsis;
  }
}
