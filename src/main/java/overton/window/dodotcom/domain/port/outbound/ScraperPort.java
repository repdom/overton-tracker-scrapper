package overton.window.dodotcom.domain.port.outbound;

import overton.window.dodotcom.domain.model.RawDocument;

/**
 * Outbound port: fetches raw content from a URL.
 * Implementations: PlaywrightScraperAdapter (JS-heavy), JsoupScraperAdapter (static HTML).
 */
public interface ScraperPort {
    boolean supports(String url, String mimeType);
    RawDocument fetch(String url);
}
