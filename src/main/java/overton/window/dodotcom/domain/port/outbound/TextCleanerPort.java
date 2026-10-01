package overton.window.dodotcom.domain.port.outbound;

/**
 * Outbound port: cleans boilerplate from raw text using NLP.
 * Implementation: OpenNlpTextCleanerAdapter.
 */
public interface TextCleanerPort {
    String clean(String rawText);
}
