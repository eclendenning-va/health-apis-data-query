package gov.va.api.health.argonaut.api;

import gov.va.api.health.argonaut.api.resources.Conformance;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

public interface MetadataApi {
  @Operation(
    summary = "Conformance",
    description = "http://hl7.org/fhir/DSTU2/conformance.html",
    tags = "Metadata"
  )
  @GET
  @Path("metadata")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/json+fhir",
          schema = @Schema(implementation = Conformance.class)
        )
  )
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  Conformance metadata();
}
