import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, Validators, ReactiveFormsModule, FormArray, FormControl } from '@angular/forms';
import { HttpResponse } from '@angular/common/http';

import SharedModule from 'app/shared/shared.module';
import { ISkill } from 'app/entities/skill/skill.model';
import { SkillService } from 'app/entities/skill/service/skill.service';

@Component({
  selector: 'jhi-intake-form',
  standalone: true,
  imports: [CommonModule, SharedModule, ReactiveFormsModule],
  templateUrl: './intake-form.component.html',
  styleUrls: ['./intake-form.component.scss'],
})
export class IntakeFormComponent implements OnInit {
  isSubmitting = false;
  skills: ISkill[] = [];

  private fb = inject(FormBuilder);
  private skillService = inject(SkillService);

  form = this.fb.group({
    name: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    preferredStack: this.fb.array([]), // We will populate this dynamically
  });

  get preferredStackFormArray(): FormArray {
    return this.form.get('preferredStack') as FormArray;
  }

  ngOnInit(): void {
    this.skillService.query().subscribe((res: HttpResponse<ISkill[]>) => {
      this.skills = res.body ?? [];
      this.addSkillCheckboxes();
    });
  }

  private addSkillCheckboxes(): void {
    this.skills.forEach(() => this.preferredStackFormArray.push(new FormControl(false)));
  }

  submit(): void {
    if (this.form.invalid) {
      // This is a good UX practice: it triggers validation messages on all fields.
      this.form.markAllAsTouched();
      return;
    }
    this.isSubmitting = true;

    // --- START OF FIX ---
    // Get the raw boolean values directly from the FormArray and cast it.
    const preferredStackValues = this.preferredStackFormArray.getRawValue() as boolean[];

    const selectedSkills = preferredStackValues.map((checked, i) => (checked ? this.skills[i].name : null)).filter(value => value !== null);
    // --- END OF FIX ---

    const submissionData = {
      name: this.form.value.name,
      email: this.form.value.email,
      preferredStack: selectedSkills,
    };

    console.log('Form Submitted!', submissionData);
    // Here you would create a new service to POST this data to a new backend endpoint
    // For now, we just log it to the console.

    setTimeout(() => {
      this.isSubmitting = false;
      alert('Thank you for your submission!');
      this.form.reset();
      // Important: clear and re-populate the checkboxes after reset
      this.preferredStackFormArray.clear();
      this.addSkillCheckboxes();
    }, 1000);
  }
}
