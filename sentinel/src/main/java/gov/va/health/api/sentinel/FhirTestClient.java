package gov.va.health.api.sentinel;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.api.health.argonaut.api.OperationOutcome;
import io.restassured.http.Method;
import io.restassured.response.Response;
import java.util.function.Supplier;
import javax.ws.rs.NotSupportedException;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Value
@Builder
@Slf4j
public class FhirTestClient implements TestClient {
  private final ServiceDefinition service;
  Supplier<ObjectMapper> mapper;

  @Override
  public ExpectedResponse get(String path, String... params) {
    Response baselineResponse =
        service()
            .requestSpecification()
            .contentType("application/json")
            .request()
            .request(Method.GET, path, (Object[]) params);
    Response fhirJsonResponse =
        service()
            .requestSpecification()
            .contentType("application/fhir+json")
            .request()
            .request(Method.GET, path, (Object[]) params);
    Response jsonFhirResponse =
        service()
            .requestSpecification()
            .contentType("application/json+fhir")
            .request()
            .request(Method.GET, path, (Object[]) params);
    final int valid = 200;
    if (baselineResponse.statusCode() == valid
        && fhirJsonResponse.statusCode() == valid
        && jsonFhirResponse.statusCode() == valid) {
      assertThat(baselineResponse.body().asString())
          .withFailMessage("Fhir Media Types are getting inconsistent results.")
          .isEqualTo(fhirJsonResponse.body().asString())
          .isEqualTo(jsonFhirResponse.body().asString());
    } else {
      assertThat(baselineResponse.body().as(OperationOutcome.class).text())
          .withFailMessage("Fhir Media Types are getting inconsistent results.")
          .isEqualTo(fhirJsonResponse.body().as(OperationOutcome.class).text())
          .isEqualTo(jsonFhirResponse.body().as(OperationOutcome.class).text());
    }
    return ExpectedResponse.of(baselineResponse);
  }

  @Override
  public ExpectedResponse post(String path, Object body) {
    throw new NotSupportedException();
  }
}
