package gov.va.health.api.sentinel;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.health.argonaut.api.resources.Conformance;
import org.junit.Test;

public class ConformanceStatementIT {
  @Test
  public void conformanceStatementIsValid() {
    ExpectedResponse response = Sentinel.get().clients().argonaut().get("/api/metadata");
    response.expect(200).expectValid(Conformance.class);
    String rawJson = response.response().asString();
    assertThat(rawJson)
        .withFailMessage("Tabs and newlines break our customer.")
        .doesNotContain("\n", "\t");
  }
}
