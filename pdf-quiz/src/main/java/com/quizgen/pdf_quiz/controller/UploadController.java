package com.quizgen.pdf_quiz.controller;

import com.quizgen.pdf_quiz.service.LLMService;
import com.quizgen.pdf_quiz.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class UploadController {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private LLMService llmService;

    private String lastCleanedText = null;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPdf(@RequestParam("file") MultipartFile file,
                                       @RequestParam(name = "numQuestions", defaultValue = "5") int numQuestions) {
        try {
            System.out.println("Received PDF upload request.");

            String cleanedText = pdfService.extractText(file);
            this.lastCleanedText = cleanedText;

            try (FileWriter writer = new FileWriter("cleaned_output.txt")) {
                writer.write(cleanedText);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String trimmedText = cleanedText.length() > 1000 ? cleanedText.substring(0, 1000) : cleanedText;
            String quizQuestions = llmService.generateQuestionsWithOllama(trimmedText, numQuestions);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "PDF processed successfully.");
            response.put("extractedText", cleanedText);
            response.put("quizGeneratedByLLM", quizQuestions);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Failed to process PDF.");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/summary")
    public ResponseEntity<?> generateSummary() {
        try {
            if (lastCleanedText == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "No uploaded PDF found. Please upload a PDF first."
                ));
            }

            String summary = llmService.generateSummaryWithOllama(lastCleanedText);
            return ResponseEntity.ok(Map.of("summary", summary));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error generating summary: " + e.getMessage()));
        }
    }

    @PostMapping("/check-answers")
    public ResponseEntity<?> checkAnswers(@RequestBody Map<String, String> payload) {
        try {
            String questions = payload.get("questions");
            String answers = payload.get("answers");

            if (questions == null || answers == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing questions or answers."));
            }

            String evaluation = llmService.checkAnswersWithOllama(questions, answers, lastCleanedText );
            return ResponseEntity.ok(Map.of("evaluation", evaluation));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error evaluating answers: " + e.getMessage()));
        }
    }
}
