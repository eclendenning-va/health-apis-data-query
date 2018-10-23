package gov.va.health.api.sentinel.ids;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.Method;
import java.util.function.Supplier;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BasicTestClient implements TestClient {

  private final ServiceDefinition service;
  String contentType;
  Supplier<ObjectMapper> mapper;

  @Override
  public ExpectedResponse get(String path, String params) {
    return ExpectedResponse.of(
        service()
            .requestSpecification()
            .contentType(contentType())
            .request()
            .request(Method.GET, path, params));
  }

  @Override
  public ExpectedResponse post(String path, Object body) {
    try {
      return ExpectedResponse.of(
          service()
              .requestSpecification()
              .contentType(contentType())
              .body(mapper.get().writeValueAsString(body))
              .request(Method.POST, path));
    } catch (JsonProcessingException e) {
      throw new AssertionError("Failed to convert body to JSON", e);
    }
  }
}
