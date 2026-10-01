package overton.window.dodotcom.infrastructure.adapter.nlp;

import jakarta.enterprise.context.ApplicationScoped;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import overton.window.dodotcom.domain.port.outbound.TextCleanerPort;

import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Uses OpenNLP sentence detector to remove boilerplate fragments
 * (short sentences, navigation remnants, cookie notices).
 */
@ApplicationScoped
public class OpenNlpTextCleanerAdapter implements TextCleanerPort {

    private static final int MIN_SENTENCE_WORDS = 6;
    private final SentenceDetectorME sentenceDetector;

    public OpenNlpTextCleanerAdapter() {
        try (InputStream modelIn = getClass().getResourceAsStream("/models/opennlp-en-ud-ewt-sentence-1.3-2.5.4.bin")) {
            if (modelIn == null) {
                // Graceful degradation: model not bundled yet, use regex-only cleaning
                this.sentenceDetector = null;
                return;
            }
            this.sentenceDetector = new SentenceDetectorME(new SentenceModel(modelIn));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load OpenNLP sentence model", e);
        }
    }

    @Override
    public String clean(String rawText) {
        if (rawText == null || rawText.isBlank()) return "";

        // Basic normalization
        String normalized = rawText
                .replaceAll("\\s+", " ")
                .replaceAll("(?i)(subscribe|sign up|newsletter|cookie policy|privacy policy)[^.]*\\.", "")
                .trim();

        if (sentenceDetector == null) return normalized;

        // Filter out boilerplate short sentences
        return Arrays.stream(sentenceDetector.sentDetect(normalized))
                .filter(s -> s.split("\\s+").length >= MIN_SENTENCE_WORDS)
                .collect(Collectors.joining(" "));
    }
}
