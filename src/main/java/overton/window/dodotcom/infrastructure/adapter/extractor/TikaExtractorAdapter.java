package overton.window.dodotcom.infrastructure.adapter.extractor;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import overton.window.dodotcom.domain.model.ExtractedDocument;
import overton.window.dodotcom.domain.model.RawDocument;
import overton.window.dodotcom.domain.port.outbound.DocumentExtractorPort;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Handles PDFs and any binary format via Apache Tika AutoDetectParser.
 */
@ApplicationScoped
public class TikaExtractorAdapter implements DocumentExtractorPort {

    @Override
    public boolean supports(RawDocument raw) {
        return raw.mimeType() != null && !raw.mimeType().startsWith("text/html");
    }

    @Override
    public ExtractedDocument extract(RawDocument raw) {
        try {
            var handler = new BodyContentHandler(-1);
            var metadata = new Metadata();
            new AutoDetectParser().parse(
                    new ByteArrayInputStream(raw.rawBytes()), handler, metadata);

            String title = metadata.get("dc:title") != null
                    ? metadata.get("dc:title") : raw.sourceUrl();
            LocalDateTime publishedAt = parseDate(metadata.get("dcterms:created"));

            return new ExtractedDocument(raw.sourceUrl(), title,
                    handler.toString(), publishedAt, raw.mimeType());
        } catch (Exception e) {
            throw new RuntimeException("Tika extraction failed for: " + raw.sourceUrl(), e);
        }
    }

    private LocalDateTime parseDate(String raw) {
        if (raw == null) return LocalDateTime.now();
        try {
            return LocalDateTime.parse(raw, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
}
