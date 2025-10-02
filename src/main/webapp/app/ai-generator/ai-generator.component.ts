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

@Component({
  selector: 'jhi-cv-ai-generator',
  standalone: true,
  imports: [CommonModule, FormsModule, SharedModule],
  templateUrl: './ai-generator.component.html',
})
export class CvAiGeneratorComponent implements OnInit {
  // Data holders for all items
  allMyCvEntries: ICurriculumVitae[] = [];
  allMyProjects: IProject[] = [];
  allMySkills: ISkill[] = [];
  allAboutMe: IAboutMe[] = [];

  // Data holders for selected items
  selectedCvEntries: ICurriculumVitae[] = [];
  selectedProjects: IProject[] = [];
  selectedSkills: ISkill[] = [];
  selectedAboutMe: IAboutMe[] = [];

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
    private http: HttpClient,
  ) {}

  ngOnInit(): void {
    // Load data from all four services
    this.cvService.query().subscribe(res => (this.allMyCvEntries = res.body ?? []));
    this.projectService.query().subscribe(res => (this.allMyProjects = res.body ?? []));
    this.skillService.query().subscribe(res => (this.allMySkills = res.body ?? []));
    this.aboutMeService.query().subscribe(res => (this.allAboutMe = res.body ?? []));

    this.fetchAiModels();
  }

  fetchAiModels(): void {
    this.http.get<string[]>('/api/ai/models').subscribe({
      next: models => {
        this.availableModels = models;
        // Automatically select the first model in the list if available
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

  generateAnalysis(): void {
    this.isLoading = true;
    this.analysisResult = null;

    const payload = {
      modelName: this.selectedModel,
      jobPost: this.jobPost,
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
