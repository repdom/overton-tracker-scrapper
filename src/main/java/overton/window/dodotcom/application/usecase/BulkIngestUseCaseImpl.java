package overton.window.dodotcom.application.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import overton.window.dodotcom.domain.port.inbound.BulkIngestUseCase;
import overton.window.dodotcom.domain.port.outbound.MediaOutletRepositoryPort;
import overton.window.dodotcom.domain.port.outbound.WaybackMachinePort;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class BulkIngestUseCaseImpl implements BulkIngestUseCase {

    private final WaybackMachinePort wayback;
    private final MediaOutletRepositoryPort mediaOutletRepository;
    private final ScrapeAndIngestUseCaseImpl ingest;

    public BulkIngestUseCaseImpl(WaybackMachinePort wayback,
                                 MediaOutletRepositoryPort mediaOutletRepository,
                                 ScrapeAndIngestUseCaseImpl ingest) {
        this.wayback = wayback;
        this.mediaOutletRepository = mediaOutletRepository;
        this.ingest = ingest;
    }

    @Override
    public List<Long> executeForOutlet(Long mediaId, LocalDate from, LocalDate to) {
        mediaOutletRepository.findById(mediaId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown mediaId: " + mediaId));

        List<String> prefixes = wayback.getPathPrefixes(mediaId);
        if (prefixes.isEmpty()) {
            throw new IllegalStateException("No configured path prefixes for mediaId: " + mediaId
                    + ". Add rows to media_outlet_paths table.");
        }

        return prefixes.stream()
                .flatMap(prefix -> wayback.discoverSnapshots(prefix, from, to).stream())
                .map(snapshotUrl -> ingest.execute(snapshotUrl, mediaId))
                .filter(id -> id != null)
                .toList();
    }
}
