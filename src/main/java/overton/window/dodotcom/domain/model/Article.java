package overton.window.dodotcom.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record Article(
        Long id,
        Long mediaId,
        Long storyId,
        String originalUrl,
        String title,
        String content,
        LocalDateTime publishedAt,
        float[] embedding,          // vector(768) — filled by external AI service
        String mainTopic,
        Map<String, Object> keywords,
        List<ArticleClassification> classifications,
        List<NamedEntity> entities
) {
    public static Article draft(Long mediaId, String originalUrl, String title,
                                String content, LocalDateTime publishedAt, String mainTopic) {
        return new Article(null, mediaId, null, originalUrl, title, content,
                publishedAt, null, mainTopic, null, List.of(), List.of());
    }
}
