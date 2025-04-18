package com.quizgen.pdf_quiz.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

@Service
public class LLMService {

    private String sendPrompt(String prompt) throws IOException {
        URL url = new URL("http://localhost:11434/api/generate");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(1800000); // 1.5 minutes
        connection.setReadTimeout(1800000);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonInput = "{"
                + "\"model\": \"llama3\","
                + "\"prompt\": \"" + prompt.replace("\"", "\\\"").replace("\n", "\\n") + "\","
                + "\"stream\": false"
                + "}";

        try (OutputStream os = connection.getOutputStream()) {
            os.write(jsonInput.getBytes("utf-8"));
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }
        }

        String result = response.toString();
        int contentStart = result.indexOf("\"response\":\"") + 12;
        int contentEnd = result.indexOf("\",\"done\"");

        if (contentStart == -1 || contentEnd == -1 || contentEnd <= contentStart) {
            return "⚠️ Could not extract content from response:\n" + result;
        }

        return result.substring(contentStart, contentEnd).replace("\\n", "\n");
    }

    public String generateQuestionsWithOllama(String text, int numQuestions) throws IOException {
        String prompt = String.format(
                "Generate %d random single-line quiz questions from the text below. Don't include options or answers. Respond fast (within 1 minute).\n\n%s",
                numQuestions, text);
        return sendPrompt(prompt);
    }

    public String generateSummaryWithOllama(String text) throws IOException {
        String prompt = "Summarize the following text clearly and concisely. Keep the response short in 100 word and complete it within 50 second:\n\n" + text;
        return sendPrompt(prompt);
    }

    public String checkAnswersWithOllama(String questions, String answers, String text) throws IOException {
        String prompt = String.format(
                "You are an evaluator. Match each of the user's answers to the corresponding question based on the text, Score it out of 5 and give a simple evaluation and right answer in few worda. Respond in less than 60 seconds.\n\nQuestions:\n%s\n\nAnswers:\n%s\n\ntext:\n%s                                    ",
                questions, answers, text);
        return sendPrompt(prompt);
    }
}
