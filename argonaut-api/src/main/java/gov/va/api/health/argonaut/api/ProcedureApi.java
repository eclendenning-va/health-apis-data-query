package gov.va.api.health.argonaut.api;

import gov.va.api.health.argonaut.api.resources.Procedure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

public interface ProcedureApi {
  @Operation(
    summary = "Procedure Read",
    description = "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-procedure.html",
    tags = {"Procedure"}
  )
  @GET
  @Path("Procedure/{id}")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/json+fhir",
          schema = @Schema(implementation = Procedure.class)
        )
  )
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  Procedure procedureRead(
      @Parameter(in = ParameterIn.PATH, name = "id", required = true) String id);

  @Operation(
    summary = "Procedure Search",
    description = "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-procedure.html",
    tags = {"Procedure"}
  )
  @GET
  @Path("Procedure")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/json+fhir",
          schema = @Schema(implementation = Procedure.Bundle.class)
        )
  )
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  Procedure.Bundle procedureSearch(
      @Parameter(in = ParameterIn.QUERY, required = true, name = "patient") String id,
      @Parameter(in = ParameterIn.QUERY, name = "page") @DefaultValue("1") int page,
      @Parameter(in = ParameterIn.QUERY, name = "_count") @DefaultValue("15") int count);
}
