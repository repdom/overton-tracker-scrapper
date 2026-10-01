package overton.window.dodotcom.domain.model;

import java.math.BigDecimal;

public record MediaOutlet(
        Long id,
        String name,
        String domain,
        SourceType sourceType,
        BigDecimal baseIdeologyScore, // -1.0 (Left) to 1.0 (Right)
        String countryCode
) {
    public enum SourceType { news, think_tank, blog }
}
