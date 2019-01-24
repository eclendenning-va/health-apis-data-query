package gov.va.health.api.sentinel;

import static gov.va.health.api.sentinel.ResourceVerifier.test;

import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import gov.va.api.health.argonaut.api.resources.Patient;
import gov.va.health.api.sentinel.Sentinel.Environment;
import gov.va.health.api.sentinel.categories.Prod;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class PatientIT {

  ResourceVerifier verifier = ResourceVerifier.get();

  @Test
  public void advanced() {
    verifier.verifyAll(
        test(
            200,
            Patient.Bundle.class,
            "/api/Patient?family={family}&gender={gender}",
            verifier.ids().pii().family(),
            verifier.ids().pii().gender()),
        test(
            200,
            Patient.Bundle.class,
            "/api/Patient?given={given}&gender={gender}",
            verifier.ids().pii().given(),
            verifier.ids().pii().gender()),
        test(
            200,
            Patient.Bundle.class,
            "/api/Patient?name={name}&birthdate={birthdate}",
            verifier.ids().pii().name(),
            verifier.ids().pii().birthdate()),
        test(
            200,
            Patient.Bundle.class,
            "/api/Patient?name={name}&gender={gender}",
            verifier.ids().pii().name(),
            verifier.ids().pii().gender()));
  }

  @Test
  @Category({Prod.class})
  public void basic() {
    verifier.verifyAll(
        test(200, Patient.class, "/api/Patient/{id}", verifier.ids().patient()),
        test(200, Patient.Bundle.class, "/api/Patient?_id={id}", verifier.ids().patient()),
        test(404, OperationOutcome.class, "/api/Patient?_id={id}", verifier.ids().unknown()));
  }

  /**
   * In the PROD/QA environments, patient reading is restricted to your unique access-token. Any IDs
   * but your own are revoked with a 403 Forbidden. In environments where this restriction is
   * lifted, the result of an unknown ID should be 404 Not Found.
   */
  @Test
  public void patientMatching() {
    if (Sentinel.environment() == Environment.LOCAL) {
      verifier.verify(
          test(404, OperationOutcome.class, "/api/Patient/{id}", verifier.ids().unknown()));
    } else {
      verifier.verify(
          test(403, OperationOutcome.class, "/api/Patient/{id}", verifier.ids().unknown()));
    }
  }

  /**
   * Although dictated by the Argonaut Spec, the DB team has disabled patient searching by in
   * PROD/QA
   */
  @Test
  public void patientIdentifierSearching() {
    if (Sentinel.environment() == Environment.LOCAL) {
      verifier.verify(
          test(
              200, Patient.Bundle.class, "/api/Patient?identifier={id}", verifier.ids().patient()));
    }
  }
}
