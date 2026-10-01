package overton.window.dodotcom.infrastructure.adapter.extractor;

import jakarta.enterprise.context.ApplicationScoped;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import overton.window.dodotcom.domain.model.ExtractedDocument;
import overton.window.dodotcom.domain.model.RawDocument;
import overton.window.dodotcom.domain.port.outbound.DocumentExtractorPort;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Extracts article text from HTML using Jsoup — strips nav, footer, ads.
 */
@ApplicationScoped
public class JsoupExtractorAdapter implements DocumentExtractorPort {

    @Override
    public boolean supports(RawDocument raw) {
        return raw.mimeType() != null && raw.mimeType().startsWith("text/html");
    }

    @Override
    public ExtractedDocument extract(RawDocument raw) {
        Document doc = Jsoup.parse(raw.htmlContent(), raw.finalUrl());

        // Remove boilerplate structural elements
        doc.select("nav, footer, header, aside, script, style, .ad, .advertisement, .cookie-banner").remove();

        String title = doc.title();
        String text = doc.select("article, main, .article-body, .post-content, .entry-content")
                .stream().findFirst()
                .map(el -> el.text())
                .orElse(doc.body().text());

        LocalDateTime publishedAt = extractPublishedAt(doc);
        return new ExtractedDocument(raw.sourceUrl(), title, text, publishedAt, raw.mimeType());
    }

    private LocalDateTime extractPublishedAt(Document doc) {
        String dateStr = doc.select("time[datetime]").attr("datetime");
        if (dateStr.isBlank()) dateStr = doc.select("meta[property=article:published_time]").attr("content");
        if (dateStr.isBlank()) return LocalDateTime.now();
        try {
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
}
