package overton.window.dodotcom.domain.model;

import java.time.LocalDateTime;

/**
 * Structured text extracted from a RawDocument, ready for NLP cleaning.
 */
public record ExtractedDocument(
        String sourceUrl,
        String title,
        String rawText,
        LocalDateTime publishedAt,
        String mimeType
) {}
