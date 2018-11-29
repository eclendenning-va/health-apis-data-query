package gov.va.api.health.argonaut.api;

import gov.va.api.health.argonaut.api.resources.MedicationOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

public interface MedicationOrderApi {

  @Operation(
    summary = "Medication Order Read",
    description =
        "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-medicationorder.html",
    tags = {"Medication Order"}
  )
  @GET
  @Path("MedicationOrder/{id}")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/json+fhir",
          schema = @Schema(implementation = MedicationOrder.class)
        )
  )
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  MedicationOrder medicationOrderRead(
      @Parameter(in = ParameterIn.PATH, name = "id", required = true) String id);

  @Operation(
    summary = "MedicationOrder Search",
    description =
        "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-medicationorder.html",
    tags = {"Medication Order"}
  )
  @GET
  @Path("MedicationOrder")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/json+fhir",
          schema = @Schema(implementation = MedicationOrder.Bundle.class)
        )
  )
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  MedicationOrder.Bundle medicationOrderSearch(
      @Parameter(in = ParameterIn.QUERY, name = "patient") String id);
}
