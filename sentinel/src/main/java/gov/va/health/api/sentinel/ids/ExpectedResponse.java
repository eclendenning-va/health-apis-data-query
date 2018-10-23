package gov.va.health.api.sentinel.ids;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of")
public class ExpectedResponse {

  Response response;

  public ExpectedResponse log() {
    response().then().log().all();
    return this;
  }

  /** Expect the HTTP status code to be the given value. */
  public ExpectedResponse expect(int statusCode) {
    try {
      response.then().statusCode(statusCode);
    } catch (AssertionError e) {
      log();
      throw e;
    }
    return this;
  }

  /** Expect the body to be JSON represented by the given type. */
  public <T> T expect(Class<T> type) {
    try {
      return JacksonConfig.createMapper().readValue(response().asByteArray(), type);
    } catch (IOException e) {
      log();
      throw new AssertionError("Failed to parse JSON body", e);
    }
  }

  /** Expect the body to be a JSON list represented by the given type. */
  public <T> List<T> expectListOf(Class<T> type) {
    try {
      ObjectMapper mapper = JacksonConfig.createMapper();
      return mapper.readValue(
          response().asByteArray(),
          mapper.getTypeFactory().constructCollectionType(List.class, type));
    } catch (IOException e) {
      log();
      throw new AssertionError("Failed to parse JSON body", e);
    }
  }
}
