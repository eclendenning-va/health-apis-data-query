package gov.va.api.health.argonaut.api;

import gov.va.api.health.argonaut.api.resources.Medication;
import gov.va.api.health.argonaut.api.resources.Observation;
import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import gov.va.api.health.argonaut.api.resources.Patient;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@OpenAPIDefinition(
  info =
      @Info(
        title = "Argonaut",
        version = "v1",
        description =
            "FHIR Argonaut implementation. See http://www.fhir.org/guides/argonaut/r2/index.html"
      )
)
@Path("api")
public interface ArgonautService {

  @Operation(
    summary = "Medication read",
    description = "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-medication.html"
  )
  @GET
  @Path("Medication/{id}")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/fhir+json",
          schema = @Schema(implementation = Medication.class)
        )
  )
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  Medication medicationRead(
      @Parameter(in = ParameterIn.PATH, name = "id", required = true) String id);

  @Operation(
    summary = "Observation read",
    description =
        "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-observationresults.html"
  )
  @GET
  @Path("Observation/{id}")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/fhir+json",
          schema = @Schema(implementation = Observation.class)
        )
  )
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  Observation observationRead(
      @Parameter(in = ParameterIn.PATH, name = "id", required = true) String id);

  @Operation(
    summary = "Observation validate",
    description =
        "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-observationresults.html"
  )
  @POST
  @Consumes("application/fhir+json")
  @Path("Observation/$validate")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/fhir+json",
          schema = @Schema(implementation = OperationOutcome.class)
        )
  )
  @ApiResponse(responseCode = "404", description = "Bad request")
  Observation observationRead(@RequestBody(required = true) Observation.Bundle bundle);

  @Operation(
    summary = "Observation search",
    description =
        "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-observationresults.html"
  )
  @GET
  @Path("Observation")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/fhir+json",
          schema = @Schema(implementation = Observation.Bundle.class)
        )
  )
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  Observation.Bundle observationSearch(
      @Parameter(in = ParameterIn.QUERY, name = "_id") String id,
      @Parameter(in = ParameterIn.QUERY, name = "category") String category,
      @Parameter(in = ParameterIn.QUERY, name = "code") String[] code,
      @Parameter(in = ParameterIn.QUERY, name = "date") String[] date);

  @Operation(
    summary = "Patient read",
    description = "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-patient.html"
  )
  @GET
  @Path("Patient/{id}")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/fhir+json",
          schema = @Schema(implementation = Patient.class)
        )
  )
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  Patient patientRead(@Parameter(in = ParameterIn.PATH, name = "id", required = true) String id);

  @Operation(
    summary = "Patient validate",
    description = "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-patient.html"
  )
  @POST
  @Consumes("application/fhir+json")
  @Path("Patient/$validate")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/fhir+json",
          schema = @Schema(implementation = OperationOutcome.class)
        )
  )
  @ApiResponse(responseCode = "404", description = "Bad request")
  Patient patientRead(@RequestBody(required = true) Patient.Bundle bundle);

  @Operation(
    summary = "Patient search",
    description = "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-patient.html"
  )
  @GET
  @Path("Patient")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/fhir+json",
          schema = @Schema(implementation = Patient.Bundle.class)
        )
  )
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  Patient.Bundle patientSearch(
      @Parameter(in = ParameterIn.QUERY, name = "_id") String id,
      @Parameter(in = ParameterIn.QUERY, name = "identifier") String identifier,
      @Parameter(in = ParameterIn.QUERY, name = "name") String name,
      @Parameter(in = ParameterIn.QUERY, name = "given") String given,
      @Parameter(in = ParameterIn.QUERY, name = "family") String family,
      @Parameter(in = ParameterIn.QUERY, name = "gender") String gender,
      @Parameter(in = ParameterIn.QUERY, name = "birthdate") String[] birthdate);

  class ArgonautServiceException extends RuntimeException {
    ArgonautServiceException(String message) {
      super(message);
    }
  }

  class SearchFailed extends ArgonautServiceException {
    @SuppressWarnings("WeakerAccess")
    public SearchFailed(String id, String reason) {
      super(id + " Reason: " + reason);
    }
  }

  class UnknownResource extends ArgonautServiceException {
    @SuppressWarnings("WeakerAccess")
    public UnknownResource(String id) {
      super(id);
    }
  }
}
