package overton.window.dodotcom.application.usecase;

import io.quarkus.arc.All;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import overton.window.dodotcom.domain.model.Article;
import overton.window.dodotcom.domain.model.ExtractedDocument;
import overton.window.dodotcom.domain.model.MediaOutlet;
import overton.window.dodotcom.domain.model.RawDocument;
import overton.window.dodotcom.domain.port.inbound.ScrapeAndIngestUseCase;
import overton.window.dodotcom.domain.port.outbound.*;

import java.util.List;

@ApplicationScoped
public class ScrapeAndIngestUseCaseImpl implements ScrapeAndIngestUseCase {

    private final List<ScraperPort> scrapers;
    private final List<DocumentExtractorPort> extractors;
    private final TextCleanerPort textCleaner;
    private final TopicLabellerPort topicLabeller;
    private final ArticleRepositoryPort articleRepository;
    private final MediaOutletRepositoryPort mediaOutletRepository;

    public ScrapeAndIngestUseCaseImpl(@All List<ScraperPort> scrapers,
                                      @All List<DocumentExtractorPort> extractors,
                                      TextCleanerPort textCleaner,
                                      TopicLabellerPort topicLabeller,
                                      ArticleRepositoryPort articleRepository,
                                      MediaOutletRepositoryPort mediaOutletRepository) {
        this.scrapers = scrapers;
        this.extractors = extractors;
        this.textCleaner = textCleaner;
        this.topicLabeller = topicLabeller;
        this.articleRepository = articleRepository;
        this.mediaOutletRepository = mediaOutletRepository;
    }

    @Override
    @Transactional
    public Long execute(String url, Long mediaId) {
        if (articleRepository.existsByUrl(url)) {
            return null; // idempotent — already ingested
        }

        MediaOutlet outlet = mediaOutletRepository.findById(mediaId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown mediaId: " + mediaId));

        // 1. Fetch raw content — pick first scraper that supports the URL
        RawDocument raw = scrapers.stream()
                .filter(s -> s.supports(url, null))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No scraper for: " + url))
                .fetch(url);

        // 2. Extract structured text — pick extractor by mime type
        ExtractedDocument extracted = extractors.stream()
                .filter(e -> e.supports(raw))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No extractor for mime: " + raw.mimeType()))
                .extract(raw);

        // 3. Clean boilerplate via OpenNLP
        String cleanText = textCleaner.clean(extracted.rawText());

        // 4. Assign preliminary topic label from metadata
        String ideologyHint = outlet.baseIdeologyScore() != null
                ? (outlet.baseIdeologyScore().doubleValue() > 0 ? "right" : "left")
                : "center";
        String topic = topicLabeller.assignTopic(extracted.title(), cleanText, ideologyHint);

        // 5. Persist draft article — embedding/NER/stance assigned later by AI service
        Article draft = Article.draft(mediaId, url, extracted.title(), cleanText,
                extracted.publishedAt(), topic);
        Article saved = articleRepository.save(draft);

        return saved.id();
    }
}
