package gov.va.health.api.sentinel;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import java.util.function.Supplier;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Value
@Builder
@Slf4j
public class FhirTestClient implements TestClient {
  private final ServiceDefinition service;
  Supplier<ObjectMapper> mapper;

  /**
   * Remove data from the OO that is unique for each instance. This includes the generated ID and
   * the timestamp.
   */
  private OperationOutcome asOperationOutcomeWithoutDiagnostics(ResponseBody<?> body) {
    try {
      OperationOutcome oo = body.as(OperationOutcome.class);
      oo.id("REMOVED-FOR-COMPARISON");
      oo.issue()
          .forEach(
              i -> {
                if (i.diagnostics() != null) {
                  i.diagnostics(
                      i.diagnostics()
                          .replaceAll("Timestamp:.*(\n|$)", "Timestamp:REMOVED-FOR-COMPARISON"));
                }
              });
      return oo;
    } catch (Exception e) {
      log.error("Failed read response as OperationOutcome: {}", body.prettyPrint());
      throw e;
    }
  }

  @Override
  public ExpectedResponse get(String path, String... params) {
    Response baselineResponse = get("application/json", path, params);
    if (path.startsWith("/actuator")) {
      /* Health checks, metrics, etc. do not have FHIR compliance requirements */
      return ExpectedResponse.of(baselineResponse);
    }

    Response fhirJsonResponse = get("application/fhir+json", path, params);
    Response jsonFhirResponse = get("application/json+fhir", path, params);

    assertThat(fhirJsonResponse.getStatusCode())
        .withFailMessage(
            "status: application/json ("
                + baselineResponse.getStatusCode()
                + ") does not equal application/fhir+json ("
                + fhirJsonResponse.getStatusCode()
                + ")")
        .isEqualTo(baselineResponse.getStatusCode());
    assertThat(jsonFhirResponse.getStatusCode())
        .withFailMessage(
            "status: application/json ("
                + baselineResponse.getStatusCode()
                + ") does not equal application/json+fhir ("
                + jsonFhirResponse.getStatusCode()
                + ")")
        .isEqualTo(baselineResponse.getStatusCode());

    if (baselineResponse.getStatusCode() >= 400) {
      /*
       * Error responses must be returned as OOs but contain a timestamp in the diagnostics
       * that prevents direct comparison.
       */
      assertThat(asOperationOutcomeWithoutDiagnostics(baselineResponse.body()))
          .isEqualTo(asOperationOutcomeWithoutDiagnostics(fhirJsonResponse.body()))
          .isEqualTo(asOperationOutcomeWithoutDiagnostics(jsonFhirResponse.body()));
    } else {
      /*
       * OK responses
       */
      assertThat(baselineResponse.body().asString())
          .isEqualTo(fhirJsonResponse.body().asString())
          .isEqualTo(jsonFhirResponse.body().asString());
    }

    return ExpectedResponse.of(baselineResponse);
  }

  private Response get(String contentType, String path, Object[] params) {
    return service()
        .requestSpecification()
        .contentType(contentType)
        .accept(contentType)
        .request(Method.GET, path, params);
  }

  @Override
  public ExpectedResponse post(String path, Object body) {
    return ExpectedResponse.of(
        service()
            .requestSpecification()
            .contentType("application/fhir+json")
            .accept("application/fhir+json")
            .body(body)
            .request(Method.POST, path));
  }
}
