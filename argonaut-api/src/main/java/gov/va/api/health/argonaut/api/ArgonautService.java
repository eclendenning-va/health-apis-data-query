package gov.va.api.health.argonaut.api;

import gov.va.api.health.argonaut.api.resources.AllergyIntolerance;
import gov.va.api.health.argonaut.api.resources.Conformance;
import gov.va.api.health.argonaut.api.resources.DiagnosticReport;
import gov.va.api.health.argonaut.api.resources.Medication;
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
    summary = "Diagnostic Report Read",
    description =
        "https://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-diagnosticreport.html"
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
        "https://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-diagnosticreport.html"
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

  @Operation(
    summary = "Diagnostic Report validate",
    description =
        "https://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-diagnosticreport.html"
  )
  @POST
  @Consumes("application/json+fhir")
  @Path("DiagnosticReport/$validate")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/json+fhir",
          schema = @Schema(implementation = OperationOutcome.class)
        )
  )
  @ApiResponse(responseCode = "404", description = "Bad request")
  OperationOutcome diagnosticReportValidate(
      @RequestBody(required = true) DiagnosticReport.Bundle bundle);

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
          mediaType = "application/json+fhir",
          schema = @Schema(implementation = Medication.class)
        )
  )
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  Medication medicationRead(
      @Parameter(in = ParameterIn.PATH, name = "id", required = true) String id);

  @Operation(
    summary = "Medication validate",
    description = "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-medication.html"
  )
  @POST
  @Consumes("application/json+fhir")
  @Path("Medication/$validate")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/json+fhir",
          schema = @Schema(implementation = OperationOutcome.class)
        )
  )
  @ApiResponse(responseCode = "404", description = "Bad request")
  OperationOutcome medicationValidate(@RequestBody(required = true) Medication.Bundle bundle);

  @Operation(summary = "Conformance", description = "http://hl7.org/fhir/DSTU2/conformance.html")
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
          mediaType = "application/json+fhir",
          schema = @Schema(implementation = Patient.class)
        )
  )
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  Patient patientRead(@Parameter(in = ParameterIn.PATH, name = "id", required = true) String id);

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
          mediaType = "application/json+fhir",
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

  @Operation(
    summary = "Patient validate",
    description = "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-patient.html"
  )
  @POST
  @Consumes("application/json+fhir")
  @Path("Patient/$validate")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/json+fhir",
          schema = @Schema(implementation = OperationOutcome.class)
        )
  )
  @ApiResponse(responseCode = "404", description = "Bad request")
  OperationOutcome patientValidate(@RequestBody(required = true) Patient.Bundle bundle);

  @Operation(
    summary = "Allergy Intolerance read",
    description =
        "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-allergyintolerance.html"
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
        "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-allergyintolerance.html"
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

  @Operation(
    summary = "Allergy Intolerance validate",
    description =
        "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-allergyintolerance.html"
  )
  @POST
  @Consumes("application/json+fhir")
  @Path("AllergyIntolerance/$validate")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/json+fhir",
          schema = @Schema(implementation = OperationOutcome.class)
        )
  )
  @ApiResponse(responseCode = "404", description = "Bad request")
  OperationOutcome allergyIntoleranceValidate(
      @RequestBody(required = true) AllergyIntolerance.Bundle bundle);

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
