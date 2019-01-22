package gov.va.health.api.sentinel;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import java.util.Optional;
import java.util.function.Supplier;
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
  Supplier<Optional<String>> accessToken;

  RequestSpecification requestSpecification() {
    RequestSpecification spec =
        RestAssured.given()
            .baseUri(url())
            .port(port())
            .relaxedHTTPSValidation()
            .log()
            .ifValidationFails()
            .header("jargonaut", "true");

    Optional<String> token = accessToken.get();
    if (token.isPresent()) {
      spec = spec.header("Authorization", "Bearer " + token.get());
    }
    return spec;
  }
}
