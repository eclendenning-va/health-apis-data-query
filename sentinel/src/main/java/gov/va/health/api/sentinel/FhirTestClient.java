package gov.va.health.api.sentinel;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.Method;
import io.restassured.response.Response;
import java.util.function.Supplier;
import javax.ws.rs.NotSupportedException;
import lombok.Builder;
import lombok.Value;
import static org.assertj.core.api.Assertions.assertThat;



@Value
@Builder
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
    Response fhirJsonResponse = service()
            .requestSpecification()
            .contentType("application/fhir+json")
            .request()
            .request(Method.GET, path, (Object[]) params);
    Response jsonFhirResponse = service()
            .requestSpecification()
            .contentType("application/json+fhir")
            .request()
            .request(Method.GET, path, (Object[]) params);
    assertThat(baselineResponse).withFailMessage("Fhir media types return inconsistent responses.").isEqualTo(fhirJsonResponse).isEqualTo(jsonFhirResponse);
    return ExpectedResponse.of(baselineResponse);
  }

  @Override
  public ExpectedResponse post(String path, Object body) {
    throw new NotSupportedException();
  }


}
