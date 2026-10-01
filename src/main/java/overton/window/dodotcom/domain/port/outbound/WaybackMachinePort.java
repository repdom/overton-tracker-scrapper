package overton.window.dodotcom.domain.port.outbound;

import java.time.LocalDate;
import java.util.List;

/**
 * Outbound port: queries the Wayback Machine CDX API to discover archived URLs.
 */
public interface WaybackMachinePort {
    /** Discovers snapshots for a specific path prefix, e.g. "heritage.org/research/" */
    List<String> discoverSnapshots(String pathPrefix, LocalDate from, LocalDate to);
    /** Returns all configured path prefixes for a given media outlet id */
    List<String> getPathPrefixes(Long mediaId);
    String buildSnapshotUrl(String originalUrl, String timestamp);
}
