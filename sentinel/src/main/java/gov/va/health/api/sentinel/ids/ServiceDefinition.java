package gov.va.health.api.sentinel.ids;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/** Defines particulars for interacting with a specific service. */
@Value
@Builder
@AllArgsConstructor
public class ServiceDefinition {
  String url;
  int port;

  RequestSpecification requestSpecification() {
    return RestAssured.given()
        .baseUri(url())
        .port(port())
        .relaxedHTTPSValidation()
        .log()
        .ifValidationFails();
  }
}
