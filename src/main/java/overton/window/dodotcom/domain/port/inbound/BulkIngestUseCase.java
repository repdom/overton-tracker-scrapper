package overton.window.dodotcom.domain.port.inbound;

import java.time.LocalDate;
import java.util.List;

public interface BulkIngestUseCase {
    /**
     * Discovers and ingests all archived snapshots for a media outlet within a date range.
     */
    List<Long> executeForOutlet(Long mediaId, LocalDate from, LocalDate to);
}
