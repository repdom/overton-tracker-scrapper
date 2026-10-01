package overton.window.dodotcom.domain.port.inbound;

public interface ScrapeAndIngestUseCase {
    /**
     * Fetches, extracts, cleans, pre-labels and persists an article from the given URL.
     * @param url  direct URL or Wayback Machine URL
     * @param mediaId  FK to media_outlets
     * @return persisted article id
     */
    Long execute(String url, Long mediaId);
}
