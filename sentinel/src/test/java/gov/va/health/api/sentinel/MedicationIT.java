package gov.va.health.api.sentinel;

import static gov.va.health.api.sentinel.ResourceVerifier.test;

import gov.va.api.health.argonaut.api.resources.Medication;
import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import org.junit.Test;

public class MedicationIT {

  ResourceVerifier verifier = ResourceVerifier.get();

  @Test
  public void basic() {
    verifier.verifyAll(
        test(200, Medication.class, "/api/Medication/{id}", verifier.ids().medication()),
        test(404, OperationOutcome.class, "/api/Medication/{id}", verifier.ids().unknown()),
        test(200, Medication.Bundle.class, "/api/Medication?_id={id}", verifier.ids().medication()),
        test(
            200,
            Medication.Bundle.class,
            "/api/Medication?identifier={id}",
            verifier.ids().medication()),
        test(404, OperationOutcome.class, "/api/Medication?_id={id}", verifier.ids().unknown()));
  }
}
