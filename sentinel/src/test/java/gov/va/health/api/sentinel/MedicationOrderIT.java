package gov.va.health.api.sentinel;

import static gov.va.health.api.sentinel.ResourceRequest.assertRequest;

import gov.va.api.health.argonaut.api.resources.MedicationOrder;
import gov.va.api.health.argonaut.api.resources.OperationOutcome;
import java.util.Arrays;
import java.util.List;
import org.junit.runners.Parameterized.Parameters;

public class MedicationOrderIT {

  @Parameters(name = "{index}: {0} {2}")
  public static List<Object[]> parameters() {
    ResourceRequest resourceRequest = new ResourceRequest();
    TestIds ids = IdRegistrar.of(Sentinel.get().system()).registeredIds();
    return Arrays.asList(
        assertRequest(
            200, MedicationOrder.class, "/api/MedicationOrder/{id}", ids.medicationOrder()),
        assertRequest(404, OperationOutcome.class, "/api/MedicationOrder/{id}", ids.unknown()),
        assertRequest(
            200,
            MedicationOrder.Bundle.class,
            "/api/MedicationOrder?_id={id}",
            ids.medicationOrder()),
        assertRequest(
            200,
            MedicationOrder.Bundle.class,
            "/api/MedicationOrder?identifier={id}",
            ids.medicationOrder()),
        assertRequest(
            200,
            MedicationOrder.Bundle.class,
            "/api/MedicationOrder?patient={patient}",
            ids.patient()));
  }
}
