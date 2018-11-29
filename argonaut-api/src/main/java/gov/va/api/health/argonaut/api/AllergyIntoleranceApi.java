package gov.va.api.health.argonaut.api;

import gov.va.api.health.argonaut.api.resources.AllergyIntolerance;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

public interface AllergyIntoleranceApi {
  @Operation(
    summary = "Allergy Intolerance read",
    description =
        "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-allergyintolerance.html",
    tags = {"Allergy Intolerance"}
  )
  @GET
  @Path("AllergyIntolerance/{id}")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/json+fhir",
          schema = @Schema(implementation = AllergyIntolerance.class)
        )
  )
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  AllergyIntolerance allergyIntoleranceRead(
      @Parameter(in = ParameterIn.PATH, name = "id", required = true) String id);

  @Operation(
    summary = "Allergy Intolerance search",
    description =
        "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-allergyintolerance.html",
    tags = {"Allergy Intolerance"}
  )
  @GET
  @Path("AllergyIntolerance")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/json+fhir",
          schema = @Schema(implementation = AllergyIntolerance.Bundle.class)
        )
  )
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  AllergyIntolerance.Bundle allergyIntoleranceSearch(
      @Parameter(in = ParameterIn.QUERY, name = "patient") String id);
}
