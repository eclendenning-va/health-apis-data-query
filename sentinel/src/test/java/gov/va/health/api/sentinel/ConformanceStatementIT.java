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
}
