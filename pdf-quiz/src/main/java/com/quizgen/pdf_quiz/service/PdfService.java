package com.quizgen.pdf_quiz.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class PdfService {

    public String extractText(MultipartFile file) throws Exception {
        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {

            PDFTextStripper stripper = new PDFTextStripper();
            String rawText = stripper.getText(document);
            return cleanText(rawText);
        }
    }

    // 🚿 Clean up extracted text
    private String cleanText(String rawText) {
        return rawText
                .replaceAll("-\\n", "")                // fix hyphen line breaks
                .replaceAll("\\r?\\n", " ")            // newlines to spaces
                .replaceAll("\\s{2,}", " ")            // extra spaces
                .replaceAll("Page \\d+", "")           // remove page numbers
                .replaceAll("\\.\\.+", ".")            // replace multiple dots
                .replaceAll("\\s+([.,?!;])", "$1")     // remove space before punctuation
                .trim();
    }


}
