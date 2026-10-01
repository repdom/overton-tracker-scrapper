package overton.window.dodotcom.infrastructure.adapter.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "articles")
public class ArticleEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "media_id")
    public Long mediaId;

    @Column(name = "story_id")
    public Long storyId;

    @Column(name = "original_url", unique = true, nullable = false, columnDefinition = "TEXT")
    public String originalUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    public String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    public String content;

    @Column(name = "published_at", nullable = false)
    public LocalDateTime publishedAt;

    // vector(768) — populated by external AI service
    @Column(name = "embedding", columnDefinition = "vector(768)")
    @JdbcTypeCode(SqlTypes.VECTOR)
    public float[] embedding;

    @Column(name = "main_topic", length = 100)
    public String mainTopic;

    @Column(name = "keywords", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    public String keywords;
}
