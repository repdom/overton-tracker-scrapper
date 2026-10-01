package overton.window.dodotcom.domain.port.outbound;

import overton.window.dodotcom.domain.model.ExtractedDocument;
import overton.window.dodotcom.domain.model.RawDocument;

/**
 * Outbound port: parses a RawDocument into structured text.
 * Implementations: TikaExtractorAdapter (PDF/binary), JsoupExtractorAdapter (HTML).
 */
public interface DocumentExtractorPort {
    boolean supports(RawDocument raw);
    ExtractedDocument extract(RawDocument raw);
}
