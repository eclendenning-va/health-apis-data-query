package gov.va.health.api.sentinel;

import gov.va.api.health.argonaut.api.resources.Conformance;
import org.junit.Test;

public class ConformanceStatementIT {
  @Test
  public void conformanceStatementIsValid() {
    Sentinel.get()
        .clients()
        .argonaut()
        .get("/api/metadata")
        .expect(200)
        .expectValid(Conformance.class);
  }

  @Test
  public void conformanceStatementShouldHaveNoNewlineOrTabChars() {
    String conformanceBody =
        Sentinel.get()
            .clients()
            .argonaut()
            .service()
            .requestSpecification()
            .header("Accept", "application/json")
            .get("/api/metadata")
            .getBody()
            .asString();

    if (conformanceBody.contains("/n") || conformanceBody.contains("/t")) {
      throw new AssertionError(
          "Newline or Tab characters exist within the conformance statement JSON body");
    }
  }
}
