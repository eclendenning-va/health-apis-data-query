package gov.va.health.api.sentinel;

import static gov.va.health.api.sentinel.ResourceRequest.assertRequest;

import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import gov.va.api.health.argonaut.api.resources.Patient;
import java.util.Arrays;
import java.util.List;
import org.junit.runners.Parameterized.Parameters;

public class PatientIT {

  @Parameters(name = "{index}: {0} {2}")
  public static List<Object[]> parameters() {
    ResourceRequest resourceRequest = new ResourceRequest();
    TestIds ids = IdRegistrar.of(Sentinel.get().system()).registeredIds();
    return Arrays.asList(
        assertRequest(200, Patient.class, "/api/Patient/{id}", ids.patient()),
        assertRequest(404, OperationOutcome.class, "/api/Patient/{id}", ids.unknown()),
        assertRequest(200, Patient.Bundle.class, "/api/Patient?_id={id}", ids.patient()),
        assertRequest(200, Patient.Bundle.class, "/api/Patient?identifier={id}", ids.patient()),
        assertRequest(404, OperationOutcome.class, "/api/Patient?_id={id}", ids.unknown()));
  }
}
