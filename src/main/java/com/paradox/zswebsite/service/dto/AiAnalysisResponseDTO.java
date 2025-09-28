package com.paradox.zswebsite.service.dto; // A good place for this DTO

import java.util.List;

public class AiAnalysisResponseDTO {

    private FitAnalysis fitAnalysis;
    private String coverLetter;
    private String applicationLetter;
    private String motivationLetter;

    // --- Getters and Setters ---
    public FitAnalysis getFitAnalysis() {
        return fitAnalysis;
    }

    public void setFitAnalysis(FitAnalysis fitAnalysis) {
        this.fitAnalysis = fitAnalysis;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public String getApplicationLetter() {
        return applicationLetter;
    }

    public void setApplicationLetter(String applicationLetter) {
        this.applicationLetter = applicationLetter;
    }

    public String getMotivationLetter() {
        return motivationLetter;
    }

    public void setMotivationLetter(String motivationLetter) {
        this.motivationLetter = motivationLetter;
    }

    public static class FitAnalysis {

        private int scorePercentage;
        private String reasoning;
        private List<String> matchingSkills;
        private List<String> missingSkills;

        // --- Getters and Setters ---
        public int getScorePercentage() {
            return scorePercentage;
        }

        public void setScorePercentage(int scorePercentage) {
            this.scorePercentage = scorePercentage;
        }

        public String getReasoning() {
            return reasoning;
        }

        public void setReasoning(String reasoning) {
            this.reasoning = reasoning;
        }

        public List<String> getMatchingSkills() {
            return matchingSkills;
        }

        public void setMatchingSkills(List<String> matchingSkills) {
            this.matchingSkills = matchingSkills;
        }

        public List<String> getMissingSkills() {
            return missingSkills;
        }

        public void setMissingSkills(List<String> missingSkills) {
            this.missingSkills = missingSkills;
        }
    }
}
