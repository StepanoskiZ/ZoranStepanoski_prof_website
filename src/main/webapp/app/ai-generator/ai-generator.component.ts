import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import SharedModule from 'app/shared/shared.module';
import { HttpClient } from '@angular/common/http';
import { ICurriculumVitae } from 'app/entities/curriculum-vitae/curriculum-vitae.model';
import { IProject } from 'app/entities/project/project.model';
import { ISkill } from 'app/entities/skill/skill.model';
import { IAboutMe } from 'app/entities/about-me/about-me.model';
import { CurriculumVitaeService } from 'app/entities/curriculum-vitae/service/curriculum-vitae.service';
import { ProjectService } from 'app/entities/project/service/project.service';
import { SkillService } from 'app/entities/skill/service/skill.service';
import { AboutMeService } from 'app/entities/about-me/service/about-me.service';

// Interface matching our backend's response DTO
export interface IAiAnalysisResponse {
  fitAnalysis: {
    scorePercentage: number;
    reasoning: string;
    matchingSkills: string[];
    missingSkills: string[];
  };
  applicationLetter: string;
  coverLetter: string;
  motivationLetter: string;
}

type SelectionType = 'cv' | 'project' | 'skill' | 'aboutMe';

@Component({
  selector: 'jhi-cv-ai-generator',
  standalone: true,
  imports: [CommonModule, FormsModule, SharedModule],
  templateUrl: './ai-generator.component.html',
  styleUrls: ['./ai-generator.component.scss'],
})
export class CvAiGeneratorComponent implements OnInit {
  // Data holders for all items from the database
  allMyCvEntries: ICurriculumVitae[] = [];
  allMyProjects: IProject[] = [];
  allMySkills: ISkill[] = [];
  allAboutMe: IAboutMe[] = [];

  // Data holders for items selected by the user via checkboxes
  selectedCvEntries: ICurriculumVitae[] = [];
  selectedProjects: IProject[] = [];
  selectedSkills: ISkill[] = [];
  selectedAboutMe: IAboutMe[] = [];

  // AI Model selection
  availableModels: string[] = [];
  selectedModel = '';

  jobPost = '';
  analysisResult: IAiAnalysisResponse | null = null;
  isLoading = false;

  constructor(
    private cvService: CurriculumVitaeService,
    private projectService: ProjectService,
    private skillService: SkillService,
    private aboutMeService: AboutMeService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    // Load all data from the database to populate the selection lists
    this.cvService.query().subscribe(res => (this.allMyCvEntries = res.body ?? []));
    this.projectService.query().subscribe(res => (this.allMyProjects = res.body ?? []));
    // Fetch all skills to avoid pagination issues in the selection list
//    this.skillService.query({ size: 100 }).subscribe(res => (this.allMySkills = res.body ?? []));
    this.http.get<ISkill[]>('/api/skills/all').subscribe(skills => (this.allMySkills = skills ?? []));
    this.aboutMeService.query().subscribe(res => (this.allAboutMe = res.body ?? []));
    this.fetchAiModels();
  }

  fetchAiModels(): void {
    this.http.get<string[]>('/api/ai/models').subscribe({
      next: models => {
        this.availableModels = models;
        if (models && models.length > 0) {
          this.selectedModel = models[0];
        }
      },
      error: err => {
        console.error('Error fetching AI models:', err);
        alert('Could not load the list of available AI models.');
      },
    });
  }

  // --- Logic to handle checkbox selections ---

  onSelectionChange(item: any, event: any, type: SelectionType): void {
    const isChecked = event.target.checked;
    const targetArray = this.getSelectionArray(type);

    if (isChecked) {
      targetArray.push(item);
    } else {
      const index = targetArray.findIndex(i => i.id === item.id);
      if (index > -1) {
        targetArray.splice(index, 1);
      }
    }
  }

  isSelected(item: any, type: SelectionType): boolean {
    return this.getSelectionArray(type).some(i => i.id === item.id);
  }

  toggleSelectAll(type: SelectionType, select: boolean): void {
    const sourceArray = this.getSourceArray(type);
    const targetArray = this.getSelectionArray(type);

    targetArray.length = 0; // Clear the array first
    if (select) {
      targetArray.push(...sourceArray); // Add all items
    }
  }

  private getSelectionArray(type: SelectionType): any[] {
    if (type === 'cv') return this.selectedCvEntries;
    if (type === 'project') return this.selectedProjects;
    if (type === 'skill') return this.selectedSkills;
    return this.selectedAboutMe;
  }

  private getSourceArray(type: SelectionType): any[] {
    if (type === 'cv') return this.allMyCvEntries;
    if (type === 'project') return this.allMyProjects;
    if (type === 'skill') return this.allMySkills;
    return this.allAboutMe;
  }

  // --- This function sends the SELECTED data to the backend ---

  generateAnalysis(): void {
    this.isLoading = true;
    this.analysisResult = null;

    const payload = {
      jobPost: this.jobPost,
      modelName: this.selectedModel,
      cvEntries: this.selectedCvEntries,
      projects: this.selectedProjects,
      skills: this.selectedSkills,
      aboutMe: this.selectedAboutMe,
    };

    const apiUrl = '/api/ai/analyze-and-generate';

    this.http.post<IAiAnalysisResponse>(apiUrl, payload).subscribe({
      next: response => {
        this.analysisResult = response;
        this.isLoading = false;
      },
      error: err => {
        console.error('Error generating analysis:', err);
        alert('An error occurred. Please check the console.');
        this.isLoading = false;
      },
    });
  }
}
