package gov.va.api.health.argonaut.api;

import gov.va.api.health.argonaut.api.resources.DiagnosticReport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

public interface DiagnosticReportApi {
  @Operation(
    summary = "Diagnostic Report Read",
    description =
        "https://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-diagnosticreport.html",
    tags = {"Diagnostic Report"}
  )
  @GET
  @Path("DiagnosticReport/{id}")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/json+fhir",
          schema = @Schema(implementation = DiagnosticReport.class)
        )
  )
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  DiagnosticReport diagnosticReportRead(
      @Parameter(in = ParameterIn.PATH, name = "id", required = true) String id);

  @Operation(
    summary = "Diagnostic Report Search",
    description =
        "https://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-diagnosticreport.html",
    tags = {"Diagnostic Report"}
  )
  @GET
  @Path("DiagnosticReport")
  @ApiResponse(
    responseCode = "200",
    description = "Record Found",
    content =
        @Content(
          mediaType = "application/json+fhir",
          schema = @Schema(implementation = DiagnosticReport.Bundle.class)
        )
  )
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  DiagnosticReport.Bundle diagnosticReportSearch(
      @Parameter(in = ParameterIn.QUERY, name = "_id") String id,
      @Parameter(in = ParameterIn.QUERY, name = "identifier") String identifier,
      @Parameter(in = ParameterIn.QUERY, name = "category") String category,
      @Parameter(in = ParameterIn.QUERY, name = "code") String[] code,
      @Parameter(in = ParameterIn.QUERY, name = "date") String[] date);
}
