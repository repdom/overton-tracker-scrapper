package overton.window.dodotcom.domain.port.outbound;

import overton.window.dodotcom.domain.model.Article;

import java.util.Optional;

public interface ArticleRepositoryPort {
    Article save(Article article);
    boolean existsByUrl(String url);
    Optional<Article> findById(Long id);
}
