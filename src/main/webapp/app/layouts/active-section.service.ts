import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ActiveSectionService {
  /**
   * We use a signal to hold the ID of the currently active section.
   * Signals are a modern Angular feature for creating reactive values.
   * When this signal's value changes, any part of the app using it will be notified.
   * We give it a default value of 'home' so the "Home" link is active on initial page load.
   */
  public activeSection = signal<string>('home');

  /**
   * This is a public method that other components (like LandingComponent) can call
   * to update the value of the active section.
   * @param sectionId The ID of the section that is now visible (e.g., 'about', 'services').
   */
  public setActiveSection(sectionId: string): void {
    this.activeSection.set(sectionId);
  }
}
