//package com.paradox.zswebsite.web.rest;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.cloud.vertexai.VertexAI;
//import com.google.cloud.vertexai.api.GenerateContentResponse;
//import com.google.cloud.vertexai.generativeai.GenerativeModel;
//import com.google.cloud.vertexai.generativeai.ResponseHandler;
//import com.paradox.zswebsite.service.dto.*;
//import com.paradox.zswebsite.web.rest.vm.AiAnalysisRequestDTO;
//import java.io.IOException;
//import java.util.List;
//import java.util.stream.Collectors;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//
//@RestController
//@RequestMapping("/api/ai")
//public class AiGeneratorResource {
//
//    private final Logger log = LoggerFactory.getLogger(AiGeneratorResource.class);
//
//    private final String googleApiKey;
//    private final String gcpProjectId;
//    private final String gcpLocation;
//    private final ObjectMapper objectMapper;
//
//    public AiGeneratorResource(
//        @Value("${application.ai.google-api-key}") String apiKey,
//        @Value("${application.ai.project-id}") String projectId,
//        @Value("${application.ai.location}") String location,
//        ObjectMapper objectMapper
//    ) {
//        this.googleApiKey = apiKey;
//        this.gcpProjectId = projectId;
//        this.gcpLocation = location;
//        this.objectMapper = objectMapper;
//    }
//
//    @PostMapping("/analyze-and-generate")
//    public ResponseEntity<AiAnalysisResponseDTO> analyzeAndGenerate(@RequestBody AiAnalysisRequestDTO request) {
//        log.debug("REST request to analyze profile and generate letters");
//
//        String cvData = formatCvData(request.getCvEntries());
//        String projectData = formatProjectData(request.getProjects());
//        String skillData = formatSkillData(request.getSkills());
//        String aboutMeData = formatAboutMeData(request.getAboutMe());
//        String prompt = buildPrompt(cvData, projectData, skillData, aboutMeData, request.getJobPost());
//
//        try (VertexAI vertexAI = new VertexAI(this.gcpProjectId, this.gcpLocation)) {
//            GenerativeModel model = new GenerativeModel("gemini-1.0-pro", vertexAI);
//            GenerateContentResponse response = model.generateContent(prompt);
//            String jsonResponse = ResponseHandler.getText(response);
//
//            log.debug("Raw JSON response from AI: {}", jsonResponse);
//
//            AiAnalysisResponseDTO analysisResponse = objectMapper.readValue(jsonResponse, AiAnalysisResponseDTO.class);
//            return ResponseEntity.ok(analysisResponse);
//        } catch (IOException e) {
//            log.error("Error communicating with Google AI or parsing response", e);
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate analysis due to AI service error.");
//        }
//    }
//
//    private String formatCvData(List<CurriculumVitaeDTO> cvEntries) {
//        if (cvEntries == null || cvEntries.isEmpty()) return "No work experience provided.";
//        return cvEntries
//            .stream()
//            .map(cv ->
//                String.format(
//                    "Company: %s\nDescription: %s\nFrom %s to %s\nStatus: %s\nType: %s\n---\n",
//                    cv.getCompanyName(),
//                    cv.getJobDescriptionHTML(),
//                    cv.getStartDate(),
//                    cv.getEndDate(),
//                    cv.getStatus(),
//                    cv.getType()
//                )
//            )
//            .collect(Collectors.joining("\n"));
//    }
//
//    private String formatProjectData(List<ProjectDTO> projects) {
//        if (projects == null || projects.isEmpty()) return "No projects provided.";
//        return projects
//            .stream()
//            .map(p ->
//                String.format("Title: %s\nDescription: %s\nCategory: %s\n---\n", p.getTitle(), p.getDescriptionHTML(), p.getCategory())
//            )
//            .collect(Collectors.joining("\n"));
//    }
//
//    private String formatSkillData(List<SkillDTO> skills) {
//        if (skills == null || skills.isEmpty()) return "No skills provided.";
//        return skills.stream().map(SkillDTO::getName).collect(Collectors.joining(", "));
//    }
//
//    private String formatAboutMeData(List<AboutMeDTO> aboutMeEntries) {
//        if (aboutMeEntries == null || aboutMeEntries.isEmpty()) return "No personal information provided.";
//        return aboutMeEntries.stream().map(AboutMeDTO::getContentHtml).collect(Collectors.joining("\n\n"));
//    }
//
//    private String buildPrompt(String cvData, String projectData, String skillData, String aboutMeData, String jobPost) {
//        // NOTE: Corrected the JSON format in the prompt (commas between fields).
//        return String.format(
//            """
//            You are an expert career analyst and professional writer named 'CareerCraft AI'. Your task is to perform a detailed analysis of my professional profile against a specific job post and then write a compelling cover, application and motivation letter.
//
//            You will be given the following information:
//            1.  **MY WORK EXPERIENCE:** A list of my past jobs.
//            2.  **MY PROJECTS:** A list of relevant projects I have completed.
//            3.  **MY SKILLS:** A list of my technical and soft skills.
//            4.  **ABOUT ME:** The description of my personal data like finished schools, hobbies, interests.
//            5.  **THE JOB POST:** The description of the job I am applying for.
//
//            You must perform four tasks and return the output in a single, valid JSON object. Do not include any text, comments, or markdown outside of the JSON object.
//
//            **TASK 1: FIT ANALYSIS**
//            -   Carefully read each requirement in THE JOB POST.
//            -   Compare these requirements against the evidence provided in MY WORK EXPERIENCE, MY PROJECTS, MY SKILLS and ABOUT ME.
//            -   Calculate a "Fit Score" as a percentage (0-100). This score should reflect the proportion of job requirements that I meet.
//            -   Provide a brief, data-driven "Reasoning" for the score.
//            -   List the key "Matching Skills" and "Missing Skills" based on the job post.
//
//            **TASK 2: COVER LETTER GENERATION**
//            -   Write a professional, confident, and enthusiastic cover letter.
//            -   The letter must connect my experiences from all data sources directly to the needs outlined in the job post.
//            -   The letter should be concise (300-400 words) and structured with an introduction, body, and conclusion.
//
//            **TASK 3: APPLICATION LETTER GENERATION**
//            -   Write a professional, confident, and enthusiastic application letter. This is more formal than a cover letter and should explicitly state the position being applied for.
//            -   It should summarize my qualifications and directly reference how my skills match the job requirements.
//            -   The letter should be concise (300-400 words) and structured.
//
//            **TASK 4: MOTIVATION LETTER GENERATION**
//            -   Write a passionate and enthusiastic motivation letter.
//            -   This letter should focus more on *why* I want this specific job at this specific company, my personal connection to the company's mission, and my future aspirations.
//            -   It should still use my experience as proof of my commitment and ability.
//            -   The letter should be slightly longer (400-500 words).
//
//            **THE OUTPUT MUST be in the following JSON format:**
//            {
//              "fitAnalysis": {
//                "scorePercentage": <number>,
//                "reasoning": "<string: a few sentences explaining the score>",
//                "matchingSkills": ["<string>", "<string>", ...],
//                "missingSkills": ["<string>", "<string>", ...]
//              },
//              "coverLetter": "<string: the full text of the cover letter with proper line breaks (\\n)>",
//              "applicationLetter": "<string: the full text of the application letter with proper line breaks (\\n)>",
//              "motivationLetter": "<string: the full text of the motivation letter with proper line breaks (\\n)>"
//            }
//
//            ---
//            **MY WORK EXPERIENCE (from CurriculumVitae):**
//            %s
//            ---
//            **MY PROJECTS:**
//            %s
//            ---
//            **MY SKILLS:**
//            %s
//            ---
//            **ABOUT ME:**
//            %s
//            ---
//            **THE JOB POST:**
//            %s
//            ---
//
//            Now, generate the JSON response.
//            """,
//            cvData,
//            projectData,
//            skillData,
//            aboutMeData,
//            jobPost
//        );
//    }
//}
package com.paradox.zswebsite.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paradox.zswebsite.service.dto.*;
import com.paradox.zswebsite.web.rest.vm.AiAnalysisRequestDTO;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import com.paradox.zswebsite.security.AuthoritiesConstants;
import com.paradox.zswebsite.config.ApplicationProperties;

