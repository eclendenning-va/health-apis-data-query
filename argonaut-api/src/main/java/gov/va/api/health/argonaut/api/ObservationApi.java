package gov.va.api.health.argonaut.api;

import gov.va.api.health.argonaut.api.resources.Observation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

public interface ObservationApi {
  @Operation(
      summary = "Observation Read",
      description =
          "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-observationresults.html",
      tags = {"Observation"})
  @GET
  @Path("Observation/{id}")
  @ApiResponse(
      responseCode = "200",
      description = "Record found",
      content =
          @Content(
              mediaType = "application/fhir+json",
              schema = @Schema(implementation = Observation.class)))
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  Observation observationRead(
      @Parameter(in = ParameterIn.PATH, name = "id", required = true) String id);

  @Operation(
      summary = "Observation Search",
      description =
          "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-observationresults.html",
      tags = {"Observation"})
  @GET
  @Path("Observation")
  @ApiResponse(
      responseCode = "200",
      description = "Record found",
      content =
          @Content(
              mediaType = "application/fhir+json",
              schema = @Schema(implementation = Observation.Bundle.class)))
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  Observation.Bundle observationSearch(
      @Parameter(in = ParameterIn.QUERY, name = "_id") String id,
      @Parameter(in = ParameterIn.QUERY, name = "category") String category,
      @Parameter(in = ParameterIn.QUERY, name = "code") String code,
      @Parameter(in = ParameterIn.QUERY, name = "date") String[] date);
}
