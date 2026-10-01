package overton.window.dodotcom.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ArticleClassification(
        Long articleId,
        String dimension,
        BigDecimal confidenceScore,
        LocalDateTime createdAt
) {}