@RestController
@RequestMapping("/api/ai")
public class AiGeneratorResource {

    private final Logger log = LoggerFactory.getLogger(AiGeneratorResource.class);

    private final String googleApiKey;
    private final RestTemplate aiRestTemplate;
    private final ObjectMapper objectMapper;

    // Correctly inject the RestTemplate and the API key from application.yml
    public AiGeneratorResource(
        ApplicationProperties applicationProperties,
//        @Value("${application.ai.google-api-key:}") String apiKey,
        @Qualifier("aiRestTemplate") RestTemplate restTemplate,
        ObjectMapper objectMapper
    ) {
//        this.googleApiKey = apiKey;
        this.googleApiKey = applicationProperties.getAi().getGoogleApiKey();
        this.aiRestTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/analyze-and-generate")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<AiAnalysisResponseDTO> analyzeAndGenerate(@RequestBody AiAnalysisRequestDTO request) {
        log.debug("REST request to analyze profile and generate letters");

        if (googleApiKey == null || googleApiKey.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "AI service is not configured: API key is missing.");
        }

        // --- YOUR DATA FORMATTING LOGIC IS CALLED HERE ---
        String prompt = buildPrompt(
            formatCvData(request.getCvEntries()),
            formatProjectData(request.getProjects()),
            formatSkillData(request.getSkills()),
            formatAboutMeData(request.getAboutMe()),
            request.getJobPost()
        );

//        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + this.googleApiKey;
//        String apiUrl = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash-latest:generateContent?key=" + this.googleApiKey;
//        String apiUrl = "https://generativelanguage.googleapis.com/v1/models/gemini-1.0-pro:generateContent?key=" + this.googleApiKey;
        String apiUrl = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash-latest:generateContent?key=" + this.googleApiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(Collections.singletonMap("contents",
                Collections.singletonList(Collections.singletonMap("parts",
                    Collections.singletonList(Collections.singletonMap("text", prompt))))));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize AI request body", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to build AI request body.");
        }

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<JsonNode> response = aiRestTemplate.postForEntity(apiUrl, entity, JsonNode.class);

            JsonNode responseBody = response.getBody();
            if (responseBody == null || !responseBody.has("candidates") || !responseBody.get("candidates").isArray() || responseBody.get("candidates").size() == 0) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "AI service returned an invalid or empty response.");
            }
            String jsonResponseText = responseBody.get("candidates").get(0).get("content").get("parts").get(0).get("text").asText();

            log.debug("Raw JSON response from AI: {}", jsonResponseText);

            // Clean the response: Gemini sometimes wraps its JSON in markdown ```json ... ```
            jsonResponseText = jsonResponseText.replace("```json", "").replace("```", "").trim();

            AiAnalysisResponseDTO analysisResponse = objectMapper.readValue(jsonResponseText, AiAnalysisResponseDTO.class);
            return ResponseEntity.ok(analysisResponse);

        } catch (Exception e) {
            log.error("Error communicating with Google AI or parsing response", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate analysis due to AI service error.", e);
        }
    }

    // --- ALL OF YOUR HELPER METHODS ARE INCLUDED BELOW ---

    private String formatCvData(List<CurriculumVitaeDTO> cvEntries) {
        if (cvEntries == null || cvEntries.isEmpty()) return "No work experience provided.";
        return cvEntries
            .stream()
            .map(cv ->
                String.format(
                    "Company: %s\nDescription: %s\nFrom %s to %s\nStatus: %s\nType: %s\n---\n",
                    cv.getCompanyName(),
                    cv.getJobDescriptionHTML(),
                    cv.getStartDate(),
                    cv.getEndDate(),
                    cv.getStatus(),
                    cv.getType()
                )
            )
            .collect(Collectors.joining("\n"));
    }

    private String formatProjectData(List<ProjectDTO> projects) {
        if (projects == null || projects.isEmpty()) return "No projects provided.";
        return projects
            .stream()
            .map(p ->
                String.format("Title: %s\nDescription: %s\nCategory: %s\n---\n", p.getTitle(), p.getDescriptionHTML(), p.getCategory())
            )
            .collect(Collectors.joining("\n"));
    }

    private String formatSkillData(List<SkillDTO> skills) {
        if (skills == null || skills.isEmpty()) return "No skills provided.";
        return skills.stream().map(SkillDTO::getName).collect(Collectors.joining(", "));
    }

    private String formatAboutMeData(List<AboutMeDTO> aboutMeEntries) {
        if (aboutMeEntries == null || aboutMeEntries.isEmpty()) return "No personal information provided.";
        return aboutMeEntries.stream().map(AboutMeDTO::getContentHtml).collect(Collectors.joining("\n\n"));
    }

    private String buildPrompt(String cvData, String projectData, String skillData, String aboutMeData, String jobPost) {
        return String.format(
            """
            You are an expert career analyst and professional writer named 'CareerCraft AI'. Your task is to perform a detailed analysis of my professional profile against a specific job post and then write a compelling cover, application and motivation letter.

            You will be given the following information:
            1.  **MY WORK EXPERIENCE:** A list of my past jobs.
            2.  **MY PROJECTS:** A list of relevant projects I have completed.
            3.  **MY SKILLS:** A list of my technical and soft skills.
            4.  **ABOUT ME:** The description of my personal data like finished schools, hobbies, interests.
            5.  **THE JOB POST:** The description of the job I am applying for.

            You must perform four tasks and return the output in a single, valid JSON object. Do not include any text, comments, or markdown outside of the JSON object.

            **TASK 1: FIT ANALYSIS**
            -   Carefully read each requirement in THE JOB POST.
            -   Compare these requirements against the evidence provided in MY WORK EXPERIENCE, MY PROJECTS, MY SKILLS and ABOUT ME.
            -   Calculate a "Fit Score" as a percentage (0-100). This score should reflect the proportion of job requirements that I meet.
            -   Provide a brief, data-driven "Reasoning" for the score.
            -   List the key "Matching Skills" and "Missing Skills" based on the job post.

            **TASK 2: COVER LETTER GENERATION**
            -   Write a professional, confident, and enthusiastic cover letter.
            -   The letter must connect my experiences from all data sources directly to the needs outlined in the job post.
            -   The letter should be concise (300-400 words) and structured with an introduction, body, and conclusion.

            **TASK 3: APPLICATION LETTER GENERATION**
            -   Write a professional, confident, and enthusiastic application letter. This is more formal than a cover letter and should explicitly state the position being applied for.
            -   It should summarize my qualifications and directly reference how my skills match the job requirements.
            -   The letter should be concise (300-400 words) and structured.

            **TASK 4: MOTIVATION LETTER GENERATION**
            -   Write a passionate and enthusiastic motivation letter.
            -   This letter should focus more on *why* I want this specific job at this specific company, my personal connection to the company's mission, and my future aspirations.
            -   It should still use my experience as proof of my commitment and ability.
            -   The letter should be slightly longer (400-500 words).

            **THE OUTPUT MUST be in the following JSON format:**
            {
              "fitAnalysis": {
                "scorePercentage": <number>,
                "reasoning": "<string: a few sentences explaining the score>",
                "matchingSkills": ["<string>", "<string>", ...],
                "missingSkills": ["<string>", "<string>", ...]
              },
              "coverLetter": "<string: the full text of the cover letter with proper line breaks (\\n)>",
              "applicationLetter": "<string: the full text of the application letter with proper line breaks (\\n)>",
              "motivationLetter": "<string: the full text of the motivation letter with proper line breaks (\\n)>"
            }

            ---
            **MY WORK EXPERIENCE (from CurriculumVitae):**
            %s
            ---
            **MY PROJECTS:**
            %s
            ---
            **MY SKILLS:**
            %s
            ---
            **ABOUT ME:**
            %s
            ---
            **THE JOB POST:**
            %s
            ---

            Now, generate the JSON response.
            """,
            cvData,
            projectData,
            skillData,
            aboutMeData,
            jobPost
        );
    }
}
