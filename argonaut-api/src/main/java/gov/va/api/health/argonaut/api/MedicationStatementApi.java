package gov.va.api.health.argonaut.api;

import gov.va.api.health.argonaut.api.resources.MedicationStatement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

public interface MedicationStatementApi {

  @Operation(
    summary = "Medication Statement read",
    description =
        "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-medicationstatement.html",
    tags = {"Medication Statement"}
  )
  @GET
  @Path("MedicationStatement/{id}")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/json+fhir",
          schema = @Schema(implementation = MedicationStatement.class)
        )
  )
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  MedicationStatement medicationStatementRead(
      @Parameter(in = ParameterIn.PATH, name = "id", required = true) String id);

  @Operation(
    summary = "Medication Statement search",
    description =
        "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-medicationstatement.html",
    tags = {"Medication Statement"}
  )
  @GET
  @Path("MedicationStatement")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/json+fhir",
          schema = @Schema(implementation = MedicationStatement.Bundle.class)
        )
  )
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  MedicationStatement.Bundle medicationStatementSearch(
      @Parameter(in = ParameterIn.QUERY, name = "patient") String id);
}
