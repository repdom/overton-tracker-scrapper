package overton.window.dodotcom.infrastructure.adapter.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import overton.window.dodotcom.domain.model.Article;
import overton.window.dodotcom.domain.port.outbound.ArticleRepositoryPort;

import java.util.Optional;

@ApplicationScoped
public class ArticleRepositoryAdapter implements ArticleRepositoryPort {

    @Override
    public Article save(Article article) {
        ArticleEntity entity = toEntity(article);
        entity.persist();
        return toDomain(entity);
    }

    @Override
    public boolean existsByUrl(String url) {
        return ArticleEntity.count("originalUrl", url) > 0;
    }

    @Override
    public Optional<Article> findById(Long id) {
        return ArticleEntity.<ArticleEntity>findByIdOptional(id).map(this::toDomain);
    }

    private ArticleEntity toEntity(Article a) {
        var e = new ArticleEntity();
        e.id = a.id();
        e.mediaId = a.mediaId();
        e.storyId = a.storyId();
        e.originalUrl = a.originalUrl();
        e.title = a.title();
        e.content = a.content();
        e.publishedAt = a.publishedAt();
        e.embedding = a.embedding();
        e.mainTopic = a.mainTopic();
        return e;
    }

    private Article toDomain(ArticleEntity e) {
        return new Article(e.id, e.mediaId, e.storyId, e.originalUrl, e.title,
                e.content, e.publishedAt, e.embedding, e.mainTopic, null,
                java.util.List.of(), java.util.List.of());
    }
}
