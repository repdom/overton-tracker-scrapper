package overton.window.dodotcom.infrastructure.adapter.scraper;

import jakarta.enterprise.context.ApplicationScoped;
import org.jsoup.Jsoup;
import overton.window.dodotcom.domain.model.RawDocument;
import overton.window.dodotcom.domain.port.outbound.ScraperPort;

import java.io.IOException;

/**
 * Default scraper for static HTML pages (think tanks, Wayback Machine snapshots).
 */
@ApplicationScoped
public class JsoupScraperAdapter implements ScraperPort {

    @Override
    public boolean supports(String url, String mimeType) {
        return true; // default fallback
    }

    @Override
    public RawDocument fetch(String url) {
        try {
            var connection = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (compatible; OvertoneBot/1.0)")
                    .timeout(20_000)
                    .followRedirects(true);
            var response = connection.execute();
            String html = response.body();
            return new RawDocument(url, response.url().toString(),
                    html.getBytes(), response.contentType(), html);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch: " + url, e);
        }
    }
}
