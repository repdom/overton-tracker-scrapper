package overton.window.dodotcom.domain.model;

/**
 * Represents a raw document fetched from a URL before full parsing.
 */
public record RawDocument(
        String sourceUrl,
        String finalUrl,
        byte[] rawBytes,
        String mimeType,
        String htmlContent   // null for binary (PDF)
) {}
