package gov.va.api.health.argonaut.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import javax.ws.rs.Path;

@OpenAPIDefinition(
    info =
        @Info(
            title = "Argonaut",
            version = "v1",
            description =
                "FHIR Argonaut implementation. See http://www.fhir.org/guides/argonaut/r2/index.html"))
@Path("api")
public interface ArgonautService
    extends AllergyIntoleranceApi,
        ConditionApi,
        DiagnosticReportApi,
        ImmunizationApi,
        MedicationOrderApi,
        MedicationApi,
        MedicationStatementApi,
        MetadataApi,
        ObservationApi,
        PatientApi,
        ProcedureApi {

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
