package gov.va.health.api.sentinel;

import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import gov.va.api.health.argonaut.api.resources.Patient.Bundle;
import org.junit.Test;

public class ArgonautValidateIT {

  private final TestIds ids = IdRegistrar.of(Sentinel.get().system()).registeredIds();

  private TestClient argonaut() {
    return Sentinel.get().clients().argonaut();
  }

  @Test
  public void patient() {
    Bundle bundle =
        argonaut().get("/api/Patient?_id={id}", ids.patient()).expectValid(Bundle.class);
    argonaut()
        .post("/api/Patient/$validate", bundle)
        .expect(200)
        .expectValid(OperationOutcome.class);
    /*
     * Murder the resource so it's not valid.
     */
    bundle.entry().get(0).resource().resourceType(null);
    argonaut()
        .post("/api/Patient/$validate", bundle)
        .expect(400)
        .expectValid(OperationOutcome.class);
  }
}
