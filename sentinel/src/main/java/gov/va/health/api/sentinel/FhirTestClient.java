package gov.va.health.api.sentinel;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.Method;
import io.restassured.response.Response;
import java.util.function.Supplier;
import javax.ws.rs.NotSupportedException;
import lombok.Builder;
import lombok.Value;

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
    // TODO compare results against other media types
    return ExpectedResponse.of(baselineResponse);
  }

  @Override
  public ExpectedResponse post(String path, Object body) {
    throw new NotSupportedException();
  }
}
