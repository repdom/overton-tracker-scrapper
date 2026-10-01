package overton.window.dodotcom.infrastructure.adapter.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import overton.window.dodotcom.domain.port.inbound.BulkIngestUseCase;
import overton.window.dodotcom.domain.port.inbound.ScrapeAndIngestUseCase;

import java.time.LocalDate;
import java.util.List;

@Path("/ingest")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IngestResource {

    private final ScrapeAndIngestUseCase scrapeAndIngest;
    private final BulkIngestUseCase bulkIngest;

    public IngestResource(ScrapeAndIngestUseCase scrapeAndIngest, BulkIngestUseCase bulkIngest) {
        this.scrapeAndIngest = scrapeAndIngest;
        this.bulkIngest = bulkIngest;
    }

    @POST
    @Path("/article")
    public Response ingestSingle(@QueryParam("url") String url,
                                 @QueryParam("mediaId") Long mediaId) {
        Long id = scrapeAndIngest.execute(url, mediaId);
        if (id == null) return Response.status(Response.Status.CONFLICT)
                .entity("{\"message\":\"already ingested\"}").build();
        return Response.ok("{\"articleId\":" + id + "}").build();
    }

    @POST
    @Path("/bulk/{mediaId}")
    public Response ingestBulk(@PathParam("mediaId") Long mediaId,
                               @QueryParam("from") String from,
                               @QueryParam("to") String to) {
        List<Long> ids = bulkIngest.executeForOutlet(mediaId,
                LocalDate.parse(from), LocalDate.parse(to));
        return Response.ok("{\"ingested\":" + ids.size() + "}").build();
    }
}
