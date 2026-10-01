package overton.window.dodotcom.infrastructure.adapter.scraper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import overton.window.dodotcom.domain.port.outbound.WaybackMachinePort;
import overton.window.dodotcom.infrastructure.adapter.persistence.MediaOutletPathEntity;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class WaybackMachineAdapter implements WaybackMachinePort {

    private static final Logger LOG = Logger.getLogger(WaybackMachineAdapter.class);
    private static final String CDX_API = "https://web.archive.org/cdx/search/cdx";
    private static final DateTimeFormatter CDX_DATE = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String USER_AGENT = "Mozilla/5.0 (compatible; OvertoneBot/1.0)";

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<String> getPathPrefixes(Long mediaId) {
        return MediaOutletPathEntity.findActiveByMediaId(mediaId)
                .stream()
                .map(e -> e.pathPrefix)
                .toList();
    }

    @Override
    public List<String> discoverSnapshots(String pathPrefix, LocalDate from, LocalDate to) {
        // Use specific path prefix (e.g. "heritage.org/research/") — never full domain/*
        String url = CDX_API
                + "?url=" + pathPrefix + "*"
                + "&output=json"
                + "&fl=timestamp,original"
                + "&filter=statuscode:200"
                + "&collapse=urlkey"
                + "&from=" + from.format(CDX_DATE)
                + "&to=" + to.format(CDX_DATE)
                + "&limit=50";

        LOG.infof("CDX query: %s", url);
        try {
            HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                    .GET()
                    .timeout(Duration.ofSeconds(30))
                    .header("User-Agent", USER_AGENT)
                    .build();

            HttpResponse<String> response = http.send(req, HttpResponse.BodyHandlers.ofString());
            LOG.infof("CDX status=%d body_length=%d for prefix=%s", response.statusCode(), response.body().length(), pathPrefix);

            if (response.statusCode() == 403) {
                LOG.warnf("CDX 403 Forbidden for prefix=%s — skipping", pathPrefix);
                return List.of();
            }
            if (response.statusCode() == 504 || response.body().contains("Gateway Time-out")) {
                LOG.warnf("CDX 504 for prefix=%s — skipping", pathPrefix);
                return List.of();
            }
            if (response.statusCode() != 200) {
                LOG.warnf("CDX HTTP %d for prefix=%s — skipping", response.statusCode(), pathPrefix);
                return List.of();
            }

            String body = response.body().trim();
            if (body.isEmpty() || body.equals("[]")) return List.of();

            JsonNode rows = mapper.readTree(body);
            if (rows.size() <= 1) return List.of(); // only header row

            List<String> snapshots = new ArrayList<>();
            for (int i = 1; i < rows.size(); i++) {
                String timestamp = rows.get(i).get(0).asText();
                String original  = rows.get(i).get(1).asText();
                snapshots.add(buildSnapshotUrl(original, timestamp));
            }
            LOG.infof("CDX found %d snapshots for prefix=%s", snapshots.size(), pathPrefix);
            return snapshots;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("CDX query failed for prefix: " + pathPrefix, e);
        }
    }

    @Override
    public String buildSnapshotUrl(String originalUrl, String timestamp) {
        return "https://web.archive.org/web/" + timestamp + "/" + originalUrl;
    }
}

/**
 * Queries the Wayback Machine CDX API to discover archived snapshots.
 * CDX API docs: https://github.com/internetarchive/wayback/tree/master/wayback-cdx-server
 */
@ApplicationScoped
public class WaybackMachineAdapter implements WaybackMachinePort {

    private static final String CDX_API = "https://web.archive.org/cdx/search/cdx";
    private static final DateTimeFormatter CDX_DATE = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String USER_AGENT = "Mozilla/5.0 (compatible; OvertoneBot/1.0; +https://github.com/overton-tracker)";
    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();
    private final ObjectMapper mapper = new ObjectMapper();

    private static final Logger LOG = Logger.getLogger(WaybackMachineAdapter.class);

    @Override
    public List<String> discoverSnapshots(String domain, LocalDate from, LocalDate to) {
        // Wildcard (domain/*) causes CDX to scan millions of records -> 504
        // Strategy: query only the root URL, get 1 snapshot per day via collapse=timestamp:8
        // This is fast (<1s) and gives representative daily snapshots of the outlet
        String url = CDX_API
                + "?url=" + domain
                + "&output=json"
                + "&fl=timestamp,original"
                + "&filter=statuscode:200"
                + "&collapse=timestamp:8"
                + "&from=" + from.format(CDX_DATE)
                + "&to=" + to.format(CDX_DATE)
                + "&limit=50";

        LOG.infof("CDX query: %s", url);

        try {
            HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                    .GET()
                    .timeout(Duration.ofSeconds(30))
                    .header("User-Agent", USER_AGENT)
                    .build();

            HttpResponse<String> response = http.send(req, HttpResponse.BodyHandlers.ofString());
            LOG.infof("CDX status=%d body_length=%d", response.statusCode(), response.body().length());
            LOG.infof("CDX body preview: %.500s", response.body());

            if (response.statusCode() == 504 || response.body().contains("Gateway Time-out")) {
                throw new RuntimeException("CDX API 504 for domain: " + domain);
            }
            if (response.statusCode() != 200) {
                throw new RuntimeException("CDX API HTTP " + response.statusCode() + " for domain: " + domain);
            }

            String body = response.body().trim();
            if (body.isEmpty() || body.equals("[]")) {
                LOG.warnf("CDX no results for domain=%s from=%s to=%s", domain, from, to);
                return List.of();
            }

            JsonNode rows = mapper.readTree(body);
            if (rows.size() <= 1) {
                LOG.warnf("CDX only header row for domain=%s", domain);
                return List.of();
            }

            List<String> snapshots = new ArrayList<>();
            for (int i = 1; i < rows.size(); i++) {
                String timestamp = rows.get(i).get(0).asText();
                String original  = rows.get(i).get(1).asText();
                snapshots.add(buildSnapshotUrl(original, timestamp));
            }
            LOG.infof("CDX found %d snapshots for domain=%s", snapshots.size(), domain);
            return snapshots;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Wayback CDX query failed for domain: " + domain, e);
        }
    }

    @Override
    public String buildSnapshotUrl(String originalUrl, String timestamp) {
        return "https://web.archive.org/web/" + timestamp + "/" + originalUrl;
    }
}
