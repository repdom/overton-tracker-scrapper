package overton.window.dodotcom.domain.model;

import java.time.LocalDateTime;

public record Story(
        Long id,
        String title,
        LocalDateTime firstSeenAt
) {}
