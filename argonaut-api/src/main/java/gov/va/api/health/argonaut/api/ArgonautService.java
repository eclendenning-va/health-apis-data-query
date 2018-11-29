package gov.va.api.health.argonaut.api;

import gov.va.api.health.argonaut.api.resources.AllergyIntolerance;
import gov.va.api.health.argonaut.api.resources.Condition;
import gov.va.api.health.argonaut.api.resources.Conformance;
import gov.va.api.health.argonaut.api.resources.DiagnosticReport;
import gov.va.api.health.argonaut.api.resources.Immunization;
import gov.va.api.health.argonaut.api.resources.Medication;
import gov.va.api.health.argonaut.api.resources.MedicationOrder;
import gov.va.api.health.argonaut.api.resources.MedicationStatement;
import gov.va.api.health.argonaut.api.resources.Observation;
import gov.va.api.health.argonaut.api.resources.Patient;
import gov.va.api.health.argonaut.api.resources.Procedure;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.ws.rs.GET;
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
    summary = "Condition Read",
    description = "https://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-condition.html"
  )
  @GET
  @Path("Condition/{id}")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/json+fhir",
          schema = @Schema(implementation = Condition.class)
        )
  )
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  Condition conditionRead(
      @Parameter(in = ParameterIn.PATH, name = "id", required = true) String id);

  @Operation(
    summary = "Condition Search",
    description = "https://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-condition.html"
  )
  @GET
  @Path("Condition")
  @ApiResponse(
    responseCode = "200",
    description = "Record Found",
    content =
        @Content(
          mediaType = "application/json+fhir",
          schema = @Schema(implementation = Condition.Bundle.class)
        )
  )
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  Condition.Bundle conditionSearch(
      @Parameter(in = ParameterIn.QUERY, name = "_id") String id,
      @Parameter(in = ParameterIn.QUERY, name = "category") String category,
      @Parameter(in = ParameterIn.QUERY, name = "clinicalStatus") String clinicalStatus);

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
    summary = "Immunization read",
    description =
        "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-immunization.html"
  )
  @GET
  @Path("Immunization/{id}")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/json+fhir",
          schema = @Schema(implementation = Immunization.class)
        )
  )
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  Immunization immunizationRead(
      @Parameter(in = ParameterIn.PATH, name = "id", required = true) String id);

  @Operation(
    summary = "Immunization search",
    description =
        "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-immunization.html"
  )
  @GET
  @Path("Immunization")
  @ApiResponse(
    responseCode = "200",
    description = "Record found",
    content =
        @Content(
          mediaType = "application/json+fhir",
          schema = @Schema(implementation = Immunization.Bundle.class)
        )
  )
  @ApiResponse(responseCode = "400", description = "Not found")
  @ApiResponse(responseCode = "404", description = "Bad request")
  Immunization.Bundle immunizationSearch(
      @Parameter(in = ParameterIn.QUERY, name = "patient") String id);

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
    summary = "Medication Order read",
    description =
        "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-medicationorder.html"
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
    summary = "MedicationOrder search",
    description =
        "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-medicationorder.html"
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

  // medication statement
  @Operation(
    summary = "Medication Statement read",
    description =
        "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-medicationstatement.html"
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
        "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-medicationstatement.html"
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

  // metadata
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
      @Parameter(in = ParameterIn.QUERY, name = "code") String code,
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

  // Procedure

  @Operation(
    summary = "Procedure read",
    description = "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-procedure.html"
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
    summary = "Procedure search",
    description = "http://www.fhir.org/guides/argonaut/r2/StructureDefinition-argo-procedure.html"
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
  Procedure.Bundle procedureSearch(@Parameter(in = ParameterIn.QUERY, name = "patient") String id);

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
