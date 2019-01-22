package gov.va.health.api.sentinel;

import static gov.va.health.api.sentinel.ResourceVerifier.test;

import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import gov.va.api.health.argonaut.api.resources.Patient;
import gov.va.health.api.sentinel.categories.AdvancedResource;
import gov.va.health.api.sentinel.categories.BasicResource;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class PatientIT {

  ResourceVerifier verifier = ResourceVerifier.get();

  @Test
  @Category({AdvancedResource.class})
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
  @Category({BasicResource.class})
  public void basic() {
    verifier.verifyAll(
        test(200, Patient.class, "/api/Patient/{id}", verifier.ids().patient()),
        test(404, OperationOutcome.class, "/api/Patient/{id}", verifier.ids().unknown()),
        test(200, Patient.Bundle.class, "/api/Patient?_id={id}", verifier.ids().patient()),
        test(200, Patient.Bundle.class, "/api/Patient?identifier={id}", verifier.ids().patient()),
        test(404, OperationOutcome.class, "/api/Patient?_id={id}", verifier.ids().unknown()));
  }
}
