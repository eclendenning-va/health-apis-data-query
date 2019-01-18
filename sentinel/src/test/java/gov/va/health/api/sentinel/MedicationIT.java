package gov.va.health.api.sentinel;

import static gov.va.health.api.sentinel.ResourceRequest.assertRequest;

import gov.va.api.health.argonaut.api.resources.Medication;
import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import java.util.Arrays;
import java.util.List;
import org.junit.runners.Parameterized.Parameters;

public class MedicationIT {

  @Parameters(name = "{index}: {0} {2}")
  public static List<Object[]> parameters() {
    ResourceRequest resourceRequest = new ResourceRequest();
    TestIds ids = IdRegistrar.of(Sentinel.get().system()).registeredIds();
    return Arrays.asList(
        assertRequest(200, Medication.class, "/api/Medication/{id}", ids.medication()),
        assertRequest(404, OperationOutcome.class, "/api/Medication/{id}", ids.unknown()),
        assertRequest(200, Medication.Bundle.class, "/api/Medication?_id={id}", ids.medication()),
        assertRequest(
            200, Medication.Bundle.class, "/api/Medication?identifier={id}", ids.medication()),
        assertRequest(404, OperationOutcome.class, "/api/Medication?_id={id}", ids.unknown()));
  }
}
